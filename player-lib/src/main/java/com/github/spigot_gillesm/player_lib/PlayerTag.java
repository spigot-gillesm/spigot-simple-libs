package com.github.spigot_gillesm.player_lib;

import lombok.Setter;

public enum PlayerTag {

	GAMEMODE(true),
	INVENTORY(false),
	LOCATION(true),
	CLASS(true),
	PROFESSION(true),
	LEVEL(true),
	EXPERIENCE(true),
	HEALTH(true),
	BUFFS(true),
	SPELLS(true),
	STATISTICS(false),
	MENU(false),
	OBJECTIVES(true),
	RECIPES(true),
	WORLDS(true),
	PARTY(true);

	@Setter
	private boolean saveOnQuit = false;

	PlayerTag(final boolean saveOnQuit) {
		this.saveOnQuit = saveOnQuit;
	}

	public boolean saveOnQuit() {
		return saveOnQuit;
	}

	public static boolean isTag(final String name) {
		try {
			PlayerTag.valueOf(name);
			return true;
		} catch(final IllegalArgumentException  e) {
			return false;
		}
	}

}
