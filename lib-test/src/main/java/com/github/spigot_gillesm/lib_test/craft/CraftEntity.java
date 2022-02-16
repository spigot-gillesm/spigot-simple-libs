package com.github.spigot_gillesm.lib_test.craft;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.DynamicCraft;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.profession.Profession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CraftEntity {

	@Getter
	protected final String id;

	@Getter
	protected final Profession profession;

	@Getter
	protected final ItemStack item;

	@Getter
	protected final int amount;

	@Getter
	protected final ItemStack reagent;

	@Getter
	protected final CraftEntityMeta metaData;

	@Getter
	protected final Class<? extends SimpleMenu> craftingMenu;

	@Getter
	protected final Class<? extends DynamicCraftMenu> dynamicCraftMenu;

	protected CraftEntity(final Builder<? extends Builder<?>> builder) {
		this.id = builder.id;
		this.profession = builder.profession;
		this.item = builder.item;
		this.amount = builder.amount;
		this.reagent = builder.reagent;
		this.metaData = builder.metaData;
		this.craftingMenu = builder.craftingMenu;
		this.dynamicCraftMenu = builder.dynamicCraftMenu;
	}

	/**
	 * Get the item(s) on craft completion.
	 *
	 * @return the craft result
	 */
	public abstract ItemStack getResult();

	/**
	 * Whether the craft has a dynamic craft or not.
	 *
	 * @return true if the craft has a dynamic craft
	 */
	public boolean isDynamicCraft() {
		return this instanceof DynamicCraft;
	}

	/**
	 * Whether the player can craft the item or not.
	 *
	 * @param player the player attempting the craft
	 * @return true if the player is able to craft the item
	 */
	public abstract boolean canCraft(Player player);

	/**
	 * Check if the given items match the pattern.
	 *
	 * @param items the items
	 * @return true if the items match the pattern
	 */
	public abstract boolean isPatternMatching(ItemStack[] items);

	/**
	 * Check if the item is similar to the craft's item ( item =/= result)
	 *
	 * @param itemStack the item
	 * @return true if the item is similar to the craft's item
	 */
	public abstract boolean isSimilar(ItemStack itemStack);

	@NoArgsConstructor
	protected abstract static class Builder<T extends Builder<T>> {

		protected String id;

		protected Profession profession;

		protected ItemStack item;

		protected int amount = 1;

		protected ItemStack reagent;

		protected CraftEntityMeta metaData;

		protected Class<? extends SimpleMenu> craftingMenu;

		protected Class<? extends DynamicCraftMenu> dynamicCraftMenu;

		//Used to avoid the Unchecked warning on the other methods
		@SuppressWarnings("unchecked")
		protected T thisObject() {
			return (T) this;
		}

		public T id(@NotNull final String id) {
			this.id = id;
			return thisObject();
		}

		public T profession(@NotNull final Profession profession) {
			this.profession = profession;
			return thisObject();
		}

		public T item(@NotNull final ItemStack item) {
			this.item = item;
			return thisObject();
		}

		public T amount(final int amount) {
			this.amount = amount;
			return thisObject();
		}

		public T reagent(final ItemStack reagent) {
			this.reagent = reagent;
			return thisObject();
		}

		public T reagent(final ItemStack reagent, final int amount) {
			final var copy = reagent.clone();
			copy.setAmount(amount);
			this.reagent = copy;
			return thisObject();
		}

		public T reagent(final Material reagent) {
			return reagent(reagent, 1);
		}

		public T reagent(final Material reagent, final int amount) {
			this.reagent = SimpleItem.newBuilder()
					.material(reagent)
					.amount(amount)
					.build()
					.getItemStack();
			return thisObject();
		}

		public T metaData(final CraftEntityMeta metaData) {
			this.metaData = metaData;
			return thisObject();
		}

		public T craftingMenu(final Class<? extends SimpleMenu> craftingMenu) {
			this.craftingMenu = craftingMenu;
			return thisObject();
		}

		public T dynamicCraftMenu(final Class<? extends DynamicCraftMenu> dynamicCraftMenu) {
			this.dynamicCraftMenu = dynamicCraftMenu;
			return thisObject();
		}

		public abstract CraftEntity build();

	}

}
