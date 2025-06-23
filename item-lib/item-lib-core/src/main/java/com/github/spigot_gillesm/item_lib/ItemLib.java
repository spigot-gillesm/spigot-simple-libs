package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.item_lib.version.ServerVersion;
import com.github.spigot_gillesm.item_lib.version.VersionWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemLib {

	@Getter(AccessLevel.PACKAGE)
	private Plugin plugin;

	private ServerVersion serverVersion;

	public void initialize(@NotNull Plugin plugin) {
		ItemLib.plugin = plugin;
		ItemLib.serverVersion = new ServerVersion();
	}

	VersionWrapper getVersionWrapper() {
		return serverVersion.getVersionWrapper();
	}

}
