package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import com.github.spigot_gillesm.rpg_stats_lib.StatManager;
import com.github.spigot_gillesm.rpg_stats_lib.Statistic;
import lombok.Setter;
import org.bukkit.entity.Player;

public class Profession {

	private final ProfessionType type;

	@Setter
	private double baseHealth = 20;

	@Setter
	private double baseResource = 20;

	public Profession(final ProfessionType type) {
		this.type = type;
	}

	private void setPlayerStats(final Player player) {
		final var stats = StatManager.getHolder(player);
		stats.setStat(Statistic.HEALTH, baseHealth);
		stats.setStat(Statistic.MANA, baseResource);
	}

	public void join(final Player player) {
		DataManager.getData(player).setTagValue(PlayerTag.CLASS, type);
		setPlayerStats(player);
		PlayerManager.updatePlayerStats(player);
	}

}
