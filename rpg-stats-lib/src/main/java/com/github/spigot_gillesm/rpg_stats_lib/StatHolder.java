package com.github.spigot_gillesm.rpg_stats_lib;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class StatHolder {

	@Getter
	private final UUID uuid;

	private final Map<Statistic, Double> playerStats = new EnumMap<>(Statistic.class);

	StatHolder(final UUID uuid) {
		this.uuid = uuid;
	}

	public Double setStat(@NotNull final Statistic statistic, @NotNull final Double value) {
		playerStats.put(statistic, value);
		return value;
	}

	public Double add(@NotNull final Statistic statistic, @NotNull final Double value) {
		playerStats.put(statistic, playerStats.get(statistic) + value);
		return playerStats.get(statistic);
	}

	public Double subtract(@NotNull final Statistic statistic, @NotNull final Double value) {
		playerStats.put(statistic, playerStats.get(statistic) - value);
		return playerStats.get(statistic);
	}

	public Double getValue(@NotNull final Statistic statistic) {
		return playerStats.get(statistic);
	}

}
