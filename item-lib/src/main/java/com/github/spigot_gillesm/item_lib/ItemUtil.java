package com.github.spigot_gillesm.item_lib;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemUtil {

	/**
	 * Get the item's lore.
	 *
	 * @param itemStack the item
	 * @return a list of string containing the lore
	 */
	public List<String> getLore(@NotNull final ItemStack itemStack) {
		if(!itemStack.hasItemMeta()) {
			return new ArrayList<>();
		} else {
			final var meta = itemStack.getItemMeta();

			return meta.hasLore() ? meta.getLore() : new ArrayList<>();
		}
	}

	/**
	 * Get the string linked to a specific key in the lore.
	 * Example: Profession: Enchanter
	 *
	 * @param itemStack the item
	 * @param key the key
	 * @return the string being the value of the key
	 */
	public String getStringFromLore(@NotNull final ItemStack itemStack, @NotNull final String key) {
		for(final var line : getLore(itemStack)) {
			final var cleanLine = ChatColor.stripColor(line);
			if(cleanLine.startsWith(key + ": ")) {
				final var data = cleanLine.split(": ");

				if(data.length > 1) {
					return ChatColor.stripColor(data[1]);
				}
			}
		}

		return "";
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
	public List<String> getStringListFromLore(@NotNull final ItemStack itemStack, @NotNull final String key,
										@NotNull final String bullet) {

		final var lore = getLore(itemStack);
		var i = -1;

		//Check for all the lore to see if one starts with the key
		for(final var line : lore) {
			final var data = ChatColor.stripColor(line);

			if(data.startsWith(key + ":")) {
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
		final var iterator = lore.listIterator(i + 1);

		while(iterator.hasNext()) {
			final var line = ChatColor.stripColor(iterator.next());

			//If the line starts with the bullet icon + a space, retrieve the data from this line
			if(line.startsWith(bullet + " ")) {
				final var data = line.replace(bullet + " ", "");
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
	public int getIntFromLore(@NotNull final ItemStack itemStack, @NotNull final String key) {
		return Integer.parseInt(getStringFromLore(itemStack, key));
	}

	/**
	 * Get the double linked to a specific key in the lore.
	 * Example: Critical Chance: 1.25
	 *
	 * @param itemStack the item
	 * @param key the key
	 * @return the double being the value of the key
	 */
	public double getDoubleFromLore(@NotNull final ItemStack itemStack, @NotNull final String key) {
		return Double.parseDouble(getStringFromLore(itemStack, key));
	}

}
