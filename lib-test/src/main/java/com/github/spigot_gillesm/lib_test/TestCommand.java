package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.command_lib.MainCommand;
import com.github.spigot_gillesm.command_lib.SimpleCommand;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.player_lib.PlayerManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import com.github.spigot_gillesm.spell_lib.spell.SimpleSpell;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MainCommand
public class TestCommand extends SimpleCommand {

	private static final SimpleSpell fireBall = new FireBall(8, 5, 2);

	public TestCommand() {
		super("test");
		setPlayerCommand(true);
		setDescription("Testing command");
	}

	@Override
	protected void run(final CommandSender commandSender, final String[] strings) {
		final var player = (Player) commandSender;
		final var data = PlayerManager.getData(player);
		data.setRawValue("NAME", player.getName());
		data.setTagValue(PlayerTag.CLASS, "Paladin");

		data.getMap().forEach((k, v) -> Formatter.tell(player, k + ": " + v.toString()));
	}

}
