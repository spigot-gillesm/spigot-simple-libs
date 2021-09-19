package com.github.spigot_gillesm.lib_test.craft.dynamic_craft;

import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.CraftRunnable;
import com.github.spigot_gillesm.lib_test.craft.DynamicCraft;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class ForgeCraft implements DynamicCraft {

	private final int requiredHeat;

	//How much +/- heat there can be before it fails
	private final int tolerance;

	//Unit = Tick(s)
	private final long time;

	//Between 0.0 and 1.0
	private final double changeChance;

	private ForgeCraft(final Builder builder) {
		this.requiredHeat = builder.requiredHeat;
		this.tolerance = builder.tolerance;
		this.time = builder.time;
		this.changeChance = builder.changeChance;
	}

	@Override
	public CraftRunnable start(final Player player, final DynamicCraftMenu forgeMenu) {
		var runnable = new ForgeCraftRunnable(this, player, (DynamicForgeMenu) forgeMenu);
		runnable.runTaskTimer(LibTest.getInstance(), 0, 1);
		return runnable;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private int requiredHeat = 5;

		private int tolerance = 2;

		//Unit = Tick(s)
		private long time = (long) 5 * 20;

		//Between 0.0 and 1.0
		private double changeChance = 0.25;

		private Builder() { }

		public Builder requiredHeat(final int requiredHeat) {
			this.requiredHeat = requiredHeat;
			return this;
		}

		public Builder tolerance(final int tolerance) {
			this.tolerance = tolerance;
			return this;
		}

		public Builder time(final int time) {
			this.time = time;
			return this;
		}

		public Builder changeChance(final double chance) {
			this.changeChance = chance;
			return this;
		}

		public ForgeCraft build() {
			if(changeChance < 0 || changeChance > 1) {
				throw new IllegalArgumentException("Change chance must be between 0 and 1");
			}
			return new ForgeCraft(this);
		}

	}

	public static class ForgeCraftRunnable extends CraftRunnable {

		private final ForgeCraft forgeCraft;

		private final Player player;

		private final DynamicForgeMenu forgeMenu;

		@Getter
		private int heat;

		private long currentTimer;

		/**
		 * Represents the direction in which the heat will be modified
		 * based on the forgeCraft.changeChance. 0 means on the left
		 * (= cooling down), 1 means right (= heating up)
		 */
		private int direction;

		private int globalTimer = 0;

		private ForgeCraftRunnable(final ForgeCraft forgeCraft, final Player player, final DynamicForgeMenu forgeMenu) {
			this.forgeCraft = forgeCraft;
			this.player = player;
			this.forgeMenu = forgeMenu;
			this.heat = forgeCraft.requiredHeat;
			this.currentTimer = forgeCraft.time;
			this.direction = getDirection();
		}

		/**
		 * Get the direction of the heat used for random modification
		 *
		 * @return a random number between 0 and 1
		 */
		private int getDirection() {
			return new Random().nextInt(2);
		}

		@Override
		public void run() {
			//Check if the smelting process is done
			if(currentTimer <= 0) {
				//Let the menu knows the craft is over
				forgeMenu.setFinished(true);
				forgeMenu.setCancelReinstantiation(true);
				//And re displays it
				forgeMenu.display(player);
				cancel();

				return;
			}
			//Check if the heat exceeded the threshold -> The craft failed
			if(heat > forgeCraft.requiredHeat + forgeCraft.tolerance
					|| heat < forgeCraft.requiredHeat - forgeCraft.tolerance) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				//Let the menu knows the craft failed
				forgeMenu.setFailed(true);
				forgeMenu.setCancelReinstantiation(true);
				//And re displays it
				forgeMenu.display(player);
				cancel();

				return;
			}
			//Check if the heat should be changed randomly. Can occur only once per second (= every 20 ticks)
			if(new Random().nextDouble() <= forgeCraft.changeChance && globalTimer >= 20) {
				//Check if the heat should go down
				if(direction == 0) {
					heat--;
				} else {
					//or up
					heat++;
				}
				//Update the menu
				forgeMenu.setCancelReinstantiation(true);
				forgeMenu.display(player);
				//Reset the global timer
				globalTimer = 0;
			}
			//If the heat is right -> decrease the timer
			if(heat == forgeCraft.requiredHeat) {
				currentTimer--;
			}
			//Update the global timer
			globalTimer++;
		}

		public void heatUp() {
			heat = PluginUtil.clamp(heat + 1, 0, 9);
			//Randomly reset the direction
			direction = getDirection();
		}

		public void coolDown() {
			heat = PluginUtil.clamp(heat - 1, 0, 9);
			//Randomly reset the direction
			direction = getDirection();
		}

	}

}
