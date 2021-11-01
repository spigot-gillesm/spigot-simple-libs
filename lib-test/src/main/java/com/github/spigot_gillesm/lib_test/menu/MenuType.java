package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.*;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicEnchantMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum MenuType {

	FORGE(ForgeMenu.class, DynamicForgeMenu.class),
	ANVIL(AnvilMenu.class, DynamicAnvilMenu.class),
	ENCHANTMENT(EnchantMenu.class, DynamicEnchantMenu.class),
	LECTERN(LecternMenu.class, DynamicEnchantMenu.class),
	POTION(PotionMenu.class, null);

	@Getter
	private final Class<? extends CraftStationMenu> menu;

	@Getter
	private final Class<? extends DynamicCraftMenu> dynamicMenu;

	MenuType(final Class<? extends CraftStationMenu> menu, final Class<? extends DynamicCraftMenu> dynamicMenu) {
		this.menu = menu;
		this.dynamicMenu = dynamicMenu;
	}

	public static MenuType getMenuType(@NotNull final String menuType) {
		try {
			return MenuType.valueOf(menuType.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

}
