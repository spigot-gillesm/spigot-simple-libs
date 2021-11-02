package com.github.spigot_gillesm.gui_lib;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleMenuInteractEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean isCancelled;

	@Getter
	private final int slot;
	
	@Getter
	private final ItemStack clickedItem;

	SimpleMenuInteractEvent(final int slot, final ItemStack clickedItem) {
		this.slot = slot;
		this.clickedItem = clickedItem;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean state) {
		this.isCancelled = state;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}
