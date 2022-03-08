package com.github.spigot_gillesm.lib_test.event;

import com.github.spigot_gillesm.lib_test.pattern.RecipePattern;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class InteractWithPatternEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	@Getter
	private final RecipePattern recipePattern;

	@Getter
	private final Player player;

	public InteractWithPatternEvent(final Player player, final RecipePattern recipePattern) {
		this.player = player;
		this.recipePattern = recipePattern;
	}

	@Override
	@NotNull
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}
