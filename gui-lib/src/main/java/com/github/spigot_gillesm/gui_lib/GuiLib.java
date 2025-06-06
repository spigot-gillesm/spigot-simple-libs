package com.github.spigot_gillesm.gui_lib;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GuiLib {

	@Getter(AccessLevel.PACKAGE)
	private static Plugin instance;

	public static void initialize(@NotNull Plugin plugin) {
		GuiLib.instance = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
	}

}
