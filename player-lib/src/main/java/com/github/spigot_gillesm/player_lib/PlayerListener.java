package com.github.spigot_gillesm.player_lib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler
	private void onPlayerQuit(final PlayerQuitEvent event) {
		DataManager.getData(event.getPlayer()).writeToFile();
	}

	@EventHandler
	private void onPlayerJoin(final PlayerJoinEvent event) {
		DataManager.getData(event.getPlayer()).loadFromFile();
	}

}
