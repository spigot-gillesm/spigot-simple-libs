package com.github.spigot_gillesm.item_lib;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemLib {

	@Getter(AccessLevel.PACKAGE)
	private Plugin plugin;

	public void initialize(@NotNull final Plugin plugin) {
		ItemLib.plugin = plugin;
	}

}
