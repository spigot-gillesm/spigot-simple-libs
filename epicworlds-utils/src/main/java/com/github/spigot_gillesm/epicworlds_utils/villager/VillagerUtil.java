package com.github.spigot_gillesm.epicworlds_utils.villager;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VillagerUtil {

	private static final VillagerUtil INSTANCE = new VillagerUtil();

	private VillagerUtil() { }

	public void addRecipeToTrades(final Villager villager, final ItemStack result, final ItemStack firstIngredient,
								  final ItemStack secondIngredient, final int maxUses) {
		if(firstIngredient == null) {
			throw new IllegalArgumentException("The first ingredient cannot be null.");
		}
		final List<MerchantRecipe> mutableRecipes = new ArrayList<>(villager.getRecipes());
		final var recipe = new MerchantRecipe(result, 4);

		recipe.addIngredient(firstIngredient);

		if(secondIngredient != null) {
			recipe.addIngredient(secondIngredient);
		}
		mutableRecipes.add(recipe);
		villager.setRecipes(mutableRecipes);
	}

	public boolean isProfession(final String string) {
		return Arrays.stream(Villager.Profession.values()).anyMatch(profession -> profession.name().equalsIgnoreCase(string));
	}

	public static VillagerUtil getInstance() {
		return INSTANCE;
	}

}
