package com.github.spigot_gillesm.epicworlds_utils.villager;

import lombok.Getter;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public class VillagerTrade {

	@Getter
	private final Villager.Profession profession;

	@Getter
	private final int level;

	@Getter
	private final int chance;

	@Getter
	private final int maxUse;

	@Getter
	private final ItemStack result;

	@Getter
	private final ItemStack firstIngredient;

	@Getter
	private final ItemStack secondIngredient;

	private VillagerTrade(final Builder builder) {
		this.profession = builder.profession;
		this.level = builder.level;
		this.chance = builder.chance;
		this.maxUse = builder.maxUse;
		this.result = builder.result;
		this.firstIngredient = builder.firstIngredient;
		this.secondIngredient = builder.secondIngredient;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private Villager.Profession profession;

		private int level = 1;

		private int chance = 1;

		private int maxUse = -1;

		private ItemStack result;

		private ItemStack firstIngredient;

		private ItemStack secondIngredient;

		private Builder() { }

		public Builder profession(final Villager.Profession profession) {
			this.profession = profession;
			return this;
		}

		public Builder level(final int level) {
			this.level = level;
			return this;
		}

		public Builder chance(final int chance) {
			this.chance = chance;
			return this;
		}

		public Builder maxUse(final int maxUse) {
			this.maxUse = maxUse;
			return this;
		}

		public Builder result(final ItemStack result) {
			this.result = result;
			return this;
		}

		public Builder firstIngredient(final ItemStack firstIngredient) {
			this.firstIngredient = firstIngredient;
			return this;
		}

		public Builder secondIngredient(final ItemStack secondIngredient) {
			this.secondIngredient = secondIngredient;
			return this;
		}

		public VillagerTrade build() {
			return new VillagerTrade(this);
		}

	}

}
