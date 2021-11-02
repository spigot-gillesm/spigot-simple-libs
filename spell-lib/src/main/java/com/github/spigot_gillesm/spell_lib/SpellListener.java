package com.github.spigot_gillesm.spell_lib;

import com.github.spigot_gillesm.spell_lib.spell.EntitySpell;
import com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl.NextHitSpell;
import com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl.ProjectileSpell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SpellListener implements Listener {

	@EventHandler
	private void onProjectileHit(final ProjectileHitEvent event) {
		final var projectile = event.getEntity();
		final var spell = EntitySpell.getSpell(projectile);

		if(!(spell instanceof ProjectileSpell)) {
			return;
		}
		final var target = event.getHitEntity();

		if(target != null) {

			if(target instanceof LivingEntity) {
				spell.onHit((LivingEntity) target);
			}
			spell.onHit(target.getLocation());

		} else {
			spell.onHit(event.getHitBlock().getLocation());
		}
		projectile.remove();
	}

	@EventHandler
	private void onEntityDamageEntity(final EntityDamageByEntityEvent event) {
		final var source = event.getDamager();
		final var target = event.getEntity();
		final var spell = EntitySpell.getSpell(source);

		if(!(spell instanceof NextHitSpell)) {
			return;
		}

		if(target instanceof LivingEntity) {
			spell.onHit((LivingEntity) target);
		}
	}

}
