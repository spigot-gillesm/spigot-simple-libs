package com.github.spigot_gillesm.rp_classes;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PluginUtil {

	public Player getPlayer(@NotNull final String name) {
		return Bukkit.getServer().getPlayer(name);
	}

}
