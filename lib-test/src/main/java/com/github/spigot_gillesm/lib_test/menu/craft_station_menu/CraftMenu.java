package com.github.spigot_gillesm.lib_test.menu.craft_station_menu;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;

public class CraftMenu extends CraftStationMenu {

	@Override
	protected SimpleItem getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.LEATHER)
				.displayName("&a&lCraft")
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build()
				.make();
	}

	@Override
	protected String getTitle() {
		return "Crafting Station";
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.UI_LOOM_TAKE_RESULT;
	}

}
