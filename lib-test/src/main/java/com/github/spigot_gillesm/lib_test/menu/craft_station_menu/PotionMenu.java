package com.github.spigot_gillesm.lib_test.menu.craft_station_menu;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import org.bukkit.Material;
import org.bukkit.Sound;

public class PotionMenu extends CraftStationMenu {

	@Override
	protected SimpleItem getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.DRAGON_BREATH)
				.displayName("&5&lBrew")
				.build();
	}

	@Override
	protected String getTitle() {
		return "Brewing Stand";
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.BLOCK_BREWING_STAND_BREW;
	}

}
