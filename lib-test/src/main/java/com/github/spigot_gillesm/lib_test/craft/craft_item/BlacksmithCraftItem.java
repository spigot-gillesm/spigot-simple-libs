package com.github.spigot_gillesm.lib_test.craft.craft_item;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.craft.CraftItem;
import com.github.spigot_gillesm.lib_test.craft.DynamicCraftItem;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import com.google.common.collect.HashMultimap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlacksmithCraftItem extends CraftItem implements DynamicCraftItem {

	//The amount of time the item must be hardened (= put in water)
	private final int hardeningAmount;

	private BlacksmithCraftItem(final Builder builder) {
		super(builder);

		this.hardeningAmount = builder.hardeningAmount;
	}

	@Override
	public ItemStack getResult() {
		//Get the original result
		final var item = super.getResult().clone();
		final var meta = item.getItemMeta();

		if(meta == null) {
			return null;
		}
		final List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		//Set the hardening data
		lore.add(0, Formatter.colorize("&cHot"));
		lore.add(0, Formatter.colorize("&cHardening: 1/" + hardeningAmount));
		lore.add(0, "");
		meta.setLore(lore);
		meta.setLocalizedName("CANCEL_USE");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);

		return item;
	}

	public ItemStack harden(final ItemStack itemStack, final boolean isHot) {
		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return null;
		}
		final var lore = meta.getLore();

		//Check if the item is being hardened
		if(lore != null && lore.size() >= 3 && "".equals(lore.get(0))
				&& ChatColor.stripColor(lore.get(1)).startsWith("Hardening: ")
				&& "CANCEL_USE".equals(meta.getLocalizedName())) {
			final var line = ChatColor.stripColor(lore.get(1));
			//The split -> " <x>/<hardeningAmount>". charAt(0) -> '<x>'
			final int amount = Integer.parseInt(String.valueOf(line.split(": ")[1].charAt(0)));

			//Check if the item is about to be hardened enough
			if(amount == hardeningAmount && !isHot) {
				return super.getResult();
			} else {
				//Clone the data
				final var newItem = itemStack.clone();
				final var newMeta = newItem.getItemMeta();
				final var newLore = new ArrayList<>(lore);
				//Update the hardening data
				final var hardenData = "&cHardening: " + (amount + 1) + "/" + hardeningAmount;

				if(isHot) {
					newLore.set(2, Formatter.colorize("&cHot"));
				} else {
					//Only increase harden amount if the item hot -> cold (= !isHot)
					newLore.set(1, Formatter.colorize(hardenData));
					newLore.set(2, Formatter.colorize("&bCold"));
				}
				newMeta.setLore(newLore);
				newMeta.setAttributeModifiers(HashMultimap.create());
				newItem.setItemMeta(newMeta);

				return newItem;
			}
		}

		return null;
	}

	@Override
	public boolean reWork(final Player player, final ItemStack itemStack) {
		//If the item is already finished (= the result from the super class), do nothing
		//If the item isn't cold, do nothing
		if(itemStack.hasItemMeta() && itemStack.getItemMeta().getLore().contains(Formatter.colorize("&bCold"))
				&& !itemStack.isSimilar(super.getResult())) {
			final var menu = new DynamicAnvilMenu();
			menu.setCraftRunnable(getDynamicCraft().start(player, menu));
			menu.setResult(harden(itemStack, true));
			menu.display(player);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isSimilar(final ItemStack itemStack) {
		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return false;
		}
		final var lore = meta.getLore();

		//Check if the item is being hardened
		if(lore != null && lore.size() >= 2 && "".equals(lore.get(0))
				&& ChatColor.stripColor(lore.get(1)).startsWith("Hardening: ")) {
			final List<String> newLore = new ArrayList<>(lore);
			//Remove the 2 first lines for further comparison
			newLore.remove(0);
			newLore.remove(0);

			//Copy the item and the lore
			final var newItem = itemStack.clone();
			final var newMeta = meta.clone();
			//Set the lore of the copied item to the updated lore
			meta.setLore(newLore);
			newItem.setItemMeta(newMeta);

			//Return whether the item without the hardening data is similar or not
			return newItem.isSimilar(itemStack);
		} else {
			//If there are no hardening data attach to the item -> compare the items
			return getItem().isSimilar(itemStack);
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder extends CraftItem.Builder {

		private int hardeningAmount = 1;

		public Builder hardeningAmount(final int hardeningAmount) {
			this.hardeningAmount = hardeningAmount;
			return this;
		}

		@Override
		public BlacksmithCraftItem build() {
			if(hardeningAmount < 1) {
				throw new IllegalArgumentException("The hardening amount cannot be smaller than 1");
			}
			return new BlacksmithCraftItem(this);
		}

	}

}
