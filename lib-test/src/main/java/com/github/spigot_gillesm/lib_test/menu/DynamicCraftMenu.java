package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.craft.CraftRunnable;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a dynamic crafting menu.
 */
public abstract class DynamicCraftMenu extends SimpleMenu {

	@Setter
	protected CraftRunnable craftRunnable;

	@Setter
	protected boolean finished = false;

	@Setter
	protected boolean failed = false;

	@Setter
	protected ItemStack result;

}
