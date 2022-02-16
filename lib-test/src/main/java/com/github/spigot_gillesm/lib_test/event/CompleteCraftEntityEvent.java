package com.github.spigot_gillesm.lib_test.event;

import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CompleteCraftEntityEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	@Getter
	private final CraftEntity craftEntity;

	@Getter
	private final Player player;

	public CompleteCraftEntityEvent(final Player player, final CraftEntity craftEntity) {
		this.player = player;
		this.craftEntity = craftEntity;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}
