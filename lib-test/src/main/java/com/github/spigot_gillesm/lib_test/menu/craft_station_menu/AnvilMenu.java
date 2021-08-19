package com.github.spigot_gillesm.lib_test.menu.craft_station_menu;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;

public class AnvilMenu extends CraftStationMenu {

	@Override
	protected SimpleItem getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.IRON_SHOVEL)
				.displayName("&b&lForge")
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build();
	}

	@Override
	protected String getTitle() {
		return "Anvil";
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.BLOCK_ANVIL_LAND;
	}

}
