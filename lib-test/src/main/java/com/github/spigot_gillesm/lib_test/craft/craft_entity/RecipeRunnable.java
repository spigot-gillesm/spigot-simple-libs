package com.github.spigot_gillesm.lib_test.craft.craft_entity;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.craft.CraftRunnable;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import org.bukkit.entity.Player;

public abstract class RecipeRunnable<T extends CraftRecipe, K extends DynamicCraftMenu> extends CraftRunnable {

	protected final Player player;

	private final CraftRecipe recipe;

	private final DynamicCraftMenu menu;

	protected int score = 0;

	protected int globalTimer = 0;

	protected RecipeRunnable(final Player player, final DynamicCraftMenu menu, final CraftRecipe recipe) {
		this.player = player;
		this.menu = menu;
		this.recipe = recipe;
	}

	@Override
	public void run() {
		//If the menu is no longer open or the execute method return true -> cancel
		if(!isMenuOpen() || execute()) {
			cancel();
		}
		globalTimer++;
	}

	private boolean isMenuOpen() {
		//Check if the player left the menu
		return menu.equals(SimpleMenu.getMenu(player));
	}

	@SuppressWarnings("unchecked")
	protected T getRecipe() {
		return (T) recipe;
	}

	@SuppressWarnings("unchecked")
	protected K getMenu() {
		return (K) menu;
	}

	/**
	 * Execute the runnable logic.
	 *
	 * @return true if the runnable should stop
	 */
	protected abstract boolean execute();

}
