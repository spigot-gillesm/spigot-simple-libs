package com.github.spigot_gillesm.lib_test.command;

import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.menu.CraftingListMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

@MainCommand
public class CraftingCommand extends SimpleCommand {

	public CraftingCommand() {
		super("craft");
		setAliases(new ArrayList<>(Arrays.asList("c", "cr", "cft")));
		setPlayerCommand(true);
		setDescription("Displays the crafting menu");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if(args.length > 0) {
			Formatter.tell(sender, "&cThis command takes no arguments");
		} else {
			new CraftingListMenu().display((Player) sender);
		}
	}

}
