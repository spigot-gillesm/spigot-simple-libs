package com.github.spigot_gillesm.lib_test.command;

import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.menu.ProfessionMenu;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@MainCommand
public class ProfessionCommand extends SimpleCommand {

	public ProfessionCommand() {
		super("profession");
		setAliases(new ArrayList<>(Arrays.asList("p", "pr", "profess")));
		setPlayerCommand(true);
		setDescription("Profession command");
		setPermission("profession");

		new GetCommand(this);
		new SetCommand(this);
	}

	@Override
	protected void run(final CommandSender commandSender, final String[] args) {
		if(args.length == 0) {
			new ProfessionMenu().display((Player) commandSender);
		}
	}

	private static class GetCommand extends SimpleCommand {

		protected GetCommand(final SimpleCommand parentCommand) {
			super(parentCommand, "get");
			setAliases(new ArrayList<>(Collections.singletonList("g")));
			setPlayerCommand(true);
			setDescription("Get items from the plugin");
			setPermission("profession.get");
			addMandatoryArgument("item id");
			addOptionalArgument("amount");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			//TODO: Integrate these tests in the upper class
			if(args.length < 1) {
				Formatter.tell(sender, "&cThis command takes at least 1 argument");
				return;
			}
			if(args.length > 2) {
				Formatter.tell(sender, "&cThis command takes at most 2 arguments");
				return;
			}
			final var item = CraftManager.getCraftItem(args[0]);

			if(item == null) {
				Formatter.tell(sender, "&cUnknown item");
				return;
			}
			if(args.length > 1 && PluginUtil.isInt(args[1])) {
				final var amount = Integer.parseInt(args[1]);
				if(amount > 0 && amount <= 64) {
					final var stack = item.getItem().clone();
					stack.setAmount(amount);
					((Player) sender).getInventory().addItem(stack);
				} else {
					Formatter.tell(sender, "&cItem amount must be a positive integer between 1 and 64");
				}
			} else {
				Formatter.tell(sender, "&cItem amount must be a positive integer between 1 and 64");
			}
		}
	}

	private static class SetCommand extends SimpleCommand {

		protected SetCommand(final SimpleCommand parent) {
			super(parent, "set");
			setAliases(new ArrayList<>(Collections.singletonList("s")));
			setPlayerCommand(false);
			setDescription("Set the player's profession");
			setPermission("profession.set");
			addMandatoryArgument("player name");
			addMandatoryArgument("profession id");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length != 2) {
				Formatter.tell(sender, "&cThis command takes 2 arguments");
				return;
			}
			final var player = PluginUtil.getPlayer(args[0]);
			final var profession = ProfessionType.getProfessionType(args[1]);

			if(player == null) {
				Formatter.tell(sender, "&cUnknown or offline player");
				return;
			}
			if(profession == null) {
				Formatter.tell(sender, "&cUnknown profession type");
				return;
			}
			PlayerManager.setPlayerProfession(player, profession);
			Formatter.tell(sender, "&9" + player.getName() + " &ais now &9" + profession.name());
			Formatter.tell(player, "&aYou're now &9" + profession.name() + "&a!");
		}

	}

}
