package com.github.spigot_gillesm.epicworlds_utils;

import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class CooldownHolder {

	private static CooldownHolder instance;

	private final Set<Integer> cooldowns = new HashSet<>();

	public static CooldownHolder getInstance() {
		if(instance == null) {
			instance = new CooldownHolder();
		}

		return instance;
	}

	private CooldownHolder() { }

	public void startCooldown(final Entity entity, final int cooldown) {
		cooldowns.add(entity.getEntityId());
		EpicworldsUtils.getInstance().getServer().getScheduler().runTaskLater(EpicworldsUtils.getInstance(),
				() -> cooldowns.remove(entity.getEntityId()),
				cooldown * 20L);
	}

	public boolean isInCooldown(final Entity entity) {
		return cooldowns.contains(entity.getEntityId());
	}

}
