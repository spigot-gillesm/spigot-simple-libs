package com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe;

import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.DynamicCraft;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.RecipeRunnable;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class ForgeCraftRecipe extends CraftRecipe implements DynamicCraft {

	private final int requiredHeat;

	//How much +/- heat there can be before it fails
	private final int tolerance;

	//Unit = Tick(s)
	private final long time;

	//Between 0.0 and 1.0
	private final double changeChance;

	private ForgeCraftRecipe(final Builder builder) {
		super(builder);
		this.requiredHeat = builder.requiredHeat;
		this.tolerance = builder.tolerance;
		this.time = builder.time;
		this.changeChance = builder.changeChance;
	}

	@Override
	public RecipeRunnable<ForgeCraftRecipe, DynamicForgeMenu> start(final Player player,
																	final DynamicCraftMenu forgeMenu) {
		var runnable = new ForgeCraftRunnable(this, player, (DynamicForgeMenu) forgeMenu);
		runnable.runTaskTimer(LibTest.getInstance(), 0, 1);
		return runnable;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder extends CraftRecipe.Builder {

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

		@Override
		public ForgeCraftRecipe build() {
			super.build();
			if(changeChance < 0 || changeChance > 1) {
				throw new IllegalArgumentException("Change chance must be between 0 and 1");
			}
			if(dynamicCraftMenu == null) {
				throw new IllegalArgumentException("Dynamic craft menu cannot be null for an instance of DynamicCraft.");
			}
			return new ForgeCraftRecipe(this);
		}

	}

	public static class ForgeCraftRunnable extends RecipeRunnable<ForgeCraftRecipe, DynamicForgeMenu> {

		@Getter
		private int heat;

		private long currentTimer;

		/**
		 * Represents the direction in which the heat will be modified
		 * based on the forgeCraft.changeChance. 0 means on the left
		 * (= cooling down), 1 means right (= heating up)
		 */
		private int direction;

		private ForgeCraftRunnable(final ForgeCraftRecipe recipe, final Player player, final DynamicForgeMenu menu) {
			super(player, menu, recipe);
			this.heat = recipe.requiredHeat;
			this.currentTimer = recipe.time;
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
		protected boolean execute() {
			//Check if the smelting process is done
			if(currentTimer <= 0) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				//Let the menu knows the craft is over
				getMenu().setFinished(true);
				getMenu().setCancelReinstantiation(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			//Check if the heat exceeded the threshold -> The craft failed
			if(heat > getRecipe().requiredHeat + getRecipe().tolerance
					|| heat < getRecipe().requiredHeat - getRecipe().tolerance) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				//Let the menu knows the craft failed
				getMenu().setFailed(true);
				getMenu().setCancelReinstantiation(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			//Check if the heat should be changed randomly. Can occur only once per second (= every 20 ticks)
			if(new Random().nextDouble() <= getRecipe().changeChance && globalTimer >= 20) {
				//Check if the heat should go down
				if(direction == 0) {
					heat--;
				} else {
					//or up
					heat++;
				}
				//Update the menu
				getMenu().setCancelReinstantiation(true);
				getMenu().display(player);
				//Reset the global timer
				globalTimer = 0;
			}
			//If the heat is right -> decrease the timer
			if(heat == getRecipe().requiredHeat) {
				currentTimer--;
			}

			return false;
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
