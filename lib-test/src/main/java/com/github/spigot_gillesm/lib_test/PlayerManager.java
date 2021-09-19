package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.lib_test.profession.Profession;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import com.github.spigot_gillesm.rpg_stats_lib.StatManager;
import com.github.spigot_gillesm.rpg_stats_lib.Statistic;
import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerManager {
	public void setPlayerProfession(final Player player, final ProfessionType professionType) {
		ProfessionManager.getProfession(professionType).ifPresent(profession -> profession.join(player));
	}

	public void updatePlayerStats(final Player player) {
		applyPlayerStats(player);
	}

	private void applyPlayerStats(final Player player) {
		final var stats = StatManager.getHolder(player);
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stats.getValue(Statistic.HEALTH));
	}

	public void decrementLevel(final Player player) {
		player.setLevel(player.getLevel() - 1);
	}
	public Profession getProfession(final Player player) {
		final var type = getProfessionType(player);
		return type != null ? ProfessionManager.getProfession(type).orElse(null) : null;
	}

	public ProfessionType getProfessionType(final Player player) {
		final var type = DataManager.getData(player).getTagValue(PlayerTag.CLASS);
		return type != null ? ProfessionType.valueOf(type.toString()) : null;
	}

	public boolean hasProfession(final Player player) {
		return getProfession(player) != null;
	}

}
