package com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl;

import com.github.spigot_gillesm.spell_lib.spell.EntitySpell;
import com.github.spigot_gillesm.spell_lib.spell.SimpleSpell;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class NextHitSpell extends SimpleSpell implements EntitySpell {

	private static final String TAG = "NEXT_HIT_SPELL";

	@Override
	public void run(@NotNull final LivingEntity entity) {
		EntitySpell.tagEntity(entity, this);
	}

}
