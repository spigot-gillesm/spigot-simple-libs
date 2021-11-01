package com.github.spigot_gillesm.lib_test.craft;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.YamlItem;
import com.github.spigot_gillesm.lib_test.ItemManager;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.BreweryCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.AnvilCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.EnchantCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.ForgeCraftRecipe;
import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.PotionMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicEnchantMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import com.github.spigot_gillesm.lib_test.profession.Profession;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.lib_test.profession.Workstation;
import com.github.spigot_gillesm.lib_test.profession.WorkstationManager;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class CraftManager {

	private final Map<String, CraftEntity> loadedCrafts = new HashMap<>();

	private Optional<BreweryCraftRecipe.Builder> loadBreweryRecipe(final ConfigurationSection section) {
		final var builder = BreweryCraftRecipe.newBuilder();

		try {
			final var fuelName = section.getString("fuel");

			if(ItemManager.exists(fuelName)) {
				builder.fuel(ItemManager.getItem(fuelName).get().getItemStack());
			} else if(fuelName != null && !fuelName.isBlank()) {
				builder.fuel(Material.valueOf(fuelName.toUpperCase()));
			}

			//Check if the receptacle must be built from file
			if(section.isConfigurationSection("recipe")) {
				final var recipeSection = section.getConfigurationSection("recipe");

				if(!section.contains("material")) {
					Formatter.error("The recipe section must contain at least the material.");
					return Optional.empty();
				}
				final var recipe = YamlItem.fromConfiguration(recipeSection).getItemFromFile();

				if(recipe == null) {
					Formatter.error("Invalid recipe.");
					return Optional.empty();
				} else {
					return Optional.of(builder.receptacle(recipe.getItemStack()));
				}

				//Else check if the item is a custom item
			} else {
				final var recipe = ItemManager.getItem(section.getString("recipe"));

				if(recipe.isPresent()) {
					return Optional.of(builder.receptacle(recipe.get().getItemStack()));
				} else {
					Formatter.error("Unknown item: " + section.getString("recipe") + " for recipe.");
					return Optional.empty();
				}
			}

		} catch (final IllegalArgumentException e) {
			Formatter.error("Invalid fuel.");
			return Optional.empty();
		}
	}

	private Optional<CraftRecipe.Builder> loadCraftRecipe(final ConfigurationSection section,
														  final Workstation station) {
		//Set the recipe
		final var recipeSection = section.getConfigurationSection("recipe");
		CraftRecipe.Builder builder = CraftRecipe.newBuilder();

		if(section.getBoolean("dynamic", false) && station.hasDynamicCraftMenu()) {
			final var menu = station.getDynamicMenu();
			if(menu.equals(DynamicAnvilMenu.class)) {
				builder = AnvilCraftRecipe.newBuilder()
						.hardeningAmount(section.getInt("hardening-amount", 1));
			} else if(menu.equals(DynamicForgeMenu.class)) {
				builder = ForgeCraftRecipe.newBuilder();
			} else if(menu.equals(DynamicEnchantMenu.class)) {
				builder = EnchantCraftRecipe.newBuilder();
			}
			builder.dynamicCraftMenu(station.getDynamicMenu());
		}

		//Loop through all the pattern items from 0 -> 8
		for (final String key : recipeSection.getKeys(false)) {
			if(PluginUtil.isInt(key)) {
				final var position = Integer.parseInt(key);

				if(position < 9 && position >= 0) {
					final var name = recipeSection.getString(key);
					try {
						//Check if the component is a custom item or a vanilla item
						final var component = ItemManager.getItem(name).isPresent() ?
								ItemManager.getItem(name).get().getItemStack() :
								new ItemStack(Material.valueOf(name.toUpperCase()));

						builder.setPatternItem(position, component);
					} catch (final IllegalArgumentException e) {
						Formatter.error("The component " + recipeSection.getString(key) + " doesn't exist.");
						return Optional.empty();
					}
				}
			}
		}

		return Optional.of(builder);
	}

	private Optional<? extends CraftEntity.Builder<?>> loadSpecific(final ConfigurationSection section,
																	final Workstation station) {
		Optional<? extends CraftEntity.Builder<?>> builder;

		if(station.getMenu().equals(PotionMenu.class) && section.getBoolean("dynamic", false)) {
			if(!section.contains("reagent")) {
				Formatter.error("BREWING_STAND recipes must have a reagent.");
				return Optional.empty();
			}
			builder = loadBreweryRecipe(section);
		} else {
			if(section.isConfigurationSection("recipe")) {
				builder = loadCraftRecipe(section, station);
			} else {
				Formatter.error("Invalid recipe data.");
				return Optional.empty();
			}
		}

		builder.ifPresent(b -> {
			if(section.contains("reagent")) {
				if(section.isConfigurationSection("reagent")) {
					final var reagent = YamlItem.fromConfiguration(section.getConfigurationSection("reagent")).getItemFromFile();

					if(reagent == null) {
						Formatter.warning("Invalid reagent data.");
					} else {
						b.reagent(reagent.getItemStack());
					}
				} else {
					final var reagentName = section.getString("reagent");
					final var reagentAmount = section.getInt("reagent-amount", 1);

					if(ItemManager.exists(reagentName)) {
						b.reagent(ItemManager.getItem(reagentName).get().getItemStack(), reagentAmount);
					} else {
						try {
							b.reagent(Material.valueOf(reagentName.toUpperCase()), reagentAmount);
						} catch (final IllegalArgumentException e) {
							Formatter.warning("Unknown reagent: " + reagentName + ".");
						}
					}
				}
			}
		});

		return builder;
	}

	public void loadCrafts() {
		loadedCrafts.clear();
		Formatter.info("Loading crafts...");
		final var folder = FileUtils.getResource("items/");

		if(folder != null && folder.isDirectory()) {
			Arrays.stream(folder.listFiles())
					.filter(f -> !f.isDirectory())
					.forEach(f -> loadedCrafts.putAll(loadCraftsFromFile(f)));
		}
		Formatter.info("Loaded " + loadedCrafts.size() + " craft(s).");
	}

	public Map<String, CraftEntity> loadCraftsFromFile(final File file) {
		final var conf = FileUtils.getConfiguration(file);
		final Map<String, CraftEntity> crafts = new HashMap<>();

		for(final String id : conf.getKeys(false)) {
			if(conf.isConfigurationSection(id)) {
				Formatter.info("Loading craft " + id + ".");
				loadCraftFromFile(conf, id, file.getName()).ifPresent(craftItem -> crafts.put(id, craftItem));
			}
		}

		return crafts;
	}

	public Optional<CraftEntity> loadCraftFromFile(final YamlConfiguration configuration, final String id, final String fileName) {
		final var section = configuration.getConfigurationSection(id);

		//If the section has no recipe -> don't try to load it
		if(!section.contains("recipe")) {
			Formatter.info("Recipe " + id + " has no recipe.");
			return Optional.empty();
		}

		final var professionName = fileName.split("\\.")[0];
		final var profession = ProfessionManager.getProfession(professionName);

		if(profession.isEmpty()) {
			Formatter.error("Profession " + professionName + " doesn't exist.");
			return Optional.empty();
		}

		final var stationName = section.getString("station");
		final var station = WorkstationManager.getWorkstation(stationName);
		final var item = ItemManager.getItem(id);

		if(station.isEmpty()) {
			Formatter.error("The station " + stationName + " doesn't exist.");
			return Optional.empty();
		}
		if(item.isEmpty()) {
			Formatter.error("The item " + id + " doesn't exist.");
			return Optional.empty();
		}

		return loadSpecific(section, station.get())
				.map(value -> value.profession(profession.get())
				.craftingMenu(station.get().getMenu())
				.item(item.get().getItemStack())
				.amount(section.getInt("amount", 1)).build());
	}

	public List<CraftEntity> getCraftItems() {
		return new ArrayList<>(loadedCrafts.values());
	}

	public Optional<CraftEntity> getCraftItem(final String id) {
		if(loadedCrafts.containsKey(id)) {
			return Optional.of(loadedCrafts.get(id));
		} else {
			return Optional.empty();
		}
	}

	public Optional<AnvilCraftRecipe> getAnvilCraftRecipe(final ItemStack itemStack) {
		final var items = getCraftItems().stream()
				.filter(AnvilCraftRecipe.class::isInstance)
				.collect(Collectors.toList());
		for(final CraftEntity item : items) {
			final var blacksmithItem = (AnvilCraftRecipe) item;
			if(blacksmithItem.isSimilar(itemStack)) {
				return Optional.of(blacksmithItem);
			}
		}

		return Optional.empty();
	}

	public Optional<CraftEntity> getCraftItem(final ItemStack itemStack) {
		for(final var craftItem : getCraftItems()) {
			if(craftItem.isSimilar(itemStack)) {
				return Optional.of(craftItem);
			}
		}

		return Optional.empty();
	}

	public List<CraftEntity> getItemsFromProfession(final Profession profession) {
		return getCraftItems().stream()
				.filter(c -> c.getProfession().equals(profession))
				.collect(Collectors.toList());
	}

	public Optional<CraftEntity> getItemFromPattern(final ItemStack[] pattern) {
		for(final var item : getCraftItems()) {
			if(item.isPatternMatching(pattern)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

}
