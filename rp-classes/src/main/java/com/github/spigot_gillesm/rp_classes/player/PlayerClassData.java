package com.github.spigot_gillesm.rp_classes.player;

import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerData;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import com.github.spigot_gillesm.rpg_stats_lib.StatHolder;
import com.github.spigot_gillesm.rpg_stats_lib.StatManager;
import com.github.spigot_gillesm.rp_classes.rp_class.ClassManager;
import com.github.spigot_gillesm.rp_classes.rp_class.RpClass;
import lombok.Getter;

import java.util.UUID;

public class PlayerClassData {

	@Getter
	private final UUID uuid;

	PlayerClassData(final UUID uuid) {
		this.uuid = uuid;
	}

	public void setClass(final RpClass rpClass) {
		getPlayerData().setTagValue(PlayerTag.CLASS, ClassManager.getId(rpClass).toUpperCase());
	}

	public PlayerData getPlayerData() {
		return DataManager.getData(uuid);
	}

	public StatHolder getStatHolder() {
		return StatManager.getHolder(uuid);
	}

}
