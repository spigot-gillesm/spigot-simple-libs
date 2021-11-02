package com.github.spigot_gillesm.command_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class CommandLib {

	public void initialize(@NotNull final Plugin plugin) {
		CommandManager.registerMainCommands(plugin);
	}

}
