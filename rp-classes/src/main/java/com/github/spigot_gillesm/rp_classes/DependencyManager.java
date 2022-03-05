package com.github.spigot_gillesm.rp_classes;

import io.lumine.mythic.lib.api.item.NBTItem;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@UtilityClass
public class DependencyManager {

	@Getter
	private boolean mmoItemsPresent = false;
	private MMOItems mmoItems;

	public Optional<ItemStack> getMmoItem(final String id) {
		if(!mmoItemsPresent) {
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

	public String getMmoItemsType(final ItemStack itemStack) {
		if(!mmoItemsPresent) {
			return null;
		}

		return NBTItem.get(itemStack).getType();
	}

	public boolean isMmoItem(final ItemStack itemStack) {
		if(!mmoItemsPresent) {
			return false;
		}

		return NBTItem.get(itemStack).hasType();
	}

	private void loadMmoItems() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MMOItems") != null) {
			mmoItems = MMOItems.plugin;
			mmoItemsPresent = true;
		}
	}

	public void loadDependencies() {
		loadMmoItems();
	}

}
