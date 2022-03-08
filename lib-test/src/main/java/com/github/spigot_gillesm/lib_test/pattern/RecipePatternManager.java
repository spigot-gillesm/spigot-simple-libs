package com.github.spigot_gillesm.lib_test.pattern;

import com.github.spigot_gillesm.item_lib.ItemUtil;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@UtilityClass
public class RecipePatternManager {

	private final Map<String, RecipePattern> LOADED_PATTERNS = new HashMap<>();

	private final String KEY_ID = "pattern-id";

	public void loadPattern(final CraftEntity craftEntity) {
		final var pattern = RecipePattern.newBuilder()
				.physicalPattern(createPhysicalPattern(craftEntity))
				.craftId(craftEntity.getId())
				.build();
		LOADED_PATTERNS.put(craftEntity.getId(), pattern);
	}

	private SimpleItem createPhysicalPattern(final CraftEntity craftEntity) {
		return SimpleItem.newBuilder()
				.material(Material.ENCHANTED_BOOK)
				.displayName("&fPattern: " + craftEntity.getItem().getItemMeta().getDisplayName())
				.lore(createLore(craftEntity))
				.addItemFlags(ItemFlag.HIDE_ENCHANTS)
				.addPersistentString(KEY_ID, craftEntity.getId())
				.build();
	}

	private List<String> createLore(final CraftEntity craftEntity) {
		final var itemMeta = craftEntity.getItem().getItemMeta();
		final var lore = new ArrayList<>(Arrays.asList("", "&aUse&7: Teaches you how to make a ", itemMeta.getDisplayName(),
				"&7---------------------------", "", itemMeta.getDisplayName()));
		lore.addAll(itemMeta.getLore());
		return lore;
	}

	public Optional<RecipePattern> getRecipePattern(final String id) {
		if(LOADED_PATTERNS.containsKey(id)) {
			return Optional.of(LOADED_PATTERNS.get(id));
		} else {
			return Optional.empty();
		}
	}

	private Optional<String> getItemId(final ItemStack itemStack) {
		return ItemUtil.getPersistentString(itemStack, KEY_ID);
	}

	public Optional<RecipePattern> getRecipePattern(final ItemStack itemStack) {
		return getItemId(itemStack).flatMap(RecipePatternManager::getRecipePattern).or(Optional::empty);
	}

}
