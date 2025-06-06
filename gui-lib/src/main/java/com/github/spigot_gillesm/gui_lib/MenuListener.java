package com.github.spigot_gillesm.gui_lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MenuListener implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player player)) {
			return;
		}
        final Optional<SimpleMenu> oMenu = SimpleMenu.getMenu(player);

		if(oMenu.isEmpty()) {
			return;
		}
		final SimpleMenu menu = oMenu.get();
		final ItemStack item = event.getCurrentItem();

		if(menu.isCancelActions()) {
			event.setCancelled(true);
		}
		final var menuEvent = new SimpleMenuInteractEvent(event.getSlot(), item);
		Bukkit.getServer().getPluginManager().callEvent(menuEvent);

		if(menuEvent.isCancelled()) {
			event.setCancelled(true);
		}
		if(item == null) {
			return;
		}
		final Optional<SimpleButton> button = menu.getButton(item);

		if(button.isPresent()) {
			event.setCancelled(true);

			//If action returns true -> re-display menu
			if(button.get().action(player, event.getClick(), event.getCursor())) {
				menu.display(player);
			}
		}
	}

	@EventHandler
	private void onInventoryClosed(InventoryCloseEvent event) {
		final var player = (Player) event.getPlayer();

		SimpleMenu.getMenu(player).ifPresent(menu -> {
			menu.onClose(player);
			player.removeMetadata(SimpleMenu.METADATA_KEY, GuiLib.getInstance());
		});
	}

	@EventHandler
	protected void onPlayerQuit(PlayerQuitEvent event) {
		//Ensures a player leaving the server removes any menu
		event.getPlayer().removeMetadata(SimpleMenu.METADATA_KEY, GuiLib.getInstance());
	}

}
