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
		final var shooter = projectile.getShooter();
		final var spell = EntitySpell.getSpell(projectile);

		if(!(spell instanceof ProjectileSpell)) {
			return;
		}
		final var target = event.getHitEntity();

		if(target != null && shooter != null) {

			if(shooter instanceof LivingEntity) {
				if(target instanceof LivingEntity) {
					spell.onHit((LivingEntity) shooter, (LivingEntity) target);
				}
				spell.onHit((LivingEntity) shooter, target.getLocation());
			}

		} else {
			if(shooter instanceof LivingEntity) {
				spell.onHit((LivingEntity) shooter, event.getHitBlock().getLocation());
			}
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

		if(source instanceof LivingEntity && target instanceof LivingEntity) {
			spell.onHit((LivingEntity) source, (LivingEntity) target);
		}
	}

}
