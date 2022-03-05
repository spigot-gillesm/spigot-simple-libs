package com.github.spigot_gillesm.rp_classes;

import com.github.spigot_gillesm.command_lib.CommandLib;
import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.GuiLib;
import com.github.spigot_gillesm.player_lib.PlayerLib;
import com.github.spigot_gillesm.rp_classes.player.PlayerListener;
import com.github.spigot_gillesm.rp_classes.rp_class.ClassManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpClasses extends JavaPlugin {

	@Getter
	private static Plugin instance;

	private static void initialize(final Plugin plugin) {
		instance = plugin;
		Formatter.PREFIX = "&f[&2&lRP&dClasses&f]";
		FileUtils.PLUGIN_DATA_FOLDER_PATH = instance.getDataFolder().getPath();
	}

	@Override
	public void onEnable() {
		DependencyManager.loadDependencies();
		initialize(this);
		CommandLib.initialize(this);
		PlayerLib.initialize(this);
		GuiLib.initialize(this);
		ClassManager.loadClasses();
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public void onDisable() {
		PlayerLib.saveAllData();
	}
}
