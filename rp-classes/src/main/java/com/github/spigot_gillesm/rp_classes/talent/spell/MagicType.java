package com.github.spigot_gillesm.rp_classes.talent.spell;

import lombok.Getter;

public enum MagicType {

	HOLY("&e");

	@Getter
	private final String colorCode;

	MagicType(final String colorCode) {
		this.colorCode = colorCode;
	}

}
