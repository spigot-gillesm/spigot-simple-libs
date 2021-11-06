package com.github.spigot_gillesm.rp_classes.player;

import com.github.spigot_gillesm.rp_classes.rp_class.RpClass;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class PlayerManager {

	private final Set<PlayerClassData> REGISTERED_PLAYER_CLASS_DATA = new HashSet<>();

	private PlayerClassData registerNew(final UUID uuid) {
		final var data = new PlayerClassData(uuid);
		REGISTERED_PLAYER_CLASS_DATA.add(data);

		return data;
	}

	@NotNull
	public PlayerClassData getPlayerClassData(final Player player) {
		return REGISTERED_PLAYER_CLASS_DATA.stream()
				.filter(d -> d.getUuid().equals(player.getUniqueId()))
				.findFirst()
				.orElseGet(() -> registerNew(player.getUniqueId()));
	}

	public void setPlayerClass(final Player player, final RpClass rpClass) {
		getPlayerClassData(player).setClass(rpClass);
	}

	public int getSpellRank(final Player player, final String id) {
		//TODO
		return 1;
	}

}
