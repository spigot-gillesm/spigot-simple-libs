package com.github.spigot_gillesm.spell_lib;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class SpellCooldown {

	private final Map<String, Long> idInCooldown = new HashMap<>();

	private final Map<String, BukkitTask> schedulers = new HashMap<>();

	/**
	 * Start the cooldown for the specified id.
	 *
	 * @param id the id of the entity to put in cooldown
	 * @param time the time before the cooldown is off in seconds
	 */
	public void start(@NotNull final String id, final double time) {
		//Put the id linked to the starting moment of the cooldown
		idInCooldown.put(id, System.currentTimeMillis());
		//Put the id linked to its offCooldown scheduler
		schedulers.put(id, Bukkit.getServer().getScheduler().runTaskLater(
				SpellLib.plugin, () -> idInCooldown.remove(id), (long) time * 20));
	}

	public void stop(@NotNull final String id) {
		idInCooldown.remove(id);
		schedulers.remove(id);
	}

	public boolean isOnCooldown(@NotNull final String id) {
		return idInCooldown.containsKey(id);
	}

	public BigDecimal remainingTime(@NotNull final String id, double originalCooldown) {
		if(isOnCooldown(id)) {
			//delta of the original cooldown and the time spent since the cooldown started
			return BigDecimal.valueOf(originalCooldown - (System.currentTimeMillis() - idInCooldown.get(id)) / 1000.0);
		} else {
			return BigDecimal.valueOf(0);
		}
	}

}
