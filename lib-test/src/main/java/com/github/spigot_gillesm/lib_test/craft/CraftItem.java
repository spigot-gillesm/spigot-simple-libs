package com.github.spigot_gillesm.lib_test.craft;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class CraftItem {

	@Getter
	private final ProfessionType professionType;

	@Getter
	private final Class<? extends SimpleMenu> craftingMenu;

	@Getter
	private final ItemStack item;

	@Getter
	private final int amount;

	@Getter
	private final ItemStack[] pattern;

	@Getter
	private final ItemStack reagent;

	private CraftItem(final Builder builder) {
		this.professionType = builder.professionType;
		this.craftingMenu = builder.craftingMenu;
		this.item = builder.item;
		this.amount = builder.amount;
		this.pattern = fillPattern(builder.pattern);
		this.reagent = builder.reagent;
	}

	private ItemStack[] fillPattern(final ItemStack[] items) {
		Arrays.stream(items).filter(Objects::isNull).forEach(i -> i = new ItemStack(Material.AIR));
		return items;
	}

	public ItemStack getResult() {
		final var result = item.clone();
		result.setAmount(amount);
		return result;
	}

	public boolean isPatternMatching(final ItemStack[] items) {
		return Arrays.equals(pattern, items);
	}

	public boolean canCraft(final Player player) {
		if(craftingMenu != null) {
			final var menu = SimpleMenu.getMenu(player);

			if(menu != null && menu.getClass().equals(craftingMenu)) {
				return professionType == PlayerManager.getProfession(player);
			} else {
				return false;
			}
		} else {
			return professionType == PlayerManager.getProfession(player);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private ProfessionType professionType;

		private Class<? extends SimpleMenu> craftingMenu;

		private ItemStack item;

		private int amount = 1;

		private final ItemStack[] pattern = new ItemStack[9];

		private ItemStack reagent;

		public Builder professionType(@NotNull final ProfessionType professionType) {
			this.professionType = professionType;
			return this;
		}

		public Builder craftingMenu(final Class<? extends SimpleMenu> craftingMenu) {
			this.craftingMenu = craftingMenu;
			return this;
		}

		public Builder item(@NotNull final ItemStack item) {
			this.item = item;
			return this;
		}

		public Builder amount(final int amount) {
			this.amount = amount;
			return this;
		}

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

		public Builder reagent(final ItemStack reagent) {
			this.reagent = reagent;
			return this;
		}

		public Builder reagent(final Material reagent, final int amount) {
			this.reagent = SimpleItem.newBuilder()
					.material(reagent)
					.amount(amount)
					.build()
					.getItemStack();
			return this;
		}

		public CraftItem build() {
			if(professionType == null) {
				throw new IllegalArgumentException("Profession type cannot be null.");
			}
			if(amount < 1) {
				throw new IllegalArgumentException("Amount cannot be smaller than 1.");
			}
			return new CraftItem(this);
		}

	}

}
