package com.github.spigot_gillesm.epicworlds_utils;

import com.github.spigot_gillesm.epicworlds_utils.villager.TradeLoader;
import com.github.spigot_gillesm.epicworlds_utils.villager.VillagerListener;
import com.github.spigot_gillesm.file_utils.FileUtils;
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
		FileUtils.PLUGIN_DATA_FOLDER_PATH = plugin.getDataFolder().getPath();
	}

	@Override
	public void onEnable() {
		initialize(this);
		TradeLoader.getInstance().loadTrades();
		Bukkit.getServer().getPluginManager().registerEvents(new WorldListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new VillagerListener(), this);
	}

}
