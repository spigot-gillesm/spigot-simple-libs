package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.RecipeRunnable;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a dynamic crafting menu.
 */
public abstract class DynamicCraftMenu extends SimpleMenu {

	@Setter
	protected RecipeRunnable<? extends CraftRecipe, ? extends DynamicCraftMenu> recipeRunnable;

	@Setter
	protected boolean finished = false;

	@Setter
	protected boolean failed = false;

	@Setter
	protected ItemStack result;

	protected <T extends RecipeRunnable<?, ?>> T getRecipeRunnable(final Class<T> clazz) {
		return clazz.cast(recipeRunnable);
	}

}
