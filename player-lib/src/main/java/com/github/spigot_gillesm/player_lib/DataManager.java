package com.github.spigot_gillesm.player_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class DataManager {

	private final Set<PlayerData> REGISTERED_PLAYER_DATA = new HashSet<>();

	private PlayerData registerNew(@NotNull final UUID uuid) {
		final var data = new PlayerData(uuid);
		REGISTERED_PLAYER_DATA.add(data);
		return data;
	}

	public PlayerData getData(@NotNull final UUID uuid) {
		return REGISTERED_PLAYER_DATA.stream()
				.filter(d -> d.getUuid().equals(uuid))
				.findFirst()
				.orElseGet(() -> registerNew(uuid));
	}

	public PlayerData getData(@NotNull final Player player) {
		return getData(player.getUniqueId());
	}

}
