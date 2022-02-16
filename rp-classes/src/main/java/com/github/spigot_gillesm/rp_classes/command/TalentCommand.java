package com.github.spigot_gillesm.rp_classes.command;

import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

@MainCommand
public class TalentCommand extends SimpleCommand {

	public TalentCommand() {
		super("talent");

		setAliases(new ArrayList<>(Arrays.asList("t", "tlt")));
		setPlayerCommand(true);
		setDescription("Talent command");
		setPermission("talent");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if(args.length == 0) {
			//new ClassesMenu().display((Player) sender);
		} else {
			super.displayHelp(sender);
		}
	}

}
