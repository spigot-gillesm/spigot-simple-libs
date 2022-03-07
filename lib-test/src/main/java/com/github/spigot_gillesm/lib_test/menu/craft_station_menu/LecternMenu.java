package com.github.spigot_gillesm.lib_test.menu.craft_station_menu;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import org.bukkit.Material;
import org.bukkit.Sound;

public class LecternMenu extends CraftStationMenu {

	@Override
	protected SimpleItem getCraftIcon() {
		return SimpleItem.newBuilder()
				.material(Material.ENCHANTED_BOOK)
				.displayName("&e&lBless")
				.build()
				.make();
	}

	@Override
	protected String getTitle() {
		return "Lectern";
	}

	@Override
	protected Sound getCraftSound() {
		return Sound.ENTITY_ILLUSIONER_MIRROR_MOVE;
	}

}
