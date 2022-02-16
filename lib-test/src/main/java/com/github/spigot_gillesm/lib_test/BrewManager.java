package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.BreweryCraftRecipe;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@UtilityClass
public class BrewManager {

	/*
	 * DOCTOR CRAFTS
	 */

	/*public final BrewRecipe ADRENALINE = BrewRecipe.newBuilder()
			.professionType(ProfessionType.DOCTOR)
			.ingredient(CraftManager.ADRENALINE_POWDER)
			.fuel(CraftManager.PURIFIER)
			.receptacle(SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&aAdrénaline")
					.potionType(PotionType.AWKWARD)
					.addPotionEffect(PotionEffectType.POISON, 20*10, 0)
					.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*30, 0)
					.addPotionEffect(PotionEffectType.SPEED, 20*30, 0)
					.build().getItemStack())
			.build();*/

	public void setBrewerInventoryItems(final Player player, final BrewerInventory inventory, final int slot,
										 final ItemStack draggedItem, final ItemStack clickedItem) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(LibTest.getInstance(), () -> {
			player.setItemOnCursor(draggedItem);
			inventory.setItem(slot, clickedItem);
			player.updateInventory();

			getBrewingRecipeFromPattern(inventory).ifPresent(
					recipe -> {
						if(recipe.canCraft(player) && PlayerManager.meetRequirements(player, recipe)
								&& isFueled(inventory.getHolder(), recipe)) {
							recipe.start(inventory);
						}
					});
		}, 1L);
	}

	public Optional<BreweryCraftRecipe> getBrewingRecipeFromPattern(final BrewerInventory inventory) {
		for(final var craft : CraftManager.getCraftItems()) {
			if(craft instanceof BreweryCraftRecipe) {
				final var recipe = (BreweryCraftRecipe) craft;
				//No need to check the fuel
				if(recipe.getFuel() == null) {
					/*
					 * Check if the ingredient is similar
					 * Check if ONE of THE receptacles is similar (only one is needed)
					 */
					if(isIngredientSimilar(inventory, recipe) && areReceptacleSimilar(inventory, recipe)) {
						return Optional.of(recipe);
					}
				} else {
					//Check the fuel too
					if(isIngredientSimilar(inventory, recipe) && areReceptacleSimilar(inventory, recipe)
							&& isFuelSimilar(inventory, recipe)) {
						return Optional.of(recipe);
					}
				}
			}
		}

		return Optional.empty();
	}

	private boolean areReceptacleSimilar(final BrewerInventory inventory, final BreweryCraftRecipe recipe) {
		return recipe.getReceptacle().isSimilar(inventory.getItem(0))
				|| recipe.getReceptacle().isSimilar(inventory.getItem(1))
				|| recipe.getReceptacle().isSimilar(inventory.getItem(2));
	}

	private boolean isFuelSimilar(final BrewerInventory inventory, final BreweryCraftRecipe recipe) {
		return recipe.getFuel().isSimilar(inventory.getFuel());
	}

	private boolean isIngredientSimilar(final BrewerInventory inventory, final BreweryCraftRecipe recipe) {
		return recipe.getReagent().isSimilar(inventory.getIngredient());
	}

	private boolean isFueled(final BrewingStand brewingStand, final BreweryCraftRecipe recipe) {
		if(recipe.isFuelConsumed()) {
			return brewingStand.getFuelLevel() > 0;
		} else {
			return true;
		}
	}

}
