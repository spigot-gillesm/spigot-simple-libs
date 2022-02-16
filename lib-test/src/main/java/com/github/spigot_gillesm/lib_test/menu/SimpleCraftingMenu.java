package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import com.github.spigot_gillesm.lib_test.event.CompleteCraftEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class SimpleCraftingMenu extends SimpleMenu {

	protected final int resultSlot;

	protected SimpleCraftingMenu(final int resultSlot) {
		this.resultSlot = resultSlot;
	}

	public void completeCraft(final Player player, final Inventory inventory, final CraftEntity result) {
		inventory.setItem(resultSlot, result.getResult());
		runEvent(player, result);
	}

	public void runEvent(final Player player, final CraftEntity craftEntity) {
		final var event = new CompleteCraftEntityEvent(player, craftEntity);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

}
