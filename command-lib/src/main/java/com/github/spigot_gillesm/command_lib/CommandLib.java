package com.github.spigot_gillesm.command_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class CommandLib {

	public <T extends JavaPlugin> void initialize(@NotNull T plugin) {
		CommandManager.getInstance().registerMainCommands(plugin);
	}

}
