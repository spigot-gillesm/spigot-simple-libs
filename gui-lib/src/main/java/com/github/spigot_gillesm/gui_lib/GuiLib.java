package com.github.spigot_gillesm.gui_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class GuiLib {

	Plugin plugin;

	public void initialize(final Plugin plugin) {
		GuiLib.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
	}

}
