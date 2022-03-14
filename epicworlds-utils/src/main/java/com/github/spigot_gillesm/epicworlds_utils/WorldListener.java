package com.github.spigot_gillesm.epicworlds_utils;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class WorldListener implements Listener {

	@EventHandler
	private void onNewChunkLoad(final ChunkPopulateEvent event) {
		new ChunkScanner(event.getChunk(), material -> material.name().contains("CHEST"))
				.scan(block -> {
					Formatter.info("coord: " + block.getLocation());
					Formatter.info("Type: " + block.getType());
					if(block.getState() instanceof Chest) {
						Formatter.info("Chest at " + block.getLocation());
						final var chest = ((Chest) block.getState());
						chest.getBlockInventory().addItem(new ItemStack(Material.EGG));
					}
				});
	}

	@EventHandler
	private void onVillagerChangeProfession(final VillagerCareerChangeEvent event) {
		Formatter.info("villager changed profession at: " + event.getEntity().getLocation());
	}

	@EventHandler
	private void onVillagerGetNewTrades(final VillagerAcquireTradeEvent event) {
		Formatter.info("villager gained trades at: " + event.getEntity().getLocation());

		if(event.getEntity() instanceof Villager) {
			final var villager = (Villager) event.getEntity();
			Formatter.info("Villager level: " + villager.getVillagerLevel());

			if(villager.getVillagerLevel() == 1) {
				final var recipes = villager.getRecipes();
				final List<MerchantRecipe> mutableRecipes = new ArrayList<>(recipes);
				final var recipe = new MerchantRecipe(SimpleItem.newBuilder()
						.material(Material.IRON_SWORD)
						.displayName("&aSteel Sword")
						.addEnchantment(Enchantment.DURABILITY, 2)
						.addEnchantment(Enchantment.DAMAGE_ALL, 1)
						.addLore("", "&7Stronger than iron!")
						.build().make().getItemStack(), 4);
				recipe.addIngredient(new ItemStack(Material.EMERALD, 9));
				mutableRecipes.add(recipe);

				villager.setRecipes(mutableRecipes);
			}
		}
	}

	@EventHandler
	private void onEntitySpawnEvent(final EntitySpawnEvent event) {
		if(event.getEntityType() == EntityType.VILLAGER) {
			Formatter.info("Villager spawned at: " + event.getEntity().getLocation());
		}
	}

}
