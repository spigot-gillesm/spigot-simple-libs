package com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl;

import com.github.spigot_gillesm.spell_lib.SimpleParticle;
import com.github.spigot_gillesm.spell_lib.SpellLib;
import com.github.spigot_gillesm.spell_lib.spell.EntitySpell;
import com.github.spigot_gillesm.spell_lib.spell.SimpleSpell;
import com.github.spigot_gillesm.spell_lib.util.PacketUtil;
import lombok.AccessLevel;
import lombok.Setter;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArrow;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ProjectileSpell extends SimpleSpell implements EntitySpell {

	@Setter(AccessLevel.PROTECTED)
	private Class<? extends Projectile> projectileType = Arrow.class;

	@Setter(AccessLevel.PROTECTED)
	private double vectorMultiplier = 2;

	@Setter(AccessLevel.PROTECTED)
	private double projectileDamage = 0;

	@Setter(AccessLevel.PROTECTED)
	private boolean invisible = false;

	@Setter(AccessLevel.PROTECTED)
	private boolean gravity = true;

	private final List<SimpleParticle> particles = new ArrayList<>();

	protected ProjectileSpell() {
		super();
	}

	@Override
	public void run(final LivingEntity entity) {
		final Projectile projectile = entity.launchProjectile(projectileType,
				entity.getLocation().getDirection().multiply(vectorMultiplier));
		projectile.setGravity(gravity);

		if(invisible) {
			makeInvisible(projectile);
		}
		EntitySpell.tagEntity(projectile, this);

		if(projectile instanceof AbstractArrow) {
			((AbstractArrow) projectile).setDamage(projectileDamage);
		}
		particles.forEach(particle -> particle.start(projectile));
		//Remove the projectile after 15 secs to avoid overloading
		Bukkit.getServer().getScheduler().runTaskLater(SpellLib.plugin,
				() -> {
					if(!projectile.isDead()) {
						projectile.remove();
					}
				},
				15 * 20L);
	}

	/**
	 * Make a projectile invisible.
	 *
	 * @param projectile the projectile to make invisible
	 */
	private void makeInvisible(final Projectile projectile) {
		projectile.getNearbyEntities(25, 25, 25).stream()
				.filter(Player.class::isInstance)
				.forEach(player -> PacketUtil.sendPacket((Player) player,
						new PacketPlayOutEntityDestroy( ((CraftArrow) projectile).getHandle().getId() )));
	}

	public void addParticles(final List<SimpleParticle> particles) {
		this.particles.addAll(particles);
	}

	public void addParticles(final SimpleParticle... particles) {
		addParticles(Arrays.asList(particles));
	}

}
