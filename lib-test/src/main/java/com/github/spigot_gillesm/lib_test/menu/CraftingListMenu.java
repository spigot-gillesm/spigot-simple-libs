package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.player.PlayerManager;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.BreweryCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.player_lib.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * The menu represented by this class allows the player to list and see all its available recipes.
 */
public class CraftingListMenu extends ListingMenu {

	@Override
	protected List<SimpleButton> generateButtons(final Player viewer) {
		final List<SimpleButton> buttons = new ArrayList<>();
		final var profession = PlayerManager.getProfession(viewer);

		profession.ifPresent(p -> CraftManager.getItemsFromProfession(p)
				.stream()
				.filter(craftEntity -> PlayerManager.meetRequirements(viewer, craftEntity))
				.forEach(craftEntity -> {
					if(craftEntity instanceof BreweryCraftRecipe) {
						buttons.add(generateBrewRecipeButton((BreweryCraftRecipe) craftEntity));
					} else {
						buttons.add(generateRecipeButton(craftEntity));
					}
				})
		);

		return buttons;
	}

	private SimpleButton generateBrewRecipeButton(final BreweryCraftRecipe breweryRecipe) {
		return new SimpleButton(breweryRecipe.getResult()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				final var inventory = (BrewerInventory) Bukkit.getServer().createInventory(player, InventoryType.BREWING);
				inventory.setIngredient(breweryRecipe.getReagent());
				inventory.setFuel(breweryRecipe.getFuel());

				for (var i = 0; i < 3; i++) {
					inventory.setItem(i, breweryRecipe.getReceptacle());
				}
				player.openInventory(inventory);
				DataManager.getData(player).setRawValue("BREWING_MENU", this, false);

				return false;
			}
		};
	}

	private SimpleButton generateRecipeButton(final CraftEntity craftEntity) {
		return new SimpleButton(craftEntity.getItem()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				final var menu = new RecipeMenu(SimpleMenu.getMenu(viewer));
				menu.setItem((CraftRecipe) craftEntity);
				menu.display(viewer);

				return false;
			}
		};
	}

}
