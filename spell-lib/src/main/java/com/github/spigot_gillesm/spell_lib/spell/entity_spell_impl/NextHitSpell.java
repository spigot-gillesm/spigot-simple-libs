package com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl;

import com.github.spigot_gillesm.spell_lib.spell.EntitySpell;
import com.github.spigot_gillesm.spell_lib.spell.SimpleSpell;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public abstract class NextHitSpell extends SimpleSpell implements EntitySpell {

	private static final String TAG = "NEXT_HIT_SPELL";

	@Override
	public void run(final LivingEntity entity) {
		EntitySpell.tagEntity(entity, this);
	}

	@Override
	public final void onHit(final Location location) { }

}
