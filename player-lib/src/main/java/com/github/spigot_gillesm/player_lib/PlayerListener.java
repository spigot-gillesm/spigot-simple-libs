package com.github.spigot_gillesm.player_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerListener implements Listener {

	@EventHandler
	private void onPlayerQuit(final PlayerQuitEvent event) {
		try {
			DataManager.getData(event.getPlayer()).writeToFile();
		} catch (IOException | InvalidConfigurationException exception) {
			Formatter.error(String.format("Error save player %s data file", event.getPlayer().getName()));
			Formatter.error(exception.getMessage());
		}
	}

	@EventHandler
	private void onPlayerJoin(final PlayerJoinEvent event) {
		try {
			DataManager.getData(event.getPlayer()).loadFromFile();
		} catch (IOException | InvalidConfigurationException exception) {
			Formatter.error(String.format("Error loading player %s data file", event.getPlayer().getName()));
			Formatter.error(exception.getMessage());
		}
	}

}
