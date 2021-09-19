package com.github.spigot_gillesm.lib_test.craft;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.craft_item.BlacksmithCraftItem;
import com.github.spigot_gillesm.lib_test.craft.dynamic_craft.AnvilCraft;
import com.github.spigot_gillesm.lib_test.craft.dynamic_craft.ForgeCraft;
import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.*;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class CraftManager {

	public final ItemStack STEEL_INGOT = SimpleItem.newBuilder()
			.material(Material.IRON_INGOT)
			.displayName("&fLingot d'Acier")
			.build()
			.getItemStack();

	public final ItemStack SANCTIFIED_IRON_INGOT = SimpleItem.newBuilder()
			.material(Material.GOLD_INGOT)
			.displayName("&aLingot de Fer Sanctifié")
			.build()
			.getItemStack();

	public final ItemStack BLESSED_SANCTIFIED_IRON_INGOT = SimpleItem.newBuilder()
			.material(Material.GOLD_INGOT)
			.displayName("&2Lingot de Fer Sanctifié Béni")
			.build()
			.getItemStack();

	public final ItemStack PURIFIER = SimpleItem.newBuilder()
			.material(Material.POTION)
			.displayName("&aPurifiant")
			.potionType(PotionType.AWKWARD)
			.addPotionEffect(PotionEffectType.CONFUSION, 20*15, 0)
			.addPotionEffect(PotionEffectType.SLOW, 20*10, 0)
			.addPotionEffect(PotionEffectType.REGENERATION, 20*10, 0)
			.build().getItemStack();

	public final ItemStack ADRENALINE_POWDER = SimpleItem.newBuilder()
			.material(Material.CLAY_BALL)
			.displayName("&fAdrénaline")
			.build().getItemStack();

	public final ItemStack CATALYZER = SimpleItem.newBuilder()
			.material(Material.GUNPOWDER)
			.displayName("&aCataliseur")
			.lore("", "&fFabriqué par des chimistes", "&fou médecins et utilisé par", "&fles artisants")
			.addEnchantment(Enchantment.DURABILITY, 1)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.build().getItemStack();

	/*
	 * BLACKSMITH CRAFTS
	 */

	public final CraftItem STEEL_INGOT_CRAFT = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(ForgeMenu.class)
			.item(STEEL_INGOT)
			.amount(5)
			.reagent(Material.GUNPOWDER, 1)
			.setPatternItem(0, Material.IRON_INGOT)
			.setPatternItem(1, Material.CHARCOAL)
			.setPatternItem(2, Material.IRON_INGOT)
			.setPatternItem(3, Material.CHARCOAL)
			.setPatternItem(4, Material.IRON_INGOT)
			.setPatternItem(5, Material.CHARCOAL)
			.setPatternItem(6, Material.IRON_INGOT)
			.setPatternItem(7, Material.CHARCOAL)
			.setPatternItem(8, Material.IRON_INGOT)
			.dynamicCraft(ForgeCraft.newBuilder().build())
			.dynamicCraftMenu(DynamicForgeMenu.class)
			.build();

	public final CraftItem SANCTIFIED_IRON_INGOT_CRAFT = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(ForgeMenu.class)
			.item(SANCTIFIED_IRON_INGOT)
			.amount(6)
			.reagent(CATALYZER)
			.setPatternItem(0, Material.IRON_INGOT)
			.setPatternItem(1, Material.GOLD_INGOT)
			.setPatternItem(2, Material.IRON_INGOT)
			.setPatternItem(3, Material.GOLD_INGOT)
			.setPatternItem(4, Material.IRON_INGOT)
			.setPatternItem(5, Material.GOLD_INGOT)
			.setPatternItem(6, Material.IRON_INGOT)
			.setPatternItem(7, Material.GOLD_INGOT)
			.setPatternItem(8, Material.IRON_INGOT)
			.build();

	public final CraftItem STEEL_SWORD = BlacksmithCraftItem.newBuilder()
			.hardeningAmount(2)
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.IRON_SWORD)
					.displayName("&aEpée en Acier")
					.addEnchantment(Enchantment.DAMAGE_ALL, 1)
					.addEnchantment(Enchantment.DURABILITY, 1)
					.build()
					.getItemStack())
			.setPatternItem(1, STEEL_INGOT)
			.setPatternItem(4, STEEL_INGOT)
			.setPatternItem(7, Material.STICK)
			.dynamicCraft(AnvilCraft.newBuilder().build())
			.dynamicCraftMenu(DynamicAnvilMenu.class)
			.build();

	public final CraftItem STEEL_AXE = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.IRON_AXE)
					.displayName("&aHache en Acier")
					.addEnchantment(Enchantment.DAMAGE_ALL, 1)
					.addEnchantment(Enchantment.DURABILITY, 1)
					.build()
					.getItemStack())
			.setPatternItem(0, STEEL_INGOT)
			.setPatternItem(1, STEEL_INGOT)
			.setPatternItem(3, STEEL_INGOT)
			.setPatternItem(4, Material.STICK)
			.setPatternItem(7, Material.STICK)
			.build();

	public final CraftItem STEEL_HELMET = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.IRON_HELMET)
					.displayName("&aCasque en Acier")
					.addEnchantment(Enchantment.DURABILITY, 1)
					.setArmor(2.5, EquipmentSlot.HEAD)
					.setMovementSpeed(-0.002, EquipmentSlot.HEAD)
					.build()
					.getItemStack())
			.setPatternItem(0, STEEL_INGOT)
			.setPatternItem(1, STEEL_INGOT)
			.setPatternItem(2, STEEL_INGOT)
			.setPatternItem(3, STEEL_INGOT)
			.setPatternItem(5, STEEL_INGOT)
			.build();

	public final CraftItem STEEL_CHESTPLATE = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.IRON_CHESTPLATE)
					.displayName("&aPlastron en Acier")
					.addEnchantment(Enchantment.DURABILITY, 1)
					.setArmor(7, EquipmentSlot.CHEST)
					.setMovementSpeed(-0.005, EquipmentSlot.CHEST)
					.build()
					.getItemStack())
			.setPatternItem(0, STEEL_INGOT)
			.setPatternItem(2, STEEL_INGOT)
			.setPatternItem(3, STEEL_INGOT)
			.setPatternItem(4, STEEL_INGOT)
			.setPatternItem(5, STEEL_INGOT)
			.setPatternItem(6, STEEL_INGOT)
			.setPatternItem(7, STEEL_INGOT)
			.setPatternItem(8, STEEL_INGOT)
			.build();

	public final CraftItem STEEL_LEGGINGS = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.IRON_LEGGINGS)
					.displayName("&aJambières en Acier")
					.addEnchantment(Enchantment.DURABILITY, 1)
					.setArmor(5.5, EquipmentSlot.LEGS)
					.setMovementSpeed(-0.003, EquipmentSlot.LEGS)
					.build()
					.getItemStack())
			.setPatternItem(0, STEEL_INGOT)
			.setPatternItem(1, STEEL_INGOT)
			.setPatternItem(2, STEEL_INGOT)
			.setPatternItem(3, STEEL_INGOT)
			.setPatternItem(5, STEEL_INGOT)
			.setPatternItem(6, STEEL_INGOT)
			.setPatternItem(8, STEEL_INGOT)
			.build();

	public final CraftItem STEEL_BOOTS = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.IRON_BOOTS)
					.displayName("&aBottes en Acier")
					.addEnchantment(Enchantment.DURABILITY, 1)
					.setArmor(2, EquipmentSlot.FEET)
					.setMovementSpeed(-0.001, EquipmentSlot.FEET)
					.build()
					.getItemStack())
			.setPatternItem(0, STEEL_INGOT)
			.setPatternItem(2, STEEL_INGOT)
			.setPatternItem(3, STEEL_INGOT)
			.setPatternItem(5, STEEL_INGOT)
			.build();

	public final CraftItem COPPER_HELMET = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.LEATHER_HELMET)
					.displayName("&aCasque en Cuivre")
					.addEnchantment(Enchantment.DURABILITY, 4)
					.setArmor(2, EquipmentSlot.HEAD)
					.build()
					.getItemStack())
			.setPatternItem(0, Material.COPPER_INGOT)
			.setPatternItem(1, Material.COPPER_INGOT)
			.setPatternItem(2, Material.COPPER_INGOT)
			.setPatternItem(3, Material.COPPER_INGOT)
			.setPatternItem(5, Material.COPPER_INGOT)
			.build();

	public final CraftItem COPPER_CHESTPLATE = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.LEATHER_CHESTPLATE)
					.displayName("&aPlastron en Cuivre")
					.addEnchantment(Enchantment.DURABILITY, 4)
					.setArmor(5, EquipmentSlot.CHEST)
					.build()
					.getItemStack())
			.setPatternItem(0, Material.COPPER_INGOT)
			.setPatternItem(2, Material.COPPER_INGOT)
			.setPatternItem(3, Material.COPPER_INGOT)
			.setPatternItem(4, Material.COPPER_INGOT)
			.setPatternItem(5, Material.COPPER_INGOT)
			.setPatternItem(6, Material.COPPER_INGOT)
			.setPatternItem(7, Material.COPPER_INGOT)
			.setPatternItem(8, Material.COPPER_INGOT)
			.build();

	public final CraftItem COPPER_LEGGINGS = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.LEATHER_LEGGINGS)
					.displayName("&aJambières en Cuivre")
					.addEnchantment(Enchantment.DURABILITY, 4)
					.setArmor(4, EquipmentSlot.LEGS)
					.build()
					.getItemStack())
			.setPatternItem(0, Material.COPPER_INGOT)
			.setPatternItem(1, Material.COPPER_INGOT)
			.setPatternItem(2, Material.COPPER_INGOT)
			.setPatternItem(3, Material.COPPER_INGOT)
			.setPatternItem(5, Material.COPPER_INGOT)
			.setPatternItem(6, Material.COPPER_INGOT)
			.setPatternItem(8, Material.COPPER_INGOT)
			.build();

	public final CraftItem COPPER_BOOTS = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.LEATHER_BOOTS)
					.displayName("&aBottes en Cuivre")
					.addEnchantment(Enchantment.DURABILITY, 4)
					.setArmor(1, EquipmentSlot.FEET)
					.build()
					.getItemStack())
			.setPatternItem(0, Material.COPPER_INGOT)
			.setPatternItem(2, Material.COPPER_INGOT)
			.setPatternItem(3, Material.COPPER_INGOT)
			.setPatternItem(5, Material.COPPER_INGOT)
			.build();

	public final CraftItem SANCTIFIED_SWORD = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.GOLDEN_SWORD)
					.displayName("&2Epée en Fer Sanctifié")
					.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2)
					.addEnchantment(Enchantment.DAMAGE_ALL, 1)
					.addEnchantment(Enchantment.DURABILITY, 2)
					.build()
					.getItemStack())
			.setPatternItem(1, SANCTIFIED_IRON_INGOT)
			.setPatternItem(4, SANCTIFIED_IRON_INGOT)
			.setPatternItem(6, Material.STRING)
			.setPatternItem(7, Material.STICK)
			.setPatternItem(8, Material.STRING)
			.build();

	public final CraftItem SANCTIFIED_HELMET = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.GOLDEN_HELMET)
					.displayName("&aCasque en Fer Sanctifié")
					.addEnchantment(Enchantment.DURABILITY, 2)
					.addEnchantment(Enchantment.PROTECTION_FIRE, 1)
					.setArmor(2, EquipmentSlot.HEAD)
					.build()
					.getItemStack())
			.setPatternItem(0, SANCTIFIED_IRON_INGOT)
			.setPatternItem(1, SANCTIFIED_IRON_INGOT)
			.setPatternItem(2, SANCTIFIED_IRON_INGOT)
			.setPatternItem(3, SANCTIFIED_IRON_INGOT)
			.setPatternItem(5, SANCTIFIED_IRON_INGOT)
			.build();

	public final CraftItem SANCTIFIED_CHESTPLATE = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.GOLDEN_CHESTPLATE)
					.displayName("&aPlastron en Fer Sanctifié")
					.addEnchantment(Enchantment.DURABILITY, 2)
					.addEnchantment(Enchantment.PROTECTION_FIRE, 1)
					.setArmor(6, EquipmentSlot.CHEST)
					.build()
					.getItemStack())
			.setPatternItem(0, SANCTIFIED_IRON_INGOT)
			.setPatternItem(2, SANCTIFIED_IRON_INGOT)
			.setPatternItem(3, SANCTIFIED_IRON_INGOT)
			.setPatternItem(4, SANCTIFIED_IRON_INGOT)
			.setPatternItem(5, SANCTIFIED_IRON_INGOT)
			.setPatternItem(6, SANCTIFIED_IRON_INGOT)
			.setPatternItem(7, SANCTIFIED_IRON_INGOT)
			.setPatternItem(8, SANCTIFIED_IRON_INGOT)
			.build();

	public final CraftItem SANCTIFIED_LEGGINGS = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.GOLDEN_LEGGINGS)
					.displayName("&aJambières en Fer Sanctifié")
					.addEnchantment(Enchantment.DURABILITY, 2)
					.addEnchantment(Enchantment.PROTECTION_FIRE, 1)
					.setArmor(5, EquipmentSlot.LEGS)
					.build()
					.getItemStack())
			.setPatternItem(0, SANCTIFIED_IRON_INGOT)
			.setPatternItem(1, SANCTIFIED_IRON_INGOT)
			.setPatternItem(2, SANCTIFIED_IRON_INGOT)
			.setPatternItem(3, SANCTIFIED_IRON_INGOT)
			.setPatternItem(5, SANCTIFIED_IRON_INGOT)
			.setPatternItem(6, SANCTIFIED_IRON_INGOT)
			.setPatternItem(8, SANCTIFIED_IRON_INGOT)
			.build();

	public final CraftItem SANCTIFIED_BOOTS = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.GOLDEN_BOOTS)
					.displayName("&aBottes en Fer Sanctifié")
					.addEnchantment(Enchantment.DURABILITY, 2)
					.addEnchantment(Enchantment.PROTECTION_FIRE, 1)
					.setArmor(2, EquipmentSlot.FEET)
					.build()
					.getItemStack())
			.setPatternItem(0, SANCTIFIED_IRON_INGOT)
			.setPatternItem(2, SANCTIFIED_IRON_INGOT)
			.setPatternItem(3, SANCTIFIED_IRON_INGOT)
			.setPatternItem(5, SANCTIFIED_IRON_INGOT)
			.build();

	public final CraftItem BLESSED_SANCTIFIED_SWORD = CraftItem.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.craftingMenu(AnvilMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.GOLDEN_SWORD)
					.displayName("&3Epée en Fer Sanctifié Bénie")
					.addEnchantment(Enchantment.DAMAGE_UNDEAD, 3)
					.addEnchantment(Enchantment.DAMAGE_ALL, 2)
					.addEnchantment(Enchantment.DURABILITY, 3)
					.build()
					.getItemStack())
			.setPatternItem(0, Material.LAPIS_LAZULI)
			.setPatternItem(1, BLESSED_SANCTIFIED_IRON_INGOT)
			.setPatternItem(2, Material.LAPIS_LAZULI)
			.setPatternItem(3, Material.LAPIS_LAZULI)
			.setPatternItem(4, BLESSED_SANCTIFIED_IRON_INGOT)
			.setPatternItem(5, Material.LAPIS_LAZULI)
			.setPatternItem(6, Material.LEATHER)
			.setPatternItem(7, Material.BLAZE_ROD)
			.setPatternItem(8, Material.LEATHER)
			.build();

	/*
	 * ENCHANTER
	 */

	public final ItemStack EMPTY_EXPERIENCE_BOTTLE = SimpleItem.newBuilder()
			.material(Material.GLASS_BOTTLE)
			.displayName("&fEmpty Bottle o' Enchanting")
			.lore("", "&7Convert one level into a", "&7filled bottle o' enchanting")
			.addEnchantment(Enchantment.DURABILITY, 1)
			.addItemFlags(ItemFlag.HIDE_ENCHANTS)
			.build()
			.getItemStack();

	public final CraftItem EMPTY_EXPERIENCE_BOTTLE_CRAFT = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(EnchantMenu.class)
			.item(EMPTY_EXPERIENCE_BOTTLE)
			.setPatternItem(0, Material.LAPIS_LAZULI)
			.setPatternItem(1, Material.REDSTONE)
			.setPatternItem(2, Material.LAPIS_LAZULI)
			.setPatternItem(3, Material.REDSTONE)
			.setPatternItem(4, Material.GLASS_BOTTLE)
			.setPatternItem(5, Material.REDSTONE)
			.setPatternItem(6, Material.LAPIS_LAZULI)
			.setPatternItem(7, Material.REDSTONE)
			.setPatternItem(8, Material.LAPIS_LAZULI)
			.build();

	public final CraftItem SHARPNESS_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.DAMAGE_ALL, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.AMETHYST_SHARD)
			.setPatternItem(1, Material.AMETHYST_SHARD)
			.setPatternItem(2, Material.AMETHYST_SHARD)
			.setPatternItem(3, Material.AMETHYST_SHARD)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.AMETHYST_SHARD)
			.setPatternItem(6, Material.AMETHYST_SHARD)
			.setPatternItem(7, Material.AMETHYST_SHARD)
			.setPatternItem(8, Material.AMETHYST_SHARD)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	public final CraftItem PROTECTION_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.OBSIDIAN)
			.setPatternItem(1, Material.IRON_INGOT)
			.setPatternItem(2, Material.OBSIDIAN)
			.setPatternItem(3, Material.IRON_INGOT)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.IRON_INGOT)
			.setPatternItem(6, Material.OBSIDIAN)
			.setPatternItem(7, Material.IRON_INGOT)
			.setPatternItem(8, Material.OBSIDIAN)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	public final CraftItem FIRE_PROTECTION_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.PROTECTION_FIRE, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.BLAZE_POWDER)
			.setPatternItem(1, Material.SNOWBALL)
			.setPatternItem(2, Material.BLAZE_POWDER)
			.setPatternItem(3, Material.SNOWBALL)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.SNOWBALL)
			.setPatternItem(6, Material.BLAZE_POWDER)
			.setPatternItem(7, Material.SNOWBALL)
			.setPatternItem(8, Material.BLAZE_POWDER)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	public final CraftItem BLAST_PROTECTION_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.GUNPOWDER)
			.setPatternItem(1, STEEL_INGOT)
			.setPatternItem(2, Material.GUNPOWDER)
			.setPatternItem(3, STEEL_INGOT)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, STEEL_INGOT)
			.setPatternItem(6, Material.GUNPOWDER)
			.setPatternItem(7, STEEL_INGOT)
			.setPatternItem(8, Material.GUNPOWDER)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	public final CraftItem PROJECTILE_PROTECTION_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.ARROW)
			.setPatternItem(1, Material.LEATHER)
			.setPatternItem(2, Material.ARROW)
			.setPatternItem(3, Material.LEATHER)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.LEATHER)
			.setPatternItem(6, Material.ARROW)
			.setPatternItem(7, Material.LEATHER)
			.setPatternItem(8, Material.ARROW)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	public final CraftItem THORNS_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.THORNS, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.REDSTONE)
			.setPatternItem(1, SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.INSTANT_DAMAGE)
					.build().getItemStack())
			.setPatternItem(2, Material.REDSTONE)
			.setPatternItem(3, Material.SWEET_BERRIES)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.SWEET_BERRIES)
			.setPatternItem(6, Material.REDSTONE)
			.setPatternItem(7, SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.INSTANT_DAMAGE)
					.build().getItemStack())
			.setPatternItem(8, Material.REDSTONE)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	public final CraftItem KNOCKBACK_I = CraftItem.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.KNOCKBACK, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.FEATHER)
			.setPatternItem(1, Material.PISTON)
			.setPatternItem(2, Material.FEATHER)
			.setPatternItem(3, Material.PISTON)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.PISTON)
			.setPatternItem(6, Material.FEATHER)
			.setPatternItem(7, Material.PISTON)
			.setPatternItem(8, Material.FEATHER)
			.reagent(Material.EXPERIENCE_BOTTLE, 3)
			.build();

	/*
	 * DOCTOR CRAFTS
	 */

	public final CraftItem PURIFIER_CRAFT = CraftItem.newBuilder()
			.professionType(ProfessionType.DOCTOR)
			.craftingMenu(PotionMenu.class)
			.item(PURIFIER)
			.setPatternItem(0, Material.REDSTONE)
			.setPatternItem(1, Material.SUGAR)
			.setPatternItem(2, Material.REDSTONE)
			.setPatternItem(3, Material.LAPIS_LAZULI)
			.setPatternItem(4, SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.setPatternItem(5, Material.LAPIS_LAZULI)
			.setPatternItem(6, Material.REDSTONE)
			.setPatternItem(7, Material.SUGAR)
			.setPatternItem(8, Material.REDSTONE)
			.build();

	public final CraftItem ADRENALINE = CraftItem.newBuilder()
			.professionType(ProfessionType.DOCTOR)
			.craftingMenu(PotionMenu.class)
			.item(ADRENALINE_POWDER)
			.setPatternItem(0, Material.KELP)
			.setPatternItem(1, Material.FERMENTED_SPIDER_EYE)
			.setPatternItem(2, Material.KELP)
			.setPatternItem(3, Material.SUGAR)
			.setPatternItem(4, Material.GUNPOWDER)
			.setPatternItem(5, Material.SUGAR)
			.setPatternItem(6, Material.KELP)
			.setPatternItem(7, Material.FERMENTED_SPIDER_EYE)
			.setPatternItem(8, Material.KELP)
			.build();

	public final CraftItem CATALYZER_CRAFT = CraftItem.newBuilder()
			.professionType(ProfessionType.DOCTOR)
			.craftingMenu(PotionMenu.class)
			.item(CATALYZER)
			.setPatternItem(0, Material.REDSTONE)
			.setPatternItem(1, Material.GUNPOWDER)
			.setPatternItem(2, Material.REDSTONE)
			.setPatternItem(3, Material.GUNPOWDER)
			.setPatternItem(4, Material.FERMENTED_SPIDER_EYE)
			.setPatternItem(5, Material.GUNPOWDER)
			.setPatternItem(6, Material.REDSTONE)
			.setPatternItem(7, Material.GUNPOWDER)
			.setPatternItem(8, Material.REDSTONE)
			.build();

	/*
	 * PRIEST CRAFTS
	 */

	public final CraftItem SMITE_I = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.DAMAGE_UNDEAD, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.LAPIS_LAZULI)
			.setPatternItem(1, Material.BONE)
			.setPatternItem(2, Material.LAPIS_LAZULI)
			.setPatternItem(3, Material.BONE)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.BONE)
			.setPatternItem(6, Material.LAPIS_LAZULI)
			.setPatternItem(7, Material.BONE)
			.setPatternItem(8, Material.LAPIS_LAZULI)
			.reagent(Material.GOLD_NUGGET, 3)
			.build();

	public final CraftItem SPIDER_I = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.LAPIS_LAZULI)
			.setPatternItem(1, Material.SPIDER_EYE)
			.setPatternItem(2, Material.LAPIS_LAZULI)
			.setPatternItem(3, Material.SPIDER_EYE)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.SPIDER_EYE)
			.setPatternItem(6, Material.LAPIS_LAZULI)
			.setPatternItem(7, Material.SPIDER_EYE)
			.setPatternItem(8, Material.LAPIS_LAZULI)
			.reagent(Material.GOLD_NUGGET, 3)
			.build();

	public final CraftItem MENDING = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.MENDING, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.NETHERITE_INGOT)
			.setPatternItem(1, Material.GOLD_BLOCK)
			.setPatternItem(2, Material.DIAMOND)
			.setPatternItem(3, Material.GOLD_BLOCK)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.GOLD_BLOCK)
			.setPatternItem(6, Material.DIAMOND)
			.setPatternItem(7, Material.GOLD_BLOCK)
			.setPatternItem(8, Material.NETHERITE_INGOT)
			.reagent(Material.GOLD_NUGGET, 15)
			.build();

	public final CraftItem BINDING = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.BINDING_CURSE, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.SLIME_BALL)
			.setPatternItem(1, Material.LEAD)
			.setPatternItem(2, Material.SLIME_BALL)
			.setPatternItem(3, Material.STRING)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.STRING)
			.setPatternItem(6, Material.SLIME_BALL)
			.setPatternItem(7, Material.LEAD)
			.setPatternItem(8, Material.SLIME_BALL)
			.reagent(Material.GOLD_NUGGET, 9)
			.build();

	public final CraftItem SOUL_SPEED_I = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.SOUL_SPEED, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.SOUL_SAND)
			.setPatternItem(1, Material.FEATHER)
			.setPatternItem(2, Material.SOUL_SAND)
			.setPatternItem(3, Material.FEATHER)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.FEATHER)
			.setPatternItem(6, Material.SOUL_SAND)
			.setPatternItem(7, Material.FEATHER)
			.setPatternItem(8, Material.SOUL_SAND)
			.reagent(Material.GOLD_NUGGET, 6)
			.build();

	public final CraftItem FROST_WALK_I = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.FROST_WALKER, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.ICE)
			.setPatternItem(1, Material.FEATHER)
			.setPatternItem(2, Material.ICE)
			.setPatternItem(3, Material.FEATHER)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.FEATHER)
			.setPatternItem(6, Material.ICE)
			.setPatternItem(7, Material.FEATHER)
			.setPatternItem(8, Material.ICE)
			.reagent(Material.GOLD_NUGGET, 6)
			.build();

	public final CraftItem LOOTING_I = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.GOLD_INGOT)
			.setPatternItem(1, Material.EMERALD)
			.setPatternItem(2, Material.GOLD_INGOT)
			.setPatternItem(3, Material.LAPIS_LAZULI)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.LAPIS_LAZULI)
			.setPatternItem(6, Material.GOLD_INGOT)
			.setPatternItem(7, Material.EMERALD)
			.setPatternItem(8, Material.GOLD_INGOT)
			.reagent(Material.GOLD_NUGGET, 6)
			.build();

	public final CraftItem VANISHING = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.VANISHING_CURSE, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.PHANTOM_MEMBRANE)
			.setPatternItem(1, Material.ENDER_PEARL)
			.setPatternItem(2, Material.PHANTOM_MEMBRANE)
			.setPatternItem(3, Material.ENDER_PEARL)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.ENDER_PEARL)
			.setPatternItem(6, Material.PHANTOM_MEMBRANE)
			.setPatternItem(7, Material.ENDER_PEARL)
			.setPatternItem(8, Material.PHANTOM_MEMBRANE)
			.reagent(Material.GOLD_NUGGET, 9)
			.build();

	public final CraftItem FIRE_ASPECT_I = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.ENCHANTED_BOOK)
					.displayName("&eEnchanted Book")
					.addEnchantment(Enchantment.FIRE_ASPECT, 1)
					.build().getItemStack())
			.setPatternItem(0, Material.BLAZE_POWDER)
			.setPatternItem(1, Material.GUNPOWDER)
			.setPatternItem(2, Material.BLAZE_POWDER)
			.setPatternItem(3, Material.REDSTONE)
			.setPatternItem(4, Material.BOOK)
			.setPatternItem(5, Material.REDSTONE)
			.setPatternItem(6, Material.BLAZE_POWDER)
			.setPatternItem(7, Material.GUNPOWDER)
			.setPatternItem(8, Material.BLAZE_POWDER)
			.reagent(Material.GOLD_NUGGET, 9)
			.build();

	public final CraftItem BLESSED_WATER = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(SimpleItem.newBuilder()
					.material(Material.POTION)
					.displayName("&bEau Bénite")
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.setPatternItem(1, Material.GLOWSTONE_DUST)
			.setPatternItem(3, Material.GLOWSTONE_DUST)
			.setPatternItem(4, SimpleItem.newBuilder()
					.material(Material.POTION)
					.potionType(PotionType.WATER)
					.build().getItemStack())
			.setPatternItem(5, Material.GLOWSTONE_DUST)
			.setPatternItem(7, Material.GLOWSTONE_DUST)
			.reagent(Material.GOLD_NUGGET, 3)
			.build();


	public final CraftItem BLESSED_SANCTIFIED_INGOT = CraftItem.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.craftingMenu(LecternMenu.class)
			.item(BLESSED_SANCTIFIED_IRON_INGOT)
			.setPatternItem(0, SANCTIFIED_IRON_INGOT)
			.setPatternItem(1, SANCTIFIED_IRON_INGOT)
			.setPatternItem(2, SANCTIFIED_IRON_INGOT)
			.setPatternItem(3, SANCTIFIED_IRON_INGOT)
			.setPatternItem(4, BLESSED_WATER.getItem())
			.setPatternItem(5, SANCTIFIED_IRON_INGOT)
			.setPatternItem(6, SANCTIFIED_IRON_INGOT)
			.setPatternItem(7, SANCTIFIED_IRON_INGOT)
			.setPatternItem(8, SANCTIFIED_IRON_INGOT)
			.reagent(Material.GOLD_NUGGET, 3)
			.build();

	public List<CraftItem> getCraftItems() {
		final var fields = Arrays.stream(CraftManager.class.getDeclaredFields())
				.filter(f -> CraftItem.class.isAssignableFrom(f.getType()))
				.collect(Collectors.toList());
		final List<CraftItem> items = new ArrayList<>();

		for(final var field : fields) {
			try {
				final var item = (CraftItem) field.get(CraftManager.class);
				items.add(item);
			} catch (IllegalAccessException e) {
				Formatter.error("Error retrieving craft items from CraftManager");
			}
		}

		return items;
	}

	public Optional<BlacksmithCraftItem> getBlacksmithCraftItem(final ItemStack itemStack) {
		final var items = getCraftItems().stream()
				.filter(BlacksmithCraftItem.class::isInstance)
				.collect(Collectors.toList());
		for(final CraftItem item : items) {
			final var blacksmithItem = (BlacksmithCraftItem) item;
			if(blacksmithItem.isSimilar(itemStack)) {
				return Optional.of(blacksmithItem);
			}
		}

		return Optional.empty();
	}

	public Optional<CraftItem> getCraftItem(final String id) {
		final var fields = Arrays.stream(CraftManager.class.getDeclaredFields())
				.filter(f -> CraftItem.class.isAssignableFrom(f.getType()))
				.collect(Collectors.toList());

		for(final var field : fields) {
			if(field.getName().equalsIgnoreCase(id)) {
				try {
					return Optional.of((CraftItem) field.get(CraftManager.class));
				} catch (IllegalAccessException e) {
					Formatter.error("Error retrieving craft items from CraftManager");
				}
			}
		}

		return Optional.empty();
	}

	public Optional<CraftItem> getCraftItem(final ItemStack itemStack) {
		for(final var craftItem : getCraftItems()) {
			if(craftItem.isSimilar(itemStack)) {
				return Optional.of(craftItem);
			}
		}

		return Optional.empty();
	}

	public List<CraftItem> getItemsFromProfession(final ProfessionType professionType) {
		return getCraftItems().stream()
				.filter(c -> c.getProfessionType() == professionType)
				.collect(Collectors.toList());
	}

	public Optional<CraftItem> getItemFromPattern(final ItemStack[] pattern) {
		for(final var item : getCraftItems()) {
			if(item.isPatternMatching(pattern)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

}
