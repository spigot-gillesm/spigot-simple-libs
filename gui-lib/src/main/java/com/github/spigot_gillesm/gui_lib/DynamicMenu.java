package com.github.spigot_gillesm.gui_lib;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class DynamicMenu extends SimpleMenu {

	@Setter(AccessLevel.PROTECTED)
	private long delay = 0;

	@Setter(AccessLevel.PROTECTED)
	private long interval = 1;

	@Override
	public void display(@NotNull final Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				execute(player);
				if(over(player)) {
					cancel();
				}
			}
		}.runTaskTimer(GuiLib.plugin, delay, interval);
	}

	/**
	 * Call the display method of the superclass and run the
	 * update command.
	 *
	 * @param player the player
	 */
	private void execute(@NotNull final Player player) {
		update(player);
		super.display(player);
		setCancelReinstantiation(true);
	}

	/**
	 * The action to perform after the delay and at each interval.
	 *
	 * @param player the player
	 */
	protected abstract void update(final Player player);

	/**
	 * Check if the menu should stop updating.
	 *
	 * @param player the player
	 * @return true if the menu should stop updating
	 */
	protected abstract boolean over(final Player player);

}
