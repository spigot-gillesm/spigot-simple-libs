package com.github.spigot_gillesm.spell_lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

public class SimpleParticle {

	public enum Shape {RANDOM}

	private final Shape shape;

	private final Particle particle;

	private final int amount;

	private final double offset;

	private final double data;

	private final Entity entity;

	private final long interval;

	private BukkitTask task;

	private SimpleParticle(final Builder builder) {
		this.shape = builder.shape;
		this.particle = builder.particle;
		this.amount = builder.amount;
		this.offset = builder.offset;
		this.data = builder.data;
		this.entity = builder.entity;
		this.interval = builder.interval;
	}

	/**
	 * Start the particle effect.
	 *
	 * @param entity the entity to attach the particle effect
	 */
	public void start(final Entity entity) {
		if(entity == null) {
			throw new IllegalArgumentException("Entity cannot be null.");
		}
		task = Bukkit.getServer().getScheduler().runTaskTimer(SpellLib.plugin,
				() -> {
					if(entity.isDead()) {
						stop();
					} else {
						final var location = entity.getLocation();

						if(shape == Shape.RANDOM) {
							location.getWorld().spawnParticle(particle, location, amount, offset, offset, offset, data);
						}
					}
				}, 0, interval);
	}

	/**
	 * Start the particle effect.
	 */
	public void start() {
		start(entity);
	}

	/**
	 * Stop the particle effect.
	 */
	public void stop() {
		task.cancel();
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Builder {

		private Shape shape = Shape.RANDOM;

		private Particle particle = Particle.FLAME;

		private int amount = 10;

		private double offset = 1;

		private double data = 1;

		private Entity entity;

		private long interval = 1;

		public SimpleParticle build() {
			return new SimpleParticle(this);
		}

		public Builder setShape(Shape shape) {
			this.shape = shape;
			return this;
		}

		public Builder setParticle(Particle particle) {
			this.particle = particle;
			return this;
		}

		public Builder setAmount(int amount) {
			this.amount = amount;
			return this;
		}

		public Builder setOffset(double offset) {
			this.offset = offset;
			return this;
		}

		public Builder setData(double data) {
			this.data = data;
			return this;
		}

		public Builder setEntity(Entity entity) {
			this.entity = entity;
			return this;
		}

		public Builder setInterval(long interval) {
			this.interval = interval;
			return this;
		}
	}

}
