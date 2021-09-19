package com.github.spigot_gillesm.lib_test.spell;

import com.github.spigot_gillesm.spell_lib.SimpleParticle;
import com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl.ProjectileSpell;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;

public class FireBall extends ProjectileSpell {

	private final double damage;

	private final double aoeDamage;

	private final double burnDuration;

	public FireBall(final double damage, final double aoeDamage, final int burnDuration) {
		this.damage = damage;
		this.aoeDamage = aoeDamage;
		this.burnDuration = burnDuration;

		setCooldown(10);
		setProjectileType(Arrow.class);
		setInvisible(true);
		setVectorMultiplier(1.2);
		setGravity(false);
		setMessage("&aCasting &cFireball&a!");
		addParticles(
				SimpleParticle.newBuilder()
						.setParticle(Particle.FLAME)
						.setAmount(25)
						.setOffset(0.25)
						.setData(0.025)
						.build(),
				SimpleParticle.newBuilder()
						.setParticle(Particle.DRIP_LAVA)
						.setAmount(15)
						.setOffset(0.15)
						.setData(1.5)
						.build()
		);
	}

	@Override
	public void onHit(final LivingEntity entity) {
		final var location = entity.getLocation();
		location.getWorld().createExplosion(location, 0);
		location.getWorld().spawnParticle(Particle.FLAME, location, 50, 0.15, 0.15, 0.15, 0.25);

		entity.damage(damage);
		entity.setFireTicks((int) burnDuration * 20);
		entity.getNearbyEntities(1.5, 1.5, 1.5).stream()
				.filter(LivingEntity.class::isInstance)
				.forEach(e -> ((LivingEntity) e).damage(aoeDamage));
	}
}
