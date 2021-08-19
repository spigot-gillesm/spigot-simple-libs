package com.github.spigot_gillesm.lib_test;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class ItemManager {

	public boolean decrementItemAmount(final ItemStack item) {
		if(item.getAmount() <= 1) {
			return true;
		} else {
			item.setAmount(item.getAmount() - 1);
			return false;
		}
	}

}
