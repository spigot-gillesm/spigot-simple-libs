package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.command_lib.CommandLib;
import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.GuiLib;
import com.github.spigot_gillesm.player_lib.PlayerLib;
import com.github.spigot_gillesm.spell_lib.SpellLib;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LibTest extends JavaPlugin {

	@Getter
	private static Plugin instance;

	private static void initialize(final Plugin plugin) {
		instance = plugin;
		Formatter.PREFIX = "&f[&2Test&f]";
		FileUtils.PLUGIN_DATA_FOLDER_PATH = instance.getDataFolder().getPath();
	}

	@Override
	public void onEnable() {
		initialize(this);
		CommandLib.initialize(this);
		SpellLib.initialize(this);
		PlayerLib.initialize(this);
		GuiLib.initialize(this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

}
