package com.github.spigot_gillesm.command_lib;


import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class CommandManager {

	private static final CommandManager INSTANCE = new CommandManager();

	private CommandManager() { }

	private void registerCommand(@NotNull final JavaPlugin plugin, @NotNull final Command command) {
		Formatter.info("Registering " + command.getName() + " as a main command.");
		try {
			final var commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			((CommandMap) commandMapField.get(Bukkit.getServer())).register(command.getLabel(), command.getName(), command);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	void registerMainCommands(@NotNull final JavaPlugin plugin) {
		new Reflections(ClasspathHelper.forPackage(plugin.getClass().getPackage().getName()),
				plugin.getClass(), new TypeAnnotationsScanner(), new SubTypesScanner())
				.getTypesAnnotatedWith(MainCommand.class)
				.forEach(clazz -> {
					if(!SimpleCommand.class.isAssignableFrom(clazz)) {
						throw new UnsupportedOperationException("The MainCommand annotation must be used on a class extending SimpleCommand.");
					}
					Arrays.stream(clazz.getDeclaredConstructors())
							.findFirst()
							.ifPresent(constructor -> {
								try {
									constructor.setAccessible(true);
									registerCommand(plugin, (Command) constructor.newInstance());
								} catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
									Formatter.error("Error accessing " + clazz.getName() + " command class. Make sure to use a no args constructor.");
								}
							});
				});
	}

	public static CommandManager getInstance() {
		return INSTANCE;
	}

}
