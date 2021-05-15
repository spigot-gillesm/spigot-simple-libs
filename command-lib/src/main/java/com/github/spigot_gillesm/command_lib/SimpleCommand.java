package com.github.spigot_gillesm.command_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SimpleCommand extends Command {

	@Setter(AccessLevel.PROTECTED)
	private boolean playerCommand = false;

	@Setter(AccessLevel.PROTECTED)
	private String alias;

	private final Set<SimpleCommand> subCommands = new HashSet<>();

	public SimpleCommand(final String name) {
		super(name);
	}

	public SimpleCommand(final SimpleCommand parentCommand, final String name) {
		this(name);
		parentCommand.subCommands.add(this);
	}

	protected abstract void run(final CommandSender sender, final String[] args);

	@Override
	public boolean execute(@NotNull final CommandSender sender, @NotNull final String commandLabel,
						   @NotNull final String[] args) {
		if(playerCommand && !(sender instanceof Player)) {
			Formatter.tell(sender, "&cYou must be a player to run this command.");
			return false;
		}
		if(sender instanceof Player && getPermission() != null && !sender.hasPermission(getPermission())) {
			Formatter.tell(sender, "&cYou don't have the required permission to run this command.");
			return false;
		}

		run(sender, args);
		//Check for help calls
		if(args.length == 1 && "help".equalsIgnoreCase(args[0])) {
			displayHelp(sender);
		}
		//True if args match no sub commands name
		if(args.length > 0 && runSubCommands(sender, commandLabel, args)) {
			displayHelp(sender);
		}

		return true;
	}

	private boolean runSubCommands(final CommandSender sender, final String commandLabel, final String[] args) {
		final List<SimpleCommand> matchingCommands = subCommands.stream()
				//Get the commands matching the arg or alias
				.filter(command -> command.getName().equalsIgnoreCase(args[0]) || args[0].equalsIgnoreCase(command.alias))
				.collect(Collectors.toList());
		//Execute every available sub commands by feeding them their args
		matchingCommands.forEach(command -> command.execute(sender, commandLabel,
				Arrays.copyOfRange(args, 1, args.length)));

		//Return true if no command was found
		return matchingCommands.isEmpty();
	}

	private void displayHelp(final CommandSender sender) {
		Formatter.tell(sender, "&7=============&8[&6&lHelp&8]&7=============");
		Formatter.tell(sender, "");
		subCommands.forEach(command -> Formatter.tell(sender,
				"&7&l* &7/&6" + command.getName() + " &8: &7" + command.getDescription()));
		Formatter.tell(sender, "");
	}

}
