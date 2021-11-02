package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.DynamicCraft;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CraftStationMenu extends SimpleMenu {

	private static final Set<Integer> CRAFTING_SLOTS = Stream.of(3+9, 4+9, 5+9, 12+9, 13+9, 14+9, 21+9, 22+9, 23+9)
			.collect(Collectors.toCollection(HashSet::new));

	private static final int RESULT_SLOT = 25;

	private static final int REAGENTS_SLOT = 49;

	private final SimpleButton craftButton;

	protected CraftStationMenu() {
		this.craftButton = new SimpleButton(getCraftIcon()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				final var inventory = player.getOpenInventory().getTopInventory();
				final var content = inventory.getContents();

				final var pattern = new ItemStack[] {content[3+9], content[4+9], content[5+9], content[12+9],
						content[13+9], content[14+9], content[21+9], content[22+9], content[23+9]};
				final var craftItem = CraftManager.getItemFromPattern(pattern);

				//Check if patterns match any item, check if player can craft it, check if the result slot is empty
				if(craftItem.isPresent() && craftItem.get().canCraft(player) && content[RESULT_SLOT] == null) {
					runCraft(player, inventory, craftItem.get());
				}

				return false;
			}
		};
		setTitle("&8" + getTitle());
		setSize(6*9);
	}

	private void clearGrid(final Player player, final Inventory inventory) {
		inventory.setItem(REAGENTS_SLOT, null);
		CRAFTING_SLOTS.forEach(s -> inventory.setItem(s, null));
		player.getWorld().playSound(player.getLocation(), getCraftSound(), 1, 1);
	}

	private void runDynamicCraft(final Player player, final CraftEntity craftEntity) {
		if(!(craftEntity instanceof DynamicCraft)) {
			return;
		}
		//Instantiate a new dyn menu
		final var menu = PluginUtil.instantiateFromClass(craftEntity.getDynamicCraftMenu());
		//Start the craft runnable and attach it to the menu
		menu.ifPresent(m -> {
			m.setRecipeRunnable(((DynamicCraft) craftEntity).start(player, m));
			m.setResult(craftEntity.getResult());
			m.display(player);
		});
	}

	private void runCraft(final Player player, final Inventory inventory, final CraftEntity craftEntity) {
		final var reagents = inventory.getContents()[REAGENTS_SLOT];

		//Check if the craft needs reagents
		if(craftEntity.getReagent() != null) {

			if(craftEntity.getReagent().equals(reagents)) {
				//Clear the crafting grid + reagent slot
				clearGrid(player, inventory);

				//Check if the craft has a dynamic craft to run
				if(craftEntity.isDynamicCraft()) {
					runDynamicCraft(player, craftEntity);
				} else {
					//If not then just set the result immediately
					inventory.setItem(RESULT_SLOT, craftEntity.getResult());
				}

			}

		} else {
			//Clear the crafting grid
			clearGrid(player, inventory);

			//Check if the craft has a dynamic craft to run
			if(craftEntity.isDynamicCraft()) {
				runDynamicCraft(player, craftEntity);
			} else {
				//If not then just set the result immediately
				inventory.setItem(RESULT_SLOT, craftEntity.getResult());
			}
		}
	}

	protected abstract SimpleItem getCraftIcon();

	protected abstract String getTitle();

	protected Sound getCraftSound() {
		return Sound.ENTITY_PLAYER_LEVELUP;
	}

	@Override
	protected List<SimpleButton> registerLastButtons() {
		return new ArrayList<>(Collections.singletonList(craftButton));
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 19) {
			return craftButton.getIcon();
		} else if(slot == RESULT_SLOT || CRAFTING_SLOTS.contains(slot) || slot == REAGENTS_SLOT) {
			return null;
		} else {
			//Fill the rest
			return SimpleItem.newBuilder()
					.material(Material.GRAY_STAINED_GLASS_PANE)
					.displayName("&f")
					.localizedName("CANCEL_MENU")
					.build()
					.getItemStack();
		}
	}

	@Override
	public void onClose(final Player player) {
		final var inventory = player.getOpenInventory().getTopInventory().getContents();

		//Drop the remaining items from the menu when closed
		CRAFTING_SLOTS.stream()
				.filter(s -> inventory[s] != null)
				.forEach(s -> player.getWorld().dropItemNaturally(player.getLocation(), inventory[s]));
		if(inventory[RESULT_SLOT] != null) {
			player.getWorld().dropItemNaturally(player.getLocation(), inventory[RESULT_SLOT]);
		}
		if(inventory[REAGENTS_SLOT] != null) {
			player.getWorld().dropItemNaturally(player.getLocation(), inventory[REAGENTS_SLOT]);
		}
	}

}
