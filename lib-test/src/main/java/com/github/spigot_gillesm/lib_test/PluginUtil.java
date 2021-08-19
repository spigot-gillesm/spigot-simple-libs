package com.github.spigot_gillesm.lib_test;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PluginUtil {

	public boolean isInt(@NotNull final String str) {
		try {
			Integer.parseInt(ChatColor.stripColor(str));
			return true;
		} catch(final NumberFormatException e) {
			return false;
		}
	}

	public Player getPlayer(@NotNull final String name) {
		return Bukkit.getServer().getPlayer(name);
	}

}
