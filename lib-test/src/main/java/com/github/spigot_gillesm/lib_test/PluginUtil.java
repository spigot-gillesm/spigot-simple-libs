package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

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

	@SuppressWarnings("Unchecked")
	public <T> Optional<T> instantiateFromClass(final Class<T> clazz) {
		final var constructor = Arrays.stream(clazz.getDeclaredConstructors()).findFirst();

		if(constructor.isPresent()) {
			try {
				return (Optional<T>) Optional.of(constructor.get().newInstance());
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
				Formatter.error("Error instantiating from class " + clazz.getName() + ".");
			}
		}

		return Optional.empty();
	}

	public int clamp(final int value, final int min, final int max) {
		if(max < min) {
			throw new IllegalArgumentException("The minimum value cannot be greater than the maximum value.");
		}
		return Math.max(min, Math.min(max, value));
	}

}
