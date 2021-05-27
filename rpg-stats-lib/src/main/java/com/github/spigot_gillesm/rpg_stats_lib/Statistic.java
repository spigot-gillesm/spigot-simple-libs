package com.github.spigot_gillesm.rpg_stats_lib;

import lombok.Getter;

public enum Statistic {

	ARMOR("Armor"),
	HEALTH("Health"),
	MANA("Mana"),
	ENERGY("Energy"),
	STAMINA("Stamina"),
	INTELLECT("Intellect"),
	STRENGTH("Strength"),
	SPIRIT("Spirit"),
	ATTACK_POWER("Attack Power"),
	SPELL_POWER("Spell Power"),
	HEALING_POWER("Healing Power"),
	FIRE_DAMAGE("Fire Damage"),
	WATER_DAMAGE("Water Damage"),
	FROST_DAMAGE("Frost Damage"),
	ICE_DAMAGE("Ice Damage"),
	NATURE_DAMAGE("Nature Damage"),
	SHADOW_DAMAGE("Shadow Damage"),
	EARTH_DAMAGE("Earth Damage"),
	LIGHTNING_DAMAGE("Lightning Damage"),
	HOLY_DAMAGE("Holy Damage"),
	WIND_DAMAGE("Wind Damage"),
	ARCANE_DAMAGE("Arcane Damage");

	@Getter
	private final String name;

	Statistic(final String name) {
		this.name = name;
	}

}
