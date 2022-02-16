package com.github.spigot_gillesm.rp_classes.command;

import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.rp_classes.PluginUtil;
import com.github.spigot_gillesm.rp_classes.menu.ClassesMenu;
import com.github.spigot_gillesm.rp_classes.player.PlayerManager;
import com.github.spigot_gillesm.rp_classes.rp_class.ClassManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@MainCommand
public class ClassCommand extends SimpleCommand {

	public ClassCommand() {
		super("class");

		setAliases(new ArrayList<>(Arrays.asList("c", "cl")));
		setPlayerCommand(false);
		setDescription("Class command");
		setPermission("class");

		//new ReloadCommand(this);
		new SetClassCommand(this);
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if(args.length == 0) {
			new ClassesMenu().display((Player) sender);
		} else {
			super.displayHelp(sender);
		}
	}

	public static class SetClassCommand extends SimpleCommand {

		private SetClassCommand(final SimpleCommand parent) {
			super(parent, "set");

			setAliases(new ArrayList<>(Collections.singleton("s")));
			setPlayerCommand(true);
			setDescription("Set the player's class");
			setPermission("class.set");
			addMandatoryArgument("player name");
			addMandatoryArgument("class id");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length != 2) {
				tell(sender, "&cThis command takes 2 arguments");
				return;
			}
			final var player = PluginUtil.getPlayer(args[0]);
			final var rpClass = ClassManager.getRpClass(args[1]);

			if(player == null) {
				tell(sender, "&cUnknown or offline player");
				return;
			}
			rpClass.ifPresentOrElse(c -> {
					tell(sender, "&9" + args[0] + " &ais now a " + c.getDisplayName());
					Formatter.tell(player, "&aYou're now a " + c.getDisplayName() + "&a!");
					PlayerManager.setPlayerClass(player, c);
			},
					() -> tell(sender, "&cUnknown class"));
		}

	}

}
