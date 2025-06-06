package com.github.spigot_gillesm.command_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SimpleCommand extends Command implements TabCompleter {

	@Setter(AccessLevel.PROTECTED)
	private boolean playerCommand = false;

	private final List<String> mandatoryArgs = new ArrayList<>();

	private final List<String> optionalArgs = new ArrayList<>();

	private final Set<SimpleCommand> subCommands = new HashSet<>();

	protected SimpleCommand(@NotNull String name) {
		super(name);
	}

	protected SimpleCommand(@NotNull SimpleCommand parentCommand, @NotNull String name) {
		this(name);

		parentCommand.subCommands.add(this);
	}

	protected abstract void run(CommandSender sender, String[] args);

	protected void tell(@NotNull CommandSender sender, @NotNull String message) {
		if(!Formatter.PREFIX.isBlank()) {
			Formatter.tell(sender, Formatter.PREFIX + " " + message);
		} else {
			Formatter.tell(sender, message);
		}
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if(playerCommand && !(sender instanceof Player)) {
			Formatter.tell(sender, "&cYou must be a player to run this command.");

			return false;
		}
		if(sender instanceof Player && getPermission() != null && !sender.hasPermission(getPermission())) {
			Formatter.tell(sender, "&cYou don't have the required permission to run this command.");

			return false;
		}
		//Check for help calls: no args when subcommands exist or "help" arg
		if((args.length == 1 && "help".equalsIgnoreCase(args[0])) || (args.length == 0 && !subCommands.isEmpty())) {
			displayHelp(sender);

			return true;
		}
		run(sender, args);

		if(args.length > 0) {
			runSubCommands(sender, commandLabel, args);
		}

		return true;
	}

	protected void displayHelp(@NotNull CommandSender sender) {
		Formatter.tell(sender, "&7=============&8[&6&lHelp&8]&7=============");
		Formatter.tell(sender, "&6Description&8: &7" + getDescription());

		if(!subCommands.isEmpty()) {
			Formatter.tell(sender, "");

			for(final SimpleCommand command : subCommands) {
				final var info = new StringBuilder("&7&l* &7/&6" + command.getName());

				for(final String arg : command.mandatoryArgs) {
					info.append(" &8<&7").append(arg).append("&8>");
				}
				for(final String arg : command.optionalArgs) {
					info.append(" &8[&7").append(arg).append("&8]");
				}
				info.append(" &8: &7").append(command.getDescription());
				Formatter.tell(sender, info.toString());
			}
			Formatter.tell(sender, "");
		}
	}

	private void runSubCommands(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if(subCommands.isEmpty()) {
			return;
		}
		final List<SimpleCommand> matchingCommands = subCommands.stream()
				//Get the commands matching the arg or alias
				.filter(command -> command.getName().equalsIgnoreCase(args[0]) || command.getAliases().contains(args[0]))
				.collect(Collectors.toList());

		//If the sub command doesn't match anything, display help
		if(matchingCommands.isEmpty()) {
			displayHelp(sender);

			return;
		}

		//Execute every available sub commands by feeding them their args
		matchingCommands.forEach(command -> command.execute(sender, commandLabel, Arrays.copyOfRange(args, 1, args.length)));
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(
				args[0],
				subCommands.stream().map(Command::getLabel).collect(Collectors.toList()),
				completions
		);
		Collections.sort(completions);

		return completions;
	}

	protected void addMandatoryArgument(@NotNull String arg) {
		mandatoryArgs.add(arg);
	}

	protected void addOptionalArgument(@NotNull String arg) {
		optionalArgs.add(arg);
	}

}
