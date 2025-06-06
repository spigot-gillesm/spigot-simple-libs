package com.github.spigot_gillesm.player_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@UtilityClass
public class PlayerLib {

	Plugin plugin;

	public void initialize(@NotNull Plugin plugin) {
		PlayerLib.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
	}

	public void saveAllData() throws IOException, InvalidConfigurationException {
		for(final Player player : Bukkit.getServer().getOnlinePlayers()) {
			DataManager.getData(player).writeToFile();
		}
	}

	public void loadAllData() throws IOException, InvalidConfigurationException {
		for(final Player player : Bukkit.getServer().getOnlinePlayers()) {
			DataManager.getData(player).loadFromFile();
		}
	}

}
