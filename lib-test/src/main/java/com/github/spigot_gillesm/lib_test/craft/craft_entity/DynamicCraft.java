package com.github.spigot_gillesm.lib_test.craft.craft_entity;

import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DynamicCraft {

	RecipeRunnable<? extends CraftRecipe, ? extends DynamicCraftMenu> start(final Player player,
																			final DynamicCraftMenu dynamicCraftMenu);

	/**
	 * Rework the item.
	 *
	 * @param player the player reworking the item
	 * @param itemStack the item being reworked
	 * @return true if the item has been reworked, false otherwise
	 */
	default boolean reWork(Player player, ItemStack itemStack) {
		return false;
	}

}
