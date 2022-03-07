package com.github.spigot_gillesm.lib_test.menu.craft_station_menu;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import org.bukkit.Material;
import org.bukkit.Sound;

public class EnchantMenu extends CraftStationMenu {

	@Override
	protected SimpleItem getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.ENDER_EYE)
				.displayName("&b&lEnchant")
				.build()
				.make();
	}

	@Override
	protected String getTitle() {
		return "Enchanting Table";
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.ENTITY_PLAYER_LEVELUP;
	}

}
