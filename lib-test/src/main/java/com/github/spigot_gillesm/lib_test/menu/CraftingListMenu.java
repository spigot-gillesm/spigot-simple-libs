package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.PlayerManager;
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

public class CraftingListMenu extends ListingMenu {

	@Override
	protected List<SimpleButton> generateButtons(final Player viewer) {
		final List<SimpleButton> buttons = new ArrayList<>();
		final var profession = PlayerManager.getProfession(viewer);

		if(profession.isPresent()) {
			for(final var recipe : CraftManager.getItemsFromProfession(profession.get())) {
				if(recipe instanceof BreweryCraftRecipe) {
					final var breweryRecipe = (BreweryCraftRecipe) recipe;
					buttons.add(new SimpleButton(breweryRecipe.getResult()) {
						@Override
						public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
							final var inventory = (BrewerInventory) Bukkit.getServer().createInventory(player,
									InventoryType.BREWING);
							inventory.setIngredient(breweryRecipe.getReagent());
							inventory.setFuel(breweryRecipe.getFuel());

							for (var i = 0; i < 3; i++) {
								inventory.setItem(i, breweryRecipe.getReceptacle());
							}
							player.openInventory(inventory);
							DataManager.getData(player).setRawValue("BREWING_MENU", this, false);

							return false;
						}
					});
				} else {
					buttons.add(new SimpleButton(recipe.getItem()) {
						@Override
						public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
							final var menu = new CraftingMenu(SimpleMenu.getMenu(viewer));
							menu.setItem((CraftRecipe) recipe);
							menu.display(viewer);

							return false;
						}
					});
				}
			}
		}

		return buttons;
	}

}
