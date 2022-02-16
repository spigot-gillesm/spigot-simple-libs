package com.github.spigot_gillesm.rp_classes.talent;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.spell_lib.Spell;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Talent {

	@Getter
	private final Spell spell;

	@Getter
	private final int requiredPoints;

	private final Material icon;

	private final String displayName;

	private final List<String> description;

	private Talent(final Builder builder) {
		this.spell = builder.spell;
		this.requiredPoints = builder.requiredPoints;
		this.icon = builder.icon;
		this.displayName = builder.displayName;
		this.description = builder.description;
	}

	public ItemStack getIcon() {
		return SimpleItem.newBuilder()
				.material(icon)
				.displayName(displayName)
				.lore(description)
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.localizedName("CANCEL_USE")
				.build().getItemStack();
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Builder {

		private Spell spell;

		private int requiredPoints;

		private Material icon;

		private String displayName;

		private List<String> description;

		public Builder spell(final Spell spell) {
			this.spell = spell;
			return this;
		}

		public Builder requiredPoints(final int requiredPoints) {
			this.requiredPoints = requiredPoints;
			return this;
		}

		public Builder icon(final Material icon) {
			this.icon = icon;
			return this;
		}

		public Builder displayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder description(final List<String> description) {
			this.description = description;
			return this;
		}

		public Talent build() {
			return new Talent(this);
		}

	}

}
