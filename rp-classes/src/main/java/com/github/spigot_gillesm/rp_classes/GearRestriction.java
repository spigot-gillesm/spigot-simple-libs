package com.github.spigot_gillesm.rp_classes;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Set;

/**
 * This class represents the gear restrictions an RPClass has
 */
public enum GearRestriction {

	LEATHER_ARMOR("Leather Armor", Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
			Material.LEATHER_BOOTS),
	CHAINMAIL_ARMOR("Chainmail Armor", Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS,
			Material.CHAINMAIL_BOOTS),
	GOLDEN_ARMOR("Golden Armor", Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS),
	IRON_ARMOR("Iron Armor", Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS),
	DIAMOND_ARMOR("Diamond Armor", Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
			Material.DIAMOND_BOOTS),
	NETHERITE_ARMOR("Netherite Armor", Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS,
			Material.NETHERITE_BOOTS),
	ARMOR("Armor"),
	LEATHER("Leather"),
	MAIL("Mail"),
	PLATE("Plate"),
	HEAVY_PLATE("Heavy Plate"),
	WOODEN_WEAPON("Wooden Weapon", Material.WOODEN_SWORD, Material.WOODEN_AXE),
	STONE_WEAPON("Stone Weapon", Material.STONE_SWORD, Material.STONE_AXE),
	GOLDEN_WEAPON("Golden Weapon", Material.GOLDEN_SWORD, Material.GOLDEN_AXE),
	IRON_WEAPON("Iron Weapon", Material.IRON_SWORD, Material.IRON_AXE),
	DIAMOND_WEAPON("Diamond Weapon", Material.DIAMOND_SWORD, Material.DIAMOND_AXE),
	NETHERITE_WEAPON("Netherite Weapon", Material.NETHERITE_SWORD, Material.NETHERITE_AXE),
	SHIELD("Shield", Material.SHIELD),
	GREATSHIELD("Greatshield"),
	BOW("Bow", Material.BOW),
	CROSSBOW("Crossbow", Material.CROSSBOW),
	SWORD("Sword"),
	GREATSWORD("Greatsword"),
	AXE("Axe"),
	GREATAXE("Greataxe"),
	HALBERD("Halberd"),
	DAGGER("Dagger"),
	LANCE("Lance"),
	HAMMER("Hammer"),
	GREATHAMMER("Greathammer"),
	WAND("Wand"),
	STAFF("Staff"),
	GREATBOW("Greatbow"),
	MUSKET("Musket");

	@Getter
	private final String displayName;

	@Getter
	private final Set<Material> materials;

	GearRestriction(final String displayName, final Material... materials) {
		this.displayName = displayName;
		this.materials = Set.of(materials);
	}

}
