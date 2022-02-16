package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemUtil {

	/**
	 * Reduce the item amount by one
	 *
	 * @param itemStack the item
	 * @return true if the current amount is equal or smaller than 1 (the item should disappear)
	 */
	public boolean decrementItemAmount(@NotNull final ItemStack itemStack) {
		if(itemStack.getAmount() <= 1) {
			return true;
		} else {
			itemStack.setAmount(itemStack.getAmount() - 1);
			return false;
		}
	}

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

	public boolean hasLineInLore(@NotNull final ItemStack itemStack, @NotNull final String line) {
		return getLore(itemStack).contains(Formatter.colorize(line));
	}

	public boolean hasStringInLore(@NotNull final ItemStack itemStack, @NotNull final String string) {
		return hasStringInLore(itemStack, string, false);
	}

	public boolean hasStringInLore(@NotNull final ItemStack itemStack, @NotNull final String string, final boolean checkColor) {
		for(final var line : getLore(itemStack)) {
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
		return getIntFromLore(itemStack, key, 0);
	}

	public int getIntFromLore(@NotNull final ItemStack itemStack, @NotNull final String key, final int defaultValue) {
		final var value = getStringFromLore(itemStack, key);

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
	public double getDoubleFromLore(@NotNull final ItemStack itemStack, @NotNull final String key) {
		return Double.parseDouble(getStringFromLore(itemStack, key));
	}

}
