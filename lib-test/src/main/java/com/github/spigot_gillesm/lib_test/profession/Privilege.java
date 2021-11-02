package com.github.spigot_gillesm.lib_test.profession;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Privilege {

	/*LEATHER_ARMORS,
  	IRON_ARMORS,
  	GOLDEN_ARMORS,
  	DIAMOND_ARMORS,
  	NETHERITE_ARMORS,
  	STONE_WEAPONS,
  	IRON_WEAPONS,
  	GOLDEN_WEAPONS,
  	DIAMOND_WEAPONS,
  	NETHERITE_WEAPONS,*/
	ARMORS,
	WEAPONS,
	ANIMALS,
	MONSTERS,
	WHEAT;

	@Nullable
	public static Privilege getPrivilege(@NotNull final String privilege) {
		try {
			return Privilege.valueOf(privilege.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

}
