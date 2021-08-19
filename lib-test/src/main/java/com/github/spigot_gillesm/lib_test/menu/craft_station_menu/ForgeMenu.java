package com.github.spigot_gillesm.lib_test.menu.craft_station_menu;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import org.bukkit.Material;
import org.bukkit.Sound;

public class ForgeMenu extends CraftStationMenu {

	@Override
	protected SimpleItem getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.GOLD_INGOT)
				.displayName("&c&lSmelt")
				.build();
	}

	@Override
	protected String getTitle() {
		return "Forge";
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.BLOCK_LAVA_EXTINGUISH;
	}

}
