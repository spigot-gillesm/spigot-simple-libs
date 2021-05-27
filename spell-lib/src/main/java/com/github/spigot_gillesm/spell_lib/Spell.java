package com.github.spigot_gillesm.spell_lib;

import org.bukkit.entity.LivingEntity;

public interface Spell {

	/*
	 * . Active and passive spell
	 * . NextHitSpell, ProjectileSpell, BasicSpell
	 */

	void cast(LivingEntity entity);

	void run(LivingEntity entity);

}
