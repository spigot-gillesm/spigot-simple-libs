package com.github.spigot_gillesm.lib_test.craft;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.item_lib.YamlItem;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.BreweryCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.AnvilCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.EnchantCraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.ForgeCraftRecipe;
import com.github.spigot_gillesm.lib_test.item.ItemManager;
import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.PotionMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicEnchantMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import com.github.spigot_gillesm.lib_test.pattern.RecipePatternManager;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@UtilityClass
public class CraftManager {

	private final Map<String, CraftEntity> loadedCrafts = new LinkedHashMap<>();

	private Optional<BreweryCraftRecipe.Builder> loadBreweryRecipe(final ConfigurationSection section) {
		final var builder = BreweryCraftRecipe.newBuilder();
		final AtomicBoolean valid = new AtomicBoolean(true);

		if(!section.contains("reagent")) {
			Formatter.error("BREWING_STAND recipes must have a reagent for " + section.getName() + ".");
			return Optional.empty();
		}
		builder.fuelConsumed(section.getBoolean("fuel-consumed", false));
		getFuelFromConfig(section).ifPresent(builder::fuel);
		getReceptacleFromConfig(section).ifPresentOrElse(builder::receptacle, () -> valid.set(false));

		if(valid.get()) {
			return Optional.of(builder);
		} else {
			return Optional.empty();
		}
	}

	private Optional<ItemStack> getFuelFromConfig(final ConfigurationSection section) {
		if(!section.contains("fuel")) {
			return Optional.empty();
		}
		final var fuelName = section.getString("fuel");

		if(fuelName == null || fuelName.isBlank()) {
			return Optional.empty();
		}
		try {
			return ItemManager.getItem(fuelName).map(SimpleItem::getItemStack)
					.or(() -> Optional.of(
							SimpleItem.newBuilder()
									.material(Material.valueOf(fuelName.toUpperCase()))
									.build().getItemStack()
							)
					);
		} catch (final IllegalArgumentException e) {
			Formatter.error("Invalid fuel for " + section.getName() + ".");
			return Optional.empty();
		}
	}

	private Optional<ItemStack> getReceptacleFromConfig(final ConfigurationSection section) {
		if(!section.contains("recipe")) {
			return Optional.empty();
		}
		//Check if the receptacle must be built from file
		if(section.isConfigurationSection("recipe")) {
			final var recipeSection = section.getConfigurationSection("recipe");

			if(!section.contains("material")) {
				Formatter.error("The recipe section must contain at least the material for " + section.getName() + ".");
				return Optional.empty();
			}
			final var recipe = YamlItem.fromConfiguration(recipeSection).getItemFromFile();

			if(recipe == null) {
				Formatter.error("Invalid recipe for " + section.getName() + ".");
				return Optional.empty();
			} else {
				return Optional.of(recipe.getItemStack());
			}

			//Else check if the item is a custom item
		} else {
			return ItemManager.getItem(section.getString("recipe")).map(SimpleItem::getItemStack)
					.or(() -> {
						Formatter.error("Unknown item: " + section.getString("recipe") + " for " + section.getName() + ".");
						return Optional.empty();
					});
		}
	}

	private Optional<CraftRecipe.Builder> loadCraftRecipe(final ConfigurationSection section,
														  final Workstation station) {
		//Set the recipe
		final var recipeSection = section.getConfigurationSection("recipe");
		CraftRecipe.Builder builder = CraftRecipe.newBuilder();

		//TODO: improve that shitty code
		if(section.getBoolean("dynamic", false) && station.hasDynamicCraftMenu()) {
			final var menu = station.getDynamicMenu();
			if(menu.equals(DynamicAnvilMenu.class)) {
				builder = AnvilCraftRecipe.newBuilder()
						.hardeningAmount(section.getInt("hardening-amount", 0));
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
					final var item = getPatternItemFromConfig(section, name);

					if(item.isPresent()) {
						builder.setPatternItem(position, item.get());
					} else {
						return Optional.empty();
					}
				}
			}
		}

		return Optional.of(builder);
	}

	private Optional<ItemStack> getPatternItemFromConfig(final ConfigurationSection section, final String itemName) {
		try {
			return ItemManager.getItem(itemName).map(SimpleItem::getItemStack)
					.or(() -> Optional.of(
									SimpleItem.newBuilder()
											.material(Material.valueOf(itemName.toUpperCase()))
											.build()
											.make()
											.getItemStack()
							)
					);
		} catch (final IllegalArgumentException e) {
			Formatter.error("Invalid pattern item for " + section.getName() + ".");
			return Optional.empty();
		}
	}

	private Optional<? extends CraftEntity.Builder<?>> loadSpecific(final ConfigurationSection section,
																	final Workstation station) {
		Optional<? extends CraftEntity.Builder<?>> builder;

		if(station.getMenu().equals(PotionMenu.class) && section.getBoolean("dynamic", false)) {
			builder = loadBreweryRecipe(section);
		} else {
			if(section.isConfigurationSection("recipe")) {
				builder = loadCraftRecipe(section, station);
			} else {
				Formatter.error("Invalid recipe data for " + section.getName() + ".");
				return Optional.empty();
			}
		}
		builder.ifPresent(b -> getReagentFromConfig(section).ifPresent(b::reagent));

		return builder;
	}

	private Optional<ItemStack> getReagentFromConfig(final ConfigurationSection section) {
		if(!section.contains("reagent")) {
			return Optional.empty();
		}
		if(section.isConfigurationSection("reagent")) {
			final var reagent = YamlItem.fromConfiguration(section.getConfigurationSection("reagent")).getItemFromFile();

			if(reagent == null) {
				Formatter.warning("Invalid reagent data for " + section.getName() + ".");
				return Optional.empty();
			} else {
				return Optional.of(reagent.getItemStack());
			}
		} else {
			final var reagentName = section.getString("reagent");
			final var reagentAmount = section.getInt("reagent-amount", 1);
			final var item = ItemManager.getItem(reagentName);

			return item.map(SimpleItem::getItemStack)
					.or(() -> {
						try {
							return Optional.of(SimpleItem.newBuilder().material(Material.valueOf(reagentName.toUpperCase()))
									.amount(reagentAmount).build().make().getItemStack());
						} catch (final IllegalArgumentException e) {
							Formatter.warning("Unknown reagent: " + reagentName + " for " + section.getName() + ".");
							return Optional.empty();
						}
					});
		}
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
		final Map<String, CraftEntity> crafts = new LinkedHashMap<>();

		for(final String id : conf.getKeys(false)) {
			if(conf.isConfigurationSection(id)) {
				loadCraftFromFile(conf, id, file.getName()).ifPresent(craftItem -> {
					crafts.put(id, craftItem);

					if(!craftItem.getMetaData().isKnownByDefault()) {
						RecipePatternManager.loadPattern(craftItem);
					}
				});
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
					.map(value -> value
							.profession(profession.get())
							.id(id)
							.craftingMenu(station.get().getMenu())
							.item(item.get().getItemStack())
							.amount(section.getInt("amount", 1))
							.metaData(CraftEntityMeta.newBuilder()
									.requiredLevel(section.getInt("required-level", 0))
									.levelGain(section.getInt("level-gain", 1))
									.levelCap(section.getInt("level-cap", 10))
									.knownByDefault(section.getBoolean("default", true))
									.build())
							.build()
					);
	}

	public List<CraftEntity> getCraftItems() {
		final List<CraftEntity> craftEntities = new ArrayList<>();

		for(final var entrySet : loadedCrafts.entrySet()) {
			craftEntities.add(entrySet.getValue());
		}

		return craftEntities;
	}

	public Optional<CraftEntity> getCraftItem(final String id) {
		if(loadedCrafts.containsKey(id)) {
			return Optional.of(loadedCrafts.get(id));
		} else {
			return Optional.empty();
		}
	}

	public Optional<AnvilCraftRecipe> getAnvilCraftRecipe(final ItemStack itemStack) {
		return ItemManager.getItemId(itemStack).flatMap(id -> getCraftItems().stream()
				.filter(AnvilCraftRecipe.class::isInstance)
				.filter(recipe -> recipe.getId().equals(id))
				.findFirst()
				.map(AnvilCraftRecipe.class::cast)
				.or(Optional::empty));
		/*final var items = getCraftItems().stream()
				.filter(AnvilCraftRecipe.class::isInstance)
				.collect(Collectors.toList());
		for(final CraftEntity item : items) {
			final var blacksmithItem = (AnvilCraftRecipe) item;
			if(blacksmithItem.isSimilar(itemStack)) {
				return Optional.of(blacksmithItem);
			}
		}

		return Optional.empty();*/
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
		final List<CraftEntity> craftEntities = new ArrayList<>();

		for(final CraftEntity craftEntity : getCraftItems()) {
			if(craftEntity.getProfession().equals(profession)) {
				craftEntities.add(craftEntity);
			}
		}

		return craftEntities;
	}

	public Optional<CraftEntity> getItemFromPattern(final ItemStack[] pattern) {
		for(final var item : getCraftItems()) {
			if(item.isPatternMatching(pattern)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

	public List<CraftEntity> getItemsWithRequiredLevel(final Profession profession, final int level) {
		return getItemsFromProfession(profession).stream()
				.filter(craftEntity -> craftEntity.getMetaData().getRequiredLevel() == level)
				.collect(Collectors.toList());
	}

}
