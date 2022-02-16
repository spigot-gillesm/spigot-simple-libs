package com.github.spigot_gillesm.lib_test.craft;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CraftEntityMeta {

	@Getter
	private final int requiredLevel;

	/**
	 * The amount of profession level the craft yields
	 */
	@Getter
	private final int levelGain;

	/**
	 * The level at which the craft won't give extra profession levels
	 */
	@Getter
	private final int levelCap;

	/**
	 * Whether the recipe is known by default
	 */
	@Getter
	private final boolean knownByDefault;

	private CraftEntityMeta(final Builder builder) {
		this.requiredLevel = builder.requiredLevel;
		this.levelGain = builder.levelGain;
		this.levelCap = builder.levelCap;
		this.knownByDefault = builder.knownByDefault;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private int requiredLevel = 0;

		private int levelGain = 1;

		private int levelCap = requiredLevel + 10;

		private boolean knownByDefault = true;

		public Builder requiredLevel(final int requiredLevel) {
			this.requiredLevel = requiredLevel;
			return this;
		}

		public Builder levelGain(final int levelGain) {
			this.levelGain = levelGain;
			return this;
		}

		public Builder levelCap(final int levelCap) {
			this.levelCap = levelCap;
			return this;
		}

		public Builder knownByDefault(final boolean knownByDefault) {
			this.knownByDefault = knownByDefault;
			return this;
		}

		public CraftEntityMeta build() {
			return new CraftEntityMeta(this);
		}

	}

}
