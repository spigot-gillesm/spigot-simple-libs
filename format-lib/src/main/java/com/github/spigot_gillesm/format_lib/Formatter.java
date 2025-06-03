package com.github.spigot_gillesm.format_lib;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This utility class provides various useful methods to help to format text, messages, etc...
 * Inspired by Kangarko (Matej Pacan).
 *
 * @author Gilles_M
 * @version 1.0.0
 */
public class Formatter {

	private Formatter() { }

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
	public static String colorize(@NotNull final String message) {
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
	public static List<String> colorize(@NotNull final List<String> lines) {
		final List<String> messages = new ArrayList<>();

		for(final String line : lines) {
			messages.add(colorize(line));
		}

		return messages;
	}

	/**
	 * Send a message to a commander sender.
	 *
	 * @param toWhom the command sender
	 * @param message the message
	 */
	public static void tell(@NotNull final CommandSender toWhom, @NotNull final String message) {
		if(message != null) {
			toWhom.sendMessage(colorize(message));
		}
	}

	/**
	 * Send messages to a commander sender.
	 *
	 * @param toWhom the command sender
	 * @param messages the messages
	 */
	public static void tell(@NotNull final CommandSender toWhom, @NotNull final String... messages) {
		for (final String message : messages) {
			tell(toWhom, message);
		}
	}

	/**
	 * Send a message to the console.
	 *
	 * @param message the info message
	 */
	public static void info(@NotNull final String message) {
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
	public static void warning(@NotNull final String message) {
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
	public static void error(@NotNull final String message) {
		if(!StringUtils.isBlank(PREFIX)) {
			Bukkit.getConsoleSender().sendMessage(colorize(PREFIX + " &f/&l&4!&cERROR&l&4!&f\\ &c" + message));
		} else {
			Bukkit.getConsoleSender().sendMessage(colorize("&f/&l&4!&cERROR&l&4!&f\\ &c" + message));
		}
	}

}
