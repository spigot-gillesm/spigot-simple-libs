package com.github.spigot_gillesm.lib_test.brew;

import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BrewRecipe {

	@Getter
	private final ProfessionType professionType;

	//The item consumed for the recipe
	@Getter
	private final ItemStack ingredient;

	@Getter
	private final ItemStack fuel;

	//The "bottles" that will be changed into the result
	@Getter
	private final ItemStack receptacle;

	@Getter
	private final ItemStack result;

	private BrewRecipe(final Builder builder) {
		this.professionType = builder.professionType;
		this.ingredient = builder.ingredient;
		this.fuel = builder.fuel;
		this.receptacle = builder.receptacle;
		this.result = builder.result;
	}

	public void start(final BrewerInventory inventory) {
		new BrewRunnable(this, inventory, 400).runTaskTimer(LibTest.getInstance(), 0, 1);
	}

	public boolean canCraft(final Player player) {
		return professionType == PlayerManager.getProfessionType(player);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private ProfessionType professionType;

		private ItemStack ingredient;

		private ItemStack fuel;

		private ItemStack receptacle;

		private ItemStack result;

		public Builder professionType(final ProfessionType professionType) {
			this.professionType = professionType;
			return this;
		}

		public Builder ingredient(final ItemStack ingredient) {
			this.ingredient = ingredient;
			return this;
		}

		public Builder ingredient(final Material ingredient) {
			return ingredient(new ItemStack(ingredient));
		}

		public Builder fuel(final ItemStack fuel) {
			this.fuel = fuel;
			return this;
		}

		public Builder fuel(final Material fuel) {
			return fuel(new ItemStack(fuel));
		}

		public Builder receptacle(final ItemStack receptacle) {
			this.receptacle = receptacle;
			return this;
		}

		public Builder receptacle(final Material receptacle) {
			return receptacle(new ItemStack(receptacle));
		}

		public Builder result(final ItemStack result) {
			this.result = result;
			return this;
		}

		public BrewRecipe build() {
			if(professionType == null) {
				throw new IllegalArgumentException("Profession type cannot be null.");
			}
			else if(ingredient == null) {
				throw new IllegalArgumentException("Ingredient cannot be null.");
			}
			else if(receptacle == null) {
				throw new IllegalArgumentException("Receptacle cannot be null.");
			}
			else if(result == null) {
				throw new IllegalArgumentException("Result cannot be null.");
			}
			return new BrewRecipe(this);
		}

	}

	private static class BrewRunnable extends BukkitRunnable {

		private final BrewRecipe recipe;

		private final BrewerInventory inventory;

		private final BrewingStand brewingStand;

		private final ItemStack[] originalContent;

		//Start at max time and is decreased by (1 * interval's ticks) tick(s) at every interval
		//-> get decremented
		private int currentTimer;

		private BrewRunnable(final BrewRecipe recipe, final BrewerInventory inventory, final int time) {
			this.recipe = recipe;
			this.inventory = inventory;
			this.brewingStand = inventory.getHolder();
			this.originalContent = inventory.getContents();
			this.currentTimer = time;
		}

		@Override
		public void run() {
			if(hasInventoryChanged(inventory.getContents())) {
				cancel();
				return;
			}
			if(currentTimer == 0) {
				brew();
				cancel();
			} else {
				brewingStand.setBrewingTime(currentTimer--);
				brewingStand.update(true);
			}
		}

		private void brew() {
			for(var i = 0; i < 3; i++) {
				//Check if there's a receptacle in the slot
				if(recipe.getReceptacle().isSimilar(inventory.getItem(i))) {
					inventory.setItem(i, recipe.result);
				}
			}
			final var newIngredient = inventory.getIngredient().clone();
			newIngredient.setAmount(newIngredient.getAmount() - 1);
			inventory.setIngredient(newIngredient);

			if(inventory.getFuel() != null) {
				final var newFuel = inventory.getFuel().clone();
				newFuel.setAmount(newFuel.getAmount() - 1);
				inventory.setFuel(newFuel);
			}
			brewingStand.getWorld().playSound(brewingStand.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
		}

		private boolean hasInventoryChanged(final ItemStack[] currentContent) {
			for(var i = 0; i < currentContent.length; i ++) {
				if((currentContent[i] == null || originalContent[i] == null)
						&& (currentContent[i] != null || originalContent[i] != null)) {
					return true;
				}
				if(currentContent[i] != null && !currentContent[i].equals(originalContent[i])) {
					return true;
				}
			}

			return false;
		}

	}

}
