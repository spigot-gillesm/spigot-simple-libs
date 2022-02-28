package com.github.spigot_gillesm.rp_classes.player;

import com.github.spigot_gillesm.item_lib.ItemUtil;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import com.github.spigot_gillesm.rp_classes.DependencyManager;
import com.github.spigot_gillesm.rp_classes.GearRestriction;
import com.github.spigot_gillesm.rp_classes.rp_class.ClassManager;
import com.github.spigot_gillesm.rp_classes.rp_class.RpClass;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class PlayerManager {

	/*private final Set<PlayerClassData> REGISTERED_PLAYER_CLASS_DATA = new HashSet<>();

	private PlayerClassData registerNew(final UUID uuid) {
		final var data = new PlayerClassData(uuid);
		REGISTERED_PLAYER_CLASS_DATA.add(data);

		return data;
	}

	@NotNull
	public PlayerClassData getPlayerClassData(final Player player) {
		return REGISTERED_PLAYER_CLASS_DATA.stream()
				.filter(d -> d.getUuid().equals(player.getUniqueId()))
				.findFirst()
				.orElseGet(() -> registerNew(player.getUniqueId()));
	}

	public void setPlayerClass(final Player player, final RpClass rpClass) {
		getPlayerClassData(player).setClass(rpClass);
	}

	public int getSpellRank(final Player player, final String id) {
		return 1;
	}*/

	public void setPlayerRpClass(final Player player, final RpClass rpClass) {
		DataManager.getData(player).setTagValue(PlayerTag.CLASS, rpClass.getId());
	}

	public Optional<RpClass> getRpClass(final Player player) {
		final var id = DataManager.getData(player).getTagValue(PlayerTag.CLASS);
		return id != null ? ClassManager.getRpClass(id.toString().toUpperCase()) : Optional.empty();
	}

	public boolean hasClass(final Player player) {
		return getRpClass(player).isPresent();
	}

	/**
	 * Check if the player meets the requirements to equip the item.
	 *
	 * @param player the player
	 * @param itemStack the item
	 * @return true if the player can equip the item
	 */
	public boolean canEquip(final Player player, final ItemStack itemStack) {
		if(DependencyManager.isMmoItem(itemStack)) {
			return isMmoItemAllowed(player, itemStack);
		}
		//Check if the material is restricted for any class first
		if(isMaterialListed(itemStack.getType())) {
			return getRpClass(player).map(c -> isMaterialAllowed(c, itemStack.getType())).orElse(true);
		}

		//If the item is not restricted in any case (example: stick), return true
		return true;
	}

	/**
	 * Check if the player can use the given mmoItem.
	 *
	 * @param player the player
	 * @param itemStack the itemStack
	 * @return true if the player can equip the item
	 */
	private boolean isMmoItemAllowed(final Player player, final ItemStack itemStack) {
		//Check if there's any filter on the item type
		if(!isMmoItemTypeListed(DependencyManager.getMmoItemsType(itemStack))) {
			return true;
		}

		return canEquipMmoItemType(player, itemStack);
	}

	/**
	 * Check if the given mmoItem type is listed among the gear restrictions.
	 *
	 * @param type the mmoItem type
	 * @return true if the type is listed
	 */
	private boolean isMmoItemTypeListed(final String type) {
		return Arrays.stream(GearRestriction.values())
				.anyMatch(gearRestriction -> gearRestriction.name().equalsIgnoreCase(type));
	}

	/**
	 * Check if the player can equip the given mmoItem based on its type.
	 *
	 * @param player the player
	 * @param itemStack the itemStack
	 * @return true if the player can equip the item
	 */
	private boolean canEquipMmoItemType(final Player player, final ItemStack itemStack) {
		final var type = DependencyManager.getMmoItemsType(itemStack);

		if("ARMOR".equals(type)) {
			return getRpClass(player).map(c -> isMmoItemArmorTypeAllowed(c, itemStack)).orElse(true);
		} else {
			return getRpClass(player).map(c -> isMmoItemTypeAllowed(c, type)).orElse(true);
		}
	}

	/**
	 * Check if the given RpClass can equip the given mmoItem type.
	 *
	 * @param rpClass the rpClass
	 * @param type the mmoItem type
	 * @return true if the rpClass allows this type
	 */
	private boolean isMmoItemTypeAllowed(final RpClass rpClass, final String type) {
		return rpClass.getGearRestrictions().stream().anyMatch(restriction -> restriction.name().equalsIgnoreCase(type));
	}

	/**
	 * Check if the given rpClass can equip the given mmoItem armor.
	 *
	 * @param rpClass the rpClass
	 * @param itemStack the itemStack
	 * @return true if the rpClass allows this armor type
	 */
	private boolean isMmoItemArmorTypeAllowed(final RpClass rpClass, final ItemStack itemStack) {
		final var restrictions = rpClass.getGearRestrictions();

		if(ItemUtil.hasLineInLore(itemStack, "Leather", false)) {
			return restrictions.contains(GearRestriction.LEATHER);
		} else if(ItemUtil.hasLineInLore(itemStack, "Mail", false)) {
			return restrictions.contains(GearRestriction.MAIL);
		} else if(ItemUtil.hasLineInLore(itemStack, "Plate", false)) {
			return restrictions.contains(GearRestriction.PLATE);
		} else if(ItemUtil.hasLineInLore(itemStack,"Heavy Plate", false)) {
			return restrictions.contains(GearRestriction.HEAVY_PLATE);
		} else {
			return false;
		}
	}

	/**
	 * Check if the given material is listed among the gear restrictions.
	 *
	 * @param material the material
	 * @return true if the material is listed
	 */
	private boolean isMaterialListed(final Material material) {
		return Arrays.stream(GearRestriction.values()).anyMatch(gearRestriction -> gearRestriction.getMaterials().contains(material));
	}

	/**
	 * Check if the given rpClass can equip the given item material
	 *
	 * @param rpClass the rpClass
	 * @param material the material
	 * @return true if the rpClass allows this item material
	 */
	private boolean isMaterialAllowed(final RpClass rpClass, final Material material) {
		return rpClass.getGearRestrictions().stream().anyMatch(restriction -> restriction.getMaterials().contains(material));
	}

}
