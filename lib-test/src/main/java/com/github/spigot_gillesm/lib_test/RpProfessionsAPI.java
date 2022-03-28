package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.item.ItemManager;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RpProfessionsAPI {

	private static RpProfessionsAPI instance;

	private RpProfessionsAPI() { }

	public Optional<ItemStack> getItem(@NotNull final String id) {
		return ItemManager.getItem(id).map(SimpleItem::getItemStack).or(Optional::empty);
	}

	public static RpProfessionsAPI getInstance() {
		if(instance == null) {
			RpProfessionsAPI.instance = new RpProfessionsAPI();
		}

		return instance;
	}

}
