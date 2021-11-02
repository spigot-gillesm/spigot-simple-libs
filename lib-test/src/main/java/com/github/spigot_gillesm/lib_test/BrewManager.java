package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.BreweryCraftRecipe;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
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
						if(recipe.canCraft(player)) {
							recipe.start(inventory);
						}
					});
		}, 1L);
	}

	public Optional<BreweryCraftRecipe> getBrewingRecipeFromPattern(final BrewerInventory inventory) {
		for(final var craft : CraftManager.getCraftItems()) {
			if(craft instanceof BreweryCraftRecipe) {
				final var brewCraft = (BreweryCraftRecipe) craft;
				//No need to check the fuel
				if(brewCraft.getFuel() == null) {
					/*
					 * Check if the ingredient is similar
					 * Check if ONE of THE receptacles is similar (only one is needed)
					 */
					if(brewCraft.getReagent().isSimilar(inventory.getIngredient())
							&& (brewCraft.getReceptacle().isSimilar(inventory.getItem(0))
							|| brewCraft.getReceptacle().isSimilar(inventory.getItem(1))
							|| brewCraft.getReceptacle().isSimilar(inventory.getItem(2)))) {
						return Optional.of(brewCraft);
					}
				} else {
					//Check the fuel too
					if(brewCraft.getReagent().isSimilar(inventory.getIngredient())
							&& (brewCraft.getReceptacle().isSimilar(inventory.getItem(0))
							|| brewCraft.getReceptacle().isSimilar(inventory.getItem(1))
							|| brewCraft.getReceptacle().isSimilar(inventory.getItem(2)))
							&& brewCraft.getFuel().isSimilar(inventory.getFuel())) {
						return Optional.of(brewCraft);
					}
				}
			}
		}
		return Optional.empty();
	}

}
