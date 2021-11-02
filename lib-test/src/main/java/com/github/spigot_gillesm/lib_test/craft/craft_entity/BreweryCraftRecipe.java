package com.github.spigot_gillesm.lib_test.craft.craft_entity;

import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BreweryCraftRecipe extends CraftEntity {

	//item = result = the bottles you get at the end
	//reagent = ingredient = blaze powder in regular potion crafting

	@Getter
	private final ItemStack fuel;

	//The bottles that will be changed into the result
	@Getter
	private final ItemStack receptacle;

	private BreweryCraftRecipe(final Builder builder) {
		super(builder);
		this.fuel = builder.fuel;
		this.receptacle = builder.receptacle;
	}

	public void start(final BrewerInventory inventory) {
		new BrewRunnable(this, inventory, 400).runTaskTimer(LibTest.getInstance(), 0, 1);
	}

	@Override
	public ItemStack getResult() {
		return getItem();
	}

	@Override
	public int getAmount() {
		return 3;
	}

	@Override
	public boolean canCraft(final Player player) {
		return PlayerManager.getProfession(player).map(value -> value.equals(profession)).orElse(false);
	}

	@Override
	public boolean isPatternMatching(final ItemStack[] items) {
		return false;
	}

	@Override
	public boolean isSimilar(final ItemStack itemStack) {
		return getItem().isSimilar(itemStack);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder extends CraftEntity.Builder<Builder> {

		//item = result = the bottles you get at the end
		//reagent = ingredient = blaze powder in regular potion crafting

		//Fuel = fuel
		private ItemStack fuel;

		//The "bottles" that will be changed into the result
		private ItemStack receptacle;

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

		@Override
		public BreweryCraftRecipe build() {
			if(profession == null) {
				throw new IllegalArgumentException("Profession cannot be null.");
			}
			if(item == null) {
				throw new IllegalArgumentException("Item cannot be null.");
			}
			if(receptacle == null) {
				throw new IllegalArgumentException("Receptacle cannot be null.");
			}
			return new BreweryCraftRecipe(this);
		}

	}

	private static class BrewRunnable extends BukkitRunnable {

		private final BreweryCraftRecipe recipe;

		private final BrewerInventory inventory;

		private final BrewingStand brewingStand;

		private final ItemStack[] originalContent;

		//Start at max time and is decreased by (1 * interval's ticks) tick(s) at every interval
		//-> get decremented
		private int currentTimer;

		private BrewRunnable(final BreweryCraftRecipe recipe, final BrewerInventory inventory, final int time) {
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
					inventory.setItem(i, recipe.getResult());
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
