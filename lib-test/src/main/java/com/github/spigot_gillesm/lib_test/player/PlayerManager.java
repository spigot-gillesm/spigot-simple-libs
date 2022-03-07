package com.github.spigot_gillesm.lib_test.player;

import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import com.github.spigot_gillesm.lib_test.item.ItemManager;
import com.github.spigot_gillesm.lib_test.profession.Privilege;
import com.github.spigot_gillesm.lib_test.profession.Profession;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@UtilityClass
public class PlayerManager {

	public void setPlayerProfession(final Player player, final Profession profession) {
		DataManager.getData(player).setTagValue(PlayerTag.PROFESSION, ProfessionManager.getId(profession));
	}

	public void setPlayerProfession(final Player player, final String id) {
		ProfessionManager.getProfession(id).ifPresent(p -> DataManager.getData(player).setTagValue(PlayerTag.PROFESSION, id.toUpperCase()));
	}

	public void removePlayerProfession(final Player player) {
		DataManager.getData(player).removeValue(PlayerTag.PROFESSION);
	}

	public void decrementLevel(final Player player) {
		player.setLevel(player.getLevel() - 1);
	}

	public Optional<Profession> getProfession(final Player player) {
		final var id = DataManager.getData(player).getTagValue(PlayerTag.PROFESSION);
		return id != null ? ProfessionManager.getProfession(id.toString().toUpperCase()) : Optional.empty();
	}

	public int getProfessionLevel(final Player player) {
		final var playerData = DataManager.getData(player);
		return playerData.getTagValue(PlayerTag.PROFESSION_LEVEL, Integer.class, 0);
	}

	public void setProfessionLevel(final Player player, final int level) {
		DataManager.getData(player).setTagValue(PlayerTag.PROFESSION_LEVEL, level);
	}

	/**
	 *
	 * @param player the player
	 * @param craftEntity the craft entity
	 * @return true if the player profession level has changed
	 */
	public boolean updateProfessionLevel(final Player player, final CraftEntity craftEntity) {
		final var professionLevel = getProfessionLevel(player);

		if(professionLevel < craftEntity.getMetaData().getLevelCap()) {
			setProfessionLevel(player, professionLevel + craftEntity.getMetaData().getLevelGain());

			return true;
		} else {
			return false;
		}
	}

	public void teachRecipe(final Player player, final CraftEntity craftEntity) {
		if(!craftEntity.getMetaData().isKnownByDefault()) {
			DataManager.getData(player).addTagValue(PlayerTag.RECIPES, craftEntity.getId());
		}
	}

	public boolean hasProfession(final Player player) {
		return getProfession(player).isPresent();
	}

	private boolean hasRequiredProfessionLevel(final Player player, final CraftEntity craftEntity) {
		return getProfessionLevel(player) >= craftEntity.getMetaData().getRequiredLevel();
	}

	private boolean knowCraft(final Player player, final CraftEntity craftEntity) {
		if(craftEntity.getMetaData().isKnownByDefault()) {
			return true;
		} else {
			return DataManager.getData(player).getTagList(PlayerTag.RECIPES, String.class).contains(craftEntity.getId());
		}
	}

	public boolean meetRequirements(final Player player, final CraftEntity craftEntity) {
		return knowCraft(player, craftEntity) && hasRequiredProfessionLevel(player, craftEntity);
	}

	public boolean hasPrivilege(final Player player, final Privilege privilege) {
		return getProfession(player).map(p -> p.hasPrivilege(privilege)).orElse(false);
	}

	public boolean canEquip(@NotNull final Player player, @NotNull final ItemStack itemStack) {
		final var professions = ItemManager.getItemProfessions(itemStack);

		//If no professions is required for the item -> always equipable
		if(professions.isEmpty()) {
			return true;
		} else {
			//Check if the player has one of the listed profession (or no profession at all)
			return getProfession(player).filter(professions::contains).isPresent();
		}
	}

}
