package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.brew.BrewManager;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
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
		final var type = PlayerManager.getProfessionType(viewer);

		for(final var item : CraftManager.getItemsFromProfession(type)) {
			buttons.add(new SimpleButton(item.getResult()) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					final var menu = new CraftingMenu(SimpleMenu.getMenu(viewer));
					menu.setItem(item);
					menu.display(viewer);

					return false;
				}
			});
		}
		for(final var recipe : BrewManager.getRecipesFromProfession(type)) {
			buttons.add(new SimpleButton(recipe.getResult()) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					final var inventory = (BrewerInventory) Bukkit.getServer().createInventory(player,
							InventoryType.BREWING, "&8Brewing Recipe");
					inventory.setIngredient(recipe.getIngredient());
					inventory.setFuel(recipe.getFuel());

					for(var i = 0; i < 3; i++) {
						inventory.setItem(i, recipe.getReceptacle());
					}
					player.openInventory(inventory);
					DataManager.getData(player).setRawValue("BREWING_MENU", this, false);

					return false;
				}
			});
		}

		return buttons;
	}

}
