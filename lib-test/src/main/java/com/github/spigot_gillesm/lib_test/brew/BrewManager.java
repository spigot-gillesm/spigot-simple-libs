package com.github.spigot_gillesm.lib_test.brew;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class BrewManager {

	/*
	 * BARMAN CRAFTS
	 */

	public final BrewRecipe WHEAT_BEER = BrewRecipe.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.ingredient(Material.WHEAT)
			.fuel(Material.SUGAR)
			.receptacle(SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&aBière Légère au Blé")
					.potionType(PotionType.THICK)
					.addPotionEffect(PotionEffectType.CONFUSION, 20*30, 0)
					.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*2, 0)
					.build().getItemStack())
			.build();

	public final BrewRecipe SWEET_BEER = BrewRecipe.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.ingredient(Material.SWEET_BERRIES)
			.fuel(Material.SUGAR)
			.receptacle(SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&aBière Douce")
					.potionType(PotionType.THICK)
					.addPotionEffect(PotionEffectType.CONFUSION, 20*20, 0)
					.addPotionEffect(PotionEffectType.JUMP, 20*60*2, 0)
					.build().getItemStack())
			.build();

	public final BrewRecipe STRONG_WHEAT_BEER = BrewRecipe.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.ingredient(Material.FERMENTED_SPIDER_EYE)
			.fuel(Material.SUGAR)
			.receptacle(WHEAT_BEER.getResult())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&cBière Forte au Blé")
					.potionType(PotionType.THICK)
					.addPotionEffect(PotionEffectType.CONFUSION, 20*40, 1)
					.addPotionEffect(PotionEffectType.POISON, 20*10, 0)
					.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*2, 1)
					.build().getItemStack())
			.build();

	public final BrewRecipe PURIFIED_STRONG_WHEAT_BEER = BrewRecipe.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.ingredient(CraftManager.PURIFIER)
			.receptacle(STRONG_WHEAT_BEER.getResult())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&2Bière Forte au Blé Purifiée")
					.potionType(PotionType.THICK)
					.addPotionEffect(PotionEffectType.CONFUSION, 20*30, 0)
					.addPotionEffect(PotionEffectType.INCREASE_DAMAGE, (int) (20*60*1.5), 1)
					.build().getItemStack())
			.build();

	public final BrewRecipe HYDROMEL = BrewRecipe.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.ingredient(Material.HONEY_BOTTLE)
			.fuel(new ItemStack(Material.SUGAR))
			.receptacle(SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&aHydromel")
					.potionType(PotionType.THICK)
					.addPotionEffect(PotionEffectType.REGENERATION, 20*5, 0)
					.addPotionEffect(PotionEffectType.SLOW, 20*60*2, 0)
					.build().getItemStack())
			.build();

	public final BrewRecipe CIDER = BrewRecipe.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.ingredient(Material.APPLE)
			.fuel(Material.SUGAR)
			.receptacle(SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.result(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&aCidre")
					.potionType(PotionType.THICK)
					.addPotionEffect(PotionEffectType.REGENERATION, 20*5, 0)
					.addPotionEffect(PotionEffectType.WEAKNESS, 20*60*2, 0)
					.build().getItemStack())
			.build();

	/*
	 * DOCTOR CRAFTS
	 */

	public final BrewRecipe ADRENALINE = BrewRecipe.newBuilder()
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
			.build();

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

	public List<BrewRecipe> getBrewRecipes() {
		final var fields = Arrays.stream(BrewManager.class.getDeclaredFields())
				.filter(f -> BrewRecipe.class.isAssignableFrom(f.getType()))
				.collect(Collectors.toList());
		final List<BrewRecipe> recipes = new ArrayList<>();

		for(final var field : fields) {
			try {
				final var recipe = (BrewRecipe) field.get(BrewManager.class);
				recipes.add(recipe);
			} catch (IllegalAccessException e) {
				Formatter.error("Error retrieving brew recipes from BrewManager");
			}
		}

		return recipes;
	}

	public Optional<BrewRecipe> getBrewingRecipeFromPattern(final BrewerInventory inventory) {
		for(final var recipe : getBrewRecipes()) {
			//No need to check the fuel
			if(recipe.getFuel() == null) {
				/*
				 * Check if the ingredient is similar
				 * Check if ONE of THE receptacles is similar (only one is needed)
				 */
				if(recipe.getIngredient().isSimilar(inventory.getIngredient())
						&& (recipe.getReceptacle().isSimilar(inventory.getItem(0))
						|| recipe.getReceptacle().isSimilar(inventory.getItem(1))
						|| recipe.getReceptacle().isSimilar(inventory.getItem(2)))) {
					return Optional.of(recipe);
				}
			} else {
				//Check the fuel too
				if(recipe.getIngredient().isSimilar(inventory.getIngredient())
						&& (recipe.getReceptacle().isSimilar(inventory.getItem(0))
						|| recipe.getReceptacle().isSimilar(inventory.getItem(1))
						|| recipe.getReceptacle().isSimilar(inventory.getItem(2)))
						&& recipe.getFuel().isSimilar(inventory.getFuel())) {
					return Optional.of(recipe);
				}
			}
		}
		return Optional.empty();
	}

	public List<BrewRecipe> getRecipesFromProfession(final ProfessionType professionType) {
		return getBrewRecipes().stream()
				.filter(recipe -> recipe.getProfessionType() == professionType)
				.collect(Collectors.toList());
	}

}
