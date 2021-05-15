package com.github.spigot_gillesm.command_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class CommandLib {

	public void initialize(final Plugin plugin) {
		CommanderManager.registerMainCommands(plugin);
	}

}
