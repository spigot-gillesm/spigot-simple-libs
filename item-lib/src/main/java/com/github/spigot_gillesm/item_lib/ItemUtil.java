package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@UtilityClass
public class ItemUtil {

	/**
	 * Reduce the item amount by one
	 *
	 * @param itemStack the item
	 * @return true if the current amount is equal or smaller than 1 (the item should disappear)
	 */
	public boolean decrementItemAmount(@NotNull ItemStack itemStack) {
		if(itemStack.getAmount() <= 1) {
			return true;
		} else {
			itemStack.setAmount(itemStack.getAmount() - 1);

			return false;
		}
	}

	public EquipmentSlot getEquipmentSlot(final Material material) {
		EquipmentSlot slot = EquipmentSlot.HAND;

		if(material.name().contains("SHIELD")) {
			slot = EquipmentSlot.OFF_HAND;
		} else if(material.name().contains("HELMET")) {
			slot = EquipmentSlot.HEAD;
		} else if(material.name().contains("CHESTPLATE") ||
				material.name().contains("ELYTRA")) {
			slot = EquipmentSlot.CHEST;
		} else if(material.name().contains("LEGGINGS")) {
			slot = EquipmentSlot.LEGS;
		} else if(material.name().contains("BOOTS")) {
			slot = EquipmentSlot.FEET;
		}

		return slot;
	}

	/**
	 * Get the item's lore.
	 *
	 * @param itemStack the item
	 * @return a list of string containing the lore
	 */
	public List<String> getLore(@NotNull ItemStack itemStack) {
		if(!itemStack.hasItemMeta()) {
			return new ArrayList<>();
		} else {
			final ItemMeta meta = itemStack.getItemMeta();

			return meta.hasLore() ? meta.getLore() : new ArrayList<>();
		}
	}

	public boolean hasLineInLore(@NotNull ItemStack itemStack, @NotNull String line) {
		return hasLineInLore(itemStack, line, true);
	}

	public boolean hasLineInLore(@NotNull ItemStack itemStack, @NotNull String line, final boolean checkColor) {
		if(checkColor) {
			return getLore(itemStack).contains(Formatter.colorize(line));
		} else {
			for(final String l : getLore(itemStack)) {
				if(ChatColor.stripColor(l).equals(ChatColor.stripColor(line))) {
					return true;
				}
			}

			return false;
		}
	}

	public boolean hasStringInLore(@NotNull ItemStack itemStack, @NotNull String string) {
		return hasStringInLore(itemStack, string, true);
	}

	public boolean hasStringInLore(@NotNull ItemStack itemStack, @NotNull String string, final boolean checkColor) {
		for(final String line : getLore(itemStack)) {
			if(checkColor) {
				if(line.contains(Formatter.colorize(string))) {
					return true;
				}
			} else {
				if(line.contains(string)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Get the string linked to a specific key in the lore.
	 * Example: Profession: Enchanter
	 *
	 * @param itemStack the item
	 * @param key the key
	 * @return the string being the value of the key
	 */
	public String getStringFromLore(@NotNull ItemStack itemStack, @NotNull String key) {
		for(final String line : getLore(itemStack)) {
			final String cleanLine = ChatColor.stripColor(line);

			if(cleanLine.startsWith(key + ": ")) {
				final String[] data = cleanLine.split(": ");

				if(data.length > 1) {
					return ChatColor.stripColor(data[1]);
				}
			}
		}

		return "";
	}

	public List<String> getStringsFromLore(@NotNull ItemStack itemStack, @NotNull String key, @NotNull String separator) {
		final List<String> lore = getLore(itemStack);

		if(lore.isEmpty()) {
			return Collections.emptyList();
		}
		lore.forEach(ChatColor::stripColor);

		for(final String line : lore) {
			if(line.startsWith(key)) {
				return new ArrayList<>(Arrays.asList(line.replace(key, "").split(separator)));
			}
		}

		return Collections.emptyList();
	}

	/**
	 * Get the list of string linked to a specific key in the lore.
	 * Example: Professions:
	 *          * Enchanter
	 *          * Blacksmith
	 *
	 * @param itemStack the item
	 * @param key the key
	 * @param bullet the bullet used in the list
	 * @return the list of string being the value of the key
	 */
	public List<String> getStringListFromLore(@NotNull ItemStack itemStack, @NotNull String key, @NotNull String bullet) {
		final List<String> lore = getLore(itemStack);
		var i = -1;

		//Check for all the lore to see if one starts with the key
		for(final String line : lore) {
			final String cleanLine = ChatColor.stripColor(line);

			if(cleanLine.startsWith(key + ":")) {
				//Get the index of the start of the list
				i = lore.indexOf(line);
			}
		}
		//If none found, i = -1 or if no list exists after the 'key:'
		if(i == -1 || lore.size() == i + 1) {
			return new ArrayList<>();
		}
		final List<String> strings = new ArrayList<>();
		//Get an iterator starting at i
		final ListIterator<String> iterator = lore.listIterator(i + 1);

		while(iterator.hasNext()) {
			final String line = ChatColor.stripColor(iterator.next());

			//If the line starts with the bullet icon + a space, retrieve the data from this line
			if(line.startsWith(bullet + " ")) {
				final String data = line.replace(bullet + " ", "");
				strings.add(data);
			} else {
				//If the line does not start with it -> end of the list
				break;
			}
		}

		return strings;
	}

	/**
	 * Get the integer linked to a specific key in the lore.
	 * Example: Level: 5
	 *
	 * @param itemStack the item
	 * @param key the key
	 * @return the integer being the value of the key
	 */
	public int getIntFromLore(@NotNull ItemStack itemStack, @NotNull String key) {
		return getIntFromLore(itemStack, key, 0);
	}

	public int getIntFromLore(@NotNull ItemStack itemStack, @NotNull String key, final int defaultValue) {
		final String value = getStringFromLore(itemStack, key);

		if(!value.isBlank()) {
			try {
				return Integer.parseInt(getStringFromLore(itemStack, key));
			} catch (final NumberFormatException exception) {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * Get the double linked to a specific key in the lore.
	 * Example: Critical Chance: 1.25
	 *
	 * @param itemStack the item
	 * @param key the key
	 * @return the double being the value of the key
	 */
	public double getDoubleFromLore(@NotNull ItemStack itemStack, @NotNull String key) {
		return Double.parseDouble(getStringFromLore(itemStack, key));
	}

	public <T, Z> void setPersistentData(@NotNull ItemStack itemStack, @NotNull PersistentDataType<T, Z> persistentDataType,
								  @NotNull String key, @NotNull Z value) {

		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return;
		}
		meta.getPersistentDataContainer().set(new NamespacedKey(ItemLib.getPlugin(), key), persistentDataType, value);
		itemStack.setItemMeta(meta);
	}

	public void setPersistentString(@NotNull ItemStack itemStack, @NotNull String key, @NotNull String string) {
		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return;
		}
		meta.getPersistentDataContainer().set(new NamespacedKey(ItemLib.getPlugin(), key), PersistentDataType.STRING, string);
		itemStack.setItemMeta(meta);
	}

	/**
	 * Get the given persistent data from the item stack
	 *
	 * @param itemStack the itemStack
	 * @param persistentDataType the persistentDataType
	 * @param key the key
	 * @param <T> T
	 * @param <Z> Z
	 * @return the persistent data
	 */
	public <T, Z> Optional<Z> getPersistentData(@Nullable ItemStack itemStack, @NotNull PersistentDataType<T, Z> persistentDataType,
												@NotNull String key) {

		if(itemStack == null) {
			return Optional.empty();
		}
		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return Optional.empty();
		}
		final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		final var namespacedKey = new NamespacedKey(ItemLib.getPlugin(), key);

		if(dataContainer.has(namespacedKey, persistentDataType)) {
			return Optional.of(dataContainer.get(namespacedKey, persistentDataType));
		}

		return Optional.empty();
	}

	public Optional<String> getPersistentString(@NotNull ItemStack itemStack, @NotNull final String key) {
		return getPersistentData(itemStack, PersistentDataType.STRING, key);
	}

}
