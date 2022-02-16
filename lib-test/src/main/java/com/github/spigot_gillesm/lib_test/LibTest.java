package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.command_lib.CommandLib;
import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.GuiLib;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.lib_test.profession.WorkstationManager;
import com.github.spigot_gillesm.player_lib.PlayerLib;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LibTest extends JavaPlugin {

	@Getter
	private static Plugin instance;

	private static void initialize(final Plugin plugin) {
		instance = plugin;
		Formatter.PREFIX = "&f[&2&lRP&9Professions&f]";
		FileUtils.PLUGIN_DATA_FOLDER_PATH = instance.getDataFolder().getPath();
	}

	@Override
	public void onEnable() {
		initialize(this);
		CommandLib.initialize(this);
		PlayerLib.initialize(this);
		GuiLib.initialize(this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		//Workstations must be loaded before professions and crafts
		//Items must be loaded before crafts
		WorkstationManager.loadWorkstations();
		ProfessionManager.loadProfessions();
		ItemManager.loadItems();
		CraftManager.loadCrafts();
	}

	@Override
	public void onDisable() {
		PlayerLib.saveAllData();
	}

}
