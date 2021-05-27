package com.github.spigot_gillesm.spell_lib.spell;

import com.github.spigot_gillesm.spell_lib.SpellLib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

public interface EntitySpell {

	String TAG = "ENTITY_SPELL";

	void onHit(LivingEntity entity);

	default void onHit(final Location location) { }

	static void tagEntity(final Entity entity, final EntitySpell instance) {
		entity.setMetadata(TAG, new FixedMetadataValue(SpellLib.plugin, instance));
	}

	static EntitySpell getSpell(final Entity entity) {
		if(entity.hasMetadata(TAG)) {
			final Object o = entity.getMetadata(TAG).get(0).value();

			if(o instanceof EntitySpell) {
				return (EntitySpell) o;
			}
		}
		return null;
	}

}
