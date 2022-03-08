package com.github.spigot_gillesm.lib_test.pattern;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.player.PlayerManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a pattern whose goal is to teach new recipes to players.
 */
public class RecipePattern {

	private final String craftId;

	@Getter
	private final ItemStack physicalPattern;

	@Getter
	private final boolean consumedOnUse;

	private RecipePattern(final Builder builder) {
		this.craftId = builder.craftId;
		this.physicalPattern = builder.physicalPattern;
		this.consumedOnUse = builder.consumedOnUse;
	}

	/**
	 * Teach its recipe to the player if the player meets the requirements.
	 *
	 * @param player the player
	 * @return true if the recipe has been taught
	 */
	public boolean teach(final Player player) {
		final var craftEntity = CraftManager.getCraftItem(craftId);

		if(craftEntity.isPresent() && willTeach(player, craftEntity.get())) {
			PlayerManager.teachRecipe(player, craftEntity.get());
			Formatter.tell(player, "&aYou've learned to craft " +
					craftEntity.get().getItem().getItemMeta().getDisplayName() + "&a!");

			return true;
		}

		return false;
	}

	private boolean willTeach(final Player player, final CraftEntity craftEntity) {
		if(!PlayerManager.hasRequiredProfessionLevel(player, craftEntity)) {
			Formatter.tell(player, "&cYou don't have the required profession!");
			return false;
		}
		if(PlayerManager.knowCraft(player, craftEntity)) {
			Formatter.tell(player, "&cYou already know this recipe!");
			return false;
		}
		if(!PlayerManager.hasRequiredProfessionLevel(player, craftEntity)) {
			Formatter.tell(player, "&cYou don't have the required level to learn that recipe!");
			return false;
		}

		return true;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private String craftId;

		private ItemStack physicalPattern;

		private boolean consumedOnUse = true;

		public Builder craftId(final String craftId) {
			this.craftId = craftId;
			return this;
		}

		public Builder physicalPattern(final SimpleItem physicalPattern) {
			this.physicalPattern = physicalPattern.make().getItemStack();
			return this;
		}

		public Builder consumeOnUse(final boolean consumedOnUse) {
			this.consumedOnUse = consumedOnUse;
			return this;
		}

		public RecipePattern build() {
			return new RecipePattern(this);
		}

	}

}
