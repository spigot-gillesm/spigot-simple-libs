package com.github.spigot_gillesm.player_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class PlayerLib {

	Plugin plugin;

	public void initialize(final Plugin plugin) {
		PlayerLib.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
	}

}
