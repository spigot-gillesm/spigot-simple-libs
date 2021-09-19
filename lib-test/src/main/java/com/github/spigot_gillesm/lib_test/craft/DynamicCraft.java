package com.github.spigot_gillesm.lib_test.craft;

import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import org.bukkit.entity.Player;

public interface DynamicCraft {

	CraftRunnable start(final Player player, final DynamicCraftMenu dynamicCraftMenu);

}
