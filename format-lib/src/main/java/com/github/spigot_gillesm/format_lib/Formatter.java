package com.github.spigot_gillesm.format_lib;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This utility class provides various useful methods to help formatting text, messages, etc...
 * Inspired by Kangarko (Matej Pacan).
 *
 * @author Gilles_M
 * @version 1.0.0
 */
@UtilityClass
public class Formatter {

	/**
	 * The prefix used for console messages
	 */
	public static String PREFIX = "";

	/**
	 * Colorize a message using the & color codes.
	 *
	 * @param message the message to colorize
	 * @return the colorized message
	 */
	public String colorize(@NotNull String message) {
		if(!StringUtils.isBlank(message)) {
			return ChatColor.translateAlternateColorCodes('&', message);
		} else {
			return "";
		}
	}

	/**
	 * Colorize multiple lines using the & color codes.
	 *
	 * @param lines the lines to colorize
	 * @return the colorized lines
	 */
	public List<String> colorize(@NotNull List<String> lines) {
		return lines.stream().map(Formatter::colorize).collect(Collectors.toList());
	}

	/**
	 * Send a message to a commander sender.
	 *
	 * @param toWhom the command sender
	 * @param message the message
	 */
	public void tell(@NotNull CommandSender toWhom, @NotNull String message) {
		toWhom.sendMessage(colorize(message));
	}

	/**
	 * Send messages to a commander sender.
	 *
	 * @param toWhom the command sender
	 * @param messages the messages
	 */
	public void tell(@NotNull CommandSender toWhom, @NotNull String... messages) {
		for(final String message : messages) {
			tell(toWhom, message);
		}
	}

	/**
	 * Send a message to the console.
	 *
	 * @param message the info message
	 */
	public void info(@NotNull String message) {
		if(!StringUtils.isBlank(PREFIX)) {
			Bukkit.getConsoleSender().sendMessage(colorize(PREFIX + " &f" + message));
		} else {
			Bukkit.getConsoleSender().sendMessage(colorize(message));
		}
	}

	/**
	 * Send a warning to the console.
	 *
	 * @param message the warning message
	 */
	public void warning(@NotNull String message) {
		if(!StringUtils.isBlank(PREFIX)) {
			Bukkit.getConsoleSender().sendMessage(colorize(PREFIX + " &f/&6!&f\\ &6" + message));
		} else {
			Bukkit.getConsoleSender().sendMessage(colorize("&f/&6!&f\\ &6" + message));
		}
	}

	/**
	 * Send an error to the console.
	 *
	 * @param message the error message
	 */
	public void error(@NotNull String message) {
		if(!StringUtils.isBlank(PREFIX)) {
			Bukkit.getConsoleSender().sendMessage(colorize(PREFIX + " &f/&l&4!&cERROR&l&4!&f\\ &c" + message));
		} else {
			Bukkit.getConsoleSender().sendMessage(colorize("&f/&l&4!&cERROR&l&4!&f\\ &c" + message));
		}
	}

}
