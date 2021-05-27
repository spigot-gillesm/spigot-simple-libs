package com.github.spigot_gillesm.spell_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class SpellLib {

	public Plugin plugin;

	public void initialize(final Plugin plugin) {
		SpellLib.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(new SpellListener(), plugin);
	}

}
