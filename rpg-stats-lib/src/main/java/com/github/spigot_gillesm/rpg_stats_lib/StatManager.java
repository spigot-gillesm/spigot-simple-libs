package com.github.spigot_gillesm.rpg_stats_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class StatManager {

	private final Set<StatHolder> HOLDERS = new HashSet<>();

	public StatHolder newHolder(final UUID uuid) {
		final var holder = new StatHolder(uuid);
		HOLDERS.add(holder);
		return holder;
	}

	public StatHolder getHolder(@NotNull final UUID uuid) {
		return HOLDERS.stream()
				.filter(h -> h.getUuid().equals(uuid))
				.findFirst()
				.orElse(newHolder(uuid));
	}

	public StatHolder getHolder(@NotNull final Player player) {
		return getHolder(player.getUniqueId());
	}

}
