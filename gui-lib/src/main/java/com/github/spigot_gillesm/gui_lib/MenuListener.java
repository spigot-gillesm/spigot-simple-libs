package com.github.spigot_gillesm.gui_lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

	@EventHandler
	private void onInventoryClick(final InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		final var player = (Player) event.getWhoClicked();
		final var menu = SimpleMenu.getMenu(player);

		if(menu == null) {
			return;
		}
		final var item = event.getCurrentItem();

		if(menu.isCancelActions()) {
			event.setCancelled(true);
		}
		final SimpleMenuInteractEvent menuEvent = new SimpleMenuInteractEvent(event.getSlot(), item);
		Bukkit.getServer().getPluginManager().callEvent(menuEvent);

		if(menuEvent.isCancelled()) {
			event.setCancelled(true);
		}

		if(item == null) {
			return;
		}
		final var button = menu.getButton(item);

		if(button.isPresent()) {
			event.setCancelled(true);
			//If action returns true -> re-display menu
			if(button.get().action(player, event.getClick(), event.getCursor())) {
				menu.display(player);
			}
		}

	}

	@EventHandler
	private void onInventoryClosed(final InventoryCloseEvent event) {
		final var player = (Player) event.getPlayer();
		final var menu = SimpleMenu.getMenu(player);

		if(menu != null) {
			menu.onClose(player);
			player.removeMetadata("SIMPLE_MENU", GuiLib.plugin);
		}
	}

}
