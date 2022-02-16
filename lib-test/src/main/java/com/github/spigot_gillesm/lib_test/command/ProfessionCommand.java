package com.github.spigot_gillesm.lib_test.command;

import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.lib_test.ItemManager;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.menu.ProfessionMenu;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.lib_test.profession.WorkstationManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@MainCommand
public class ProfessionCommand extends SimpleCommand {

	public ProfessionCommand() {
		super("rpprofession");
		setAliases(new ArrayList<>(Arrays.asList("rpp", "rpprof", "rpprofess")));
		setPlayerCommand(false);
		setDescription("Profession command");
		setPermission("profession");

		new ReloadCommand(this);
		new GetCommand(this);
		new TeachCommand(this);
		new SetCommand(this);
	}

	@Override
	protected void run(final CommandSender commandSender, final String[] args) {
		if(args.length == 0) {
			new ProfessionMenu().display((Player) commandSender);
		}
	}

	private static class ReloadCommand extends SimpleCommand {

		private ReloadCommand(final SimpleCommand parentCommand) {
			super(parentCommand, "reload");
			setAliases(new ArrayList<>(Collections.singletonList("r")));
			setPlayerCommand(false);
			setDescription("Reload the items and crafts from files");
			setPermission("profession.reload");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length > 0) {
				tell(sender, "&cThis command takes no arguments");
			} else {
				tell(sender, "&aReloading...");
				//Workstations must be loaded before professions and crafts
				//Items must be loaded before crafts
				WorkstationManager.loadWorkstations();
				ProfessionManager.loadProfessions();
				ItemManager.loadItems();
				CraftManager.loadCrafts();
				tell(sender, "&aDone!");
			}
		}

	}

	private static class GetCommand extends SimpleCommand {

		private GetCommand(final SimpleCommand parentCommand) {
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
				tell(sender, "&cThis command takes at least 1 argument");
				return;
			}
			if(args.length > 2) {
				tell(sender, "&cThis command takes at most 2 arguments");
				return;
			}
			final var item = ItemManager.getItem(args[0]);

			if(item.isPresent()) {
				var amount = 1;
				if(args.length > 1) {
					if(PluginUtil.isInt(args[1])) {
						amount = PluginUtil.clamp(Integer.parseInt(args[1]), 0, 64);
					} else {
						tell(sender, "&cItem amount must be a positive integer between 1 and 64");
						return;
					}
				}
				final var stack = item.get().getItemStack().clone();
				stack.setAmount(amount);
				((Player) sender).getInventory().addItem(stack);
			} else {
				tell(sender, "&cUnknown item");
			}
		}
	}

	private static class TeachCommand extends SimpleCommand {

		private TeachCommand(final SimpleCommand parent) {
			super(parent, "teach");
			setAliases(Collections.singletonList("t"));
			setPlayerCommand(false);
			setDescription("Teach the player the specified recipe");
			setPermission("rpprofession.teach");
			addMandatoryArgument("player name");
			addMandatoryArgument("recipe id");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length != 2) {
				tell(sender, "&cThis command takes 2 arguments");
				return;
			}
			final var player = PluginUtil.getPlayer(args[0]);
			final var recipe = CraftManager.getCraftItem(args[1]);
			recipe.ifPresentOrElse(r -> {
						PlayerManager.teachRecipe(player, r);
						tell(sender, "&9" + player.getName() + " &ahas learned the recipe &9" + r.getId());
						tell(player, "&aYou've learned to craft " + r.getItem().getItemMeta().getDisplayName() + "&a!");
					},
					() -> tell(sender, "&cThis recipe doesn't exist")
			);
		}

	}

	private static class SetCommand extends SimpleCommand {

		private SetCommand(final SimpleCommand parent) {
			super(parent, "set");
			setAliases(new ArrayList<>(Collections.singletonList("s")));
			setPlayerCommand(false);
			setDescription("Set players profession data");
			setPermission("profession.set");

			new SetPlayerProfessionCommand(this);
			new SetPlayerProfessionLevelCommand(this);
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length == 0) {
				displayHelp(sender);
			}
		}
	}

	private static class SetPlayerProfessionCommand extends SimpleCommand {

		private SetPlayerProfessionCommand(final SimpleCommand parent) {
			super(parent, "profession");
			setAliases(new ArrayList<>(Arrays.asList("p", "prof", "profess")));
			setPlayerCommand(false);
			setDescription("Set the player's profession");
			setPermission("profession.set.profession");
			addMandatoryArgument("player name");
			addMandatoryArgument("profession id");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length != 2) {
				tell(sender, "&cThis command takes 2 arguments");
				return;
			}
			final var player = PluginUtil.getPlayer(args[0]);
			final var profession = ProfessionManager.getProfession(args[1]);

			if(player == null) {
				tell(sender, "&cUnknown or offline player");
				return;
			}
			if(profession.isEmpty()) {
				tell(sender, "&cUnknown profession type");
				return;
			}
			PlayerManager.setPlayerProfession(player, args[1]);
			final var newProfession = PlayerManager.getProfession(player);
			tell(sender, "&9" + player.getName() + " &ais now &9" + newProfession.get().getDisplayName());
			tell(player, "&aYou're now &9" + newProfession.get().getDisplayName() + "&a!");
		}

	}

	private static class SetPlayerProfessionLevelCommand extends SimpleCommand {

		private SetPlayerProfessionLevelCommand(final SimpleCommand parent) {
			super(parent, "level");
			setAliases(new ArrayList<>(Collections.singletonList("s")));
			setPlayerCommand(false);
			setDescription("Set the player's profession level");
			setPermission("profession.set.level");
			addMandatoryArgument("player name");
			addMandatoryArgument("level");
		}

		@Override
		protected void run(final CommandSender sender, final String[] args) {
			if(args.length != 2) {
				tell(sender, "&cThis command takes 2 arguments");
				return;
			}
			if(!PluginUtil.isInt(args[1])) {
				tell(sender, "&cThe level must be an integer greater than 0");
			}
			final var player = PluginUtil.getPlayer(args[0]);
			final var level = Integer.parseInt(args[1]);

			if(player == null) {
				tell(sender, "&cUnknown or offline player");
				return;
			}
			if(level < 0) {
				tell(sender, "&cThe level must be an integer greater than 0");
				return;
			}
			PlayerManager.setProfessionLevel(player, level);
			tell(sender, "&9" + player.getName() + " &ahas now its profession set to level &9" + level + "&a!");
			tell(player, "&aYou profession level has been set to &9" + level + "&a!");
		}

	}

}
