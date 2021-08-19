package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum ProfessionType {

	BLACKSMITH(ForgeMenu.class, AnvilMenu.class),
	ENCHANTER(EnchantMenu.class),
	ALCHEMIST(PotionMenu.class),
	DOCTOR(PotionMenu.class),
	PRIEST(LecternMenu.class),
	JOURNALIST,
	FARMER,
	BUTCHER,
	BARMAN(PotionMenu.class),
	POSTMAN,
	POLICEMAN,
	PROSTITUTE,
	ENGINEER;

	@Getter
	private final Class<? extends SimpleMenu>[] menus;

	@SafeVarargs
	ProfessionType(final Class<? extends SimpleMenu>... menus) {
		this.menus = menus;
	}

	public static ProfessionType getProfessionType(@NotNull final String professionType) {
		try {
			return ProfessionType.valueOf(professionType.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

}
