package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.ItemUtil;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.item_lib.YamlItem;
import com.github.spigot_gillesm.lib_test.profession.Profession;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

@UtilityClass
public class ItemManager {

	private final Map<String, SimpleItem> loadedItems = new HashMap<>();

	public void loadItems() {
		loadedItems.clear();

		//path: /items/*.yml
		Formatter.info("Loading items...");
		final var folder = FileUtils.getResource("items/");

		if(folder != null && folder.isDirectory()) {

			Arrays.stream(folder.listFiles())
					.filter(f -> !f.isDirectory())
					.forEach(f -> loadedItems.putAll(loadItemsFromFile(f)));
		}
		Formatter.info("Loaded " + loadedItems.size() + " item(s).");
	}

	public Map<String, SimpleItem> loadItemsFromFile(@NotNull final File file) {
		final var conf = FileUtils.getConfiguration(file);
		final Map<String, SimpleItem> items = new HashMap<>();

		for(final String id : conf.getKeys(false)) {
			loadItemFromFile(conf, id).ifPresent(simpleItem -> items.put(id, simpleItem));
		}

		return items;
	}

	public Optional<SimpleItem> loadItemFromFile(@NotNull final YamlConfiguration configuration, @NotNull final String id) {
		final var itemSection = configuration.getConfigurationSection(id);

		//Check if the items should be loaded from mmoitems plugin
		if(itemSection.contains("mmo-item")) {
			return DependencyManager.getMmoItem(itemSection.getString("mmo-item"))
					.map(mi -> SimpleItem.newBuilder().itemStack(mi).build());
		}
		final var item = YamlItem.fromConfiguration(configuration).getBuilderFromFile(id);

		if(item == null || itemSection == null) {
			return Optional.empty();
		}

		final List<String> conditions = new ArrayList<>();

		if(itemSection.isConfigurationSection("conditions")) {
			final var conditionSection = itemSection.getConfigurationSection("conditions");

			if(conditionSection.contains("level")) {
				conditions.add("&2Required Level&f: " + conditionSection.getInt("level"));
			}
			if(conditionSection.contains("professions")) {
				conditions.add("&eProfessions&f:");

				for(final var profession : conditionSection.getStringList("professions")) {
					conditions.add("&f- " + PluginUtil.reformat(profession));
				}
			}
			if(!conditions.isEmpty()) {
				conditions.add(0, "");

				for(final var line : conditions) {
					item.addLore(line);
				}
			}
		}

		return Optional.of(item.build());
	}

	public Optional<SimpleItem> getItem(@NotNull final String id) {
		return Optional.ofNullable(loadedItems.get(id));
	}

	public Set<Profession> getItemProfessions(@NotNull final ItemStack itemStack) {
		final List<String> professionNames = ItemUtil.getStringListFromLore(itemStack, "Professions", "-");
		final Set<Profession> set = new HashSet<>();

		if(!professionNames.isEmpty()) {
			for(final var professionName : professionNames) {
				ProfessionManager.getProfession(professionName).ifPresent(set::add);
			}
		} else {
			return Collections.emptySet();
		}

		return set;
	}

	public boolean exists(@NotNull final String id) {
		return loadedItems.containsKey(id);
	}

}
