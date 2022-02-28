package com.github.spigot_gillesm.lib_test;

import lombok.experimental.UtilityClass;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@UtilityClass
public class DependencyManager {

	private MMOItems mmoItems;

	public Optional<ItemStack> getMmoItem(final String id) {
		if(mmoItems == null) {
			return Optional.empty();
		}
		for(final var type : mmoItems.getTypes().getAll()) {
			final MMOItem mmoItem = mmoItems.getMMOItem(type, id);

			if(mmoItem != null) {
				final ItemStack itemStack = mmoItem.newBuilder().build();

				if(itemStack != null) {
					return Optional.of(itemStack);
				}
			}
		}

		return Optional.empty();
	}

	private void loadMmoItems() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MMOItems") != null) {
			mmoItems = MMOItems.plugin;
		}
	}

	public void loadDependencies() {
		loadMmoItems();
	}

}
