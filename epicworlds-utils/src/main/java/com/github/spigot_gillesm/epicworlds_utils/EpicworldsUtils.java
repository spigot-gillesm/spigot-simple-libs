package com.github.spigot_gillesm.epicworlds_utils;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicworldsUtils extends JavaPlugin {

	@Getter
	private static Plugin instance;

	private static void initialize(final Plugin plugin) {
		instance = plugin;
		Formatter.PREFIX = "&f[&a&lEpicWorlds&9Utils&f]";
	}

	@Override
	public void onEnable() {
		initialize(this);
		Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
	}

}
