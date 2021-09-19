package com.github.spigot_gillesm.lib_test.craft;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Tag interface
 */
public interface DynamicCraftItem {

	boolean reWork(Player player, ItemStack itemStack);

}
