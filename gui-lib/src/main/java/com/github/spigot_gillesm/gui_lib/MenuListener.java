package com.github.spigot_gillesm.gui_lib;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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

		if(item == null) {
			return;
		}
		final var button = menu.getButton(item);

		if(button.isPresent()) {
			event.setCancelled(true);
			button.get().action(player, event.getClick(), event.getCursor());

			if(!button.get().isCancelOpen()) {
				menu.display(player);
			}
		}

	}

}
