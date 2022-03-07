package com.github.spigot_gillesm.lib_test.craft.craft_entity;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.player.PlayerManager;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class CraftRecipe extends CraftEntity {

	@Getter
	private final ItemStack[] pattern;

	protected CraftRecipe(@NotNull final Builder builder) {
		super(builder);

		this.pattern = fillPattern(builder.pattern);
	}

	private ItemStack[] fillPattern(final ItemStack[] items) {
		final var clone = items.clone();
		Arrays.stream(clone).filter(Objects::isNull).forEach(i -> i = new ItemStack(Material.AIR));
		return clone;
	}

	@Override
	public ItemStack getResult() {
		final var result = item.clone();
		result.setAmount(amount);
		return result;
	}

	@Override
	public boolean canCraft(@NotNull final Player player) {
		final var playerProfession = PlayerManager.getProfession(player);

		if(playerProfession.isPresent()) {
			if(craftingMenu != null) {
				final var menu = SimpleMenu.getMenu(player);

				//Check if the right menu is used
				if(menu != null && menu.getClass().equals(craftingMenu)) {
					return profession.equals(playerProfession.get());
				} else {
					return false;
				}
			} else {
				return profession.equals(playerProfession.get());
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean isPatternMatching(final ItemStack[] items) {
		return Arrays.equals(pattern, items);
	}

	@Override
	public boolean isSimilar(final ItemStack itemStack) {
		return item.isSimilar(itemStack);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder extends CraftEntity.Builder<Builder> {

		private final ItemStack[] pattern = new ItemStack[9];

		public Builder setPatternItem(final int position, @NotNull final ItemStack item) {
			if(position < 0 || position > 8) {
				throw new IllegalArgumentException("Position must be between 0 and 8.");
			}
			pattern[position] = item;
			return this;
		}

		public Builder setPatternItem(final int position, @NotNull final Material material) {
			return setPatternItem(position, new ItemStack(material));
		}

		@Override
		public CraftRecipe build() {
			if(profession == null) {
				throw new IllegalArgumentException("Profession cannot be null.");
			}
			if(item == null) {
				throw new IllegalArgumentException("Item cannot be null.");
			}
			if(amount < 1) {
				throw new IllegalArgumentException("Amount cannot be smaller than 1.");
			}
			return new CraftRecipe(this);
		}

	}

}
