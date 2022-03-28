package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class YamlItem {

	public final ConfigurationSection configuration;

	private YamlItem(final ConfigurationSection configuration) {
		this.configuration = configuration;
	}

	private YamlItem(final File file) {
		this(FileUtils.getConfiguration(file));
	}

	private YamlItem(final String path) {
		this(FileUtils.getResource(path));
	}

	public SimpleItem.Builder getBuilderFromFile(@NotNull final String id) {
		if(!configuration.isConfigurationSection(id)) {
			throw new IllegalArgumentException("Id " + id + " doesn't exist in this context.");
		}
		final var section = configuration.getConfigurationSection(id);
		final var builder = SimpleItem.newBuilder();
		var material = Material.WOODEN_SWORD;

		if(section.contains("material")) {
			final var strMaterial = section.getString("material");
			try {
				material = Material.valueOf(strMaterial.toUpperCase());
				builder.material(material);
			} catch(final IllegalArgumentException e) {
				Formatter.error("Invalid material name for " + id + ": " + strMaterial + ".");
			}
		} else {
			Formatter.warning(String.format("The material data in the configuration section %s is not defined", id));
		}
		if(section.contains("display-name")) {
			builder.displayName(section.getString("display-name"));
		}
		if(section.contains("localized-name")) {
			builder.localizedName(section.getString("localized-name"));
		}
		if(section.contains("lore")) {
			builder.lore(section.getStringList("lore"));
		}
		if(section.contains("custom-model-data")) {
			builder.customModelData(section.getInt("custom-model-data"));
		}
		if(section.isConfigurationSection("enchantments")) {
			final var enchants = section.getConfigurationSection("enchantments");

			for(final var key : enchants.getKeys(false)) {
				try {
					builder.addEnchantment(Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase())), enchants.getInt(key));
				} catch(final IllegalArgumentException e) {
					Formatter.error("Invalid enchantment name for " + id + ": " + key + ".");
				}
			}
		}
		if(section.contains("item-flags")) {
			for(final var flag : section.getStringList("item-flags")) {
				try {
					builder.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
				} catch(final IllegalArgumentException e) {
					Formatter.error("Invalid item flag name for " + id + ": " + flag + ".");
				}
			}
		}
		if(section.contains("potion-type")) {
			final var type = section.getString("potion-type");
			try {
				builder.potionType(PotionType.valueOf(type.toUpperCase()));
			} catch(final IllegalArgumentException e) {
				Formatter.error("Invalid potion type name for " + id + ": " + type + ".");
			}
		}
		if(section.contains("extended")) {
			builder.extended(section.getBoolean("extended"));
		}
		if(section.contains("upgraded")) {
			builder.upgraded(section.getBoolean("upgraded"));
		}
		if(section.isConfigurationSection("potion-effects")) {
			//Contains all the effects
			final var effectsSection = section.getConfigurationSection("potion-effects");

			for(final var key : effectsSection.getKeys(false)) {
				//Contains the data of one effect
				final var effectSection = effectsSection.getConfigurationSection(key);

				if(!effectSection.contains("duration") && !effectSection.contains("amplifier")) {
					Formatter.error("Invalid potion effect data for " + id + ": All effects must have an amplifier or a duration set.");
				}
				builder.addPotionEffect(PotionEffectType.getByName(key.toUpperCase()),
						(int) effectSection.getDouble("duration", 0) * 20, effectSection.getInt("amplifier", 0));
			}
		}
		if(section.isConfigurationSection("color")) {
			final var colorSection = section.getConfigurationSection("color");
			builder.color(colorSection.getInt("r", 0), colorSection.getInt("g", 0),colorSection.getInt("b", 0));
		}
		var slot = EquipmentSlot.HAND;

		if(material.name().contains("SHIELD")) {
			slot = EquipmentSlot.OFF_HAND;
		} else if(material.name().contains("HELMET")) {
			slot = EquipmentSlot.HEAD;
		} else if(material.name().contains("CHESTPLATE") || material.name().contains("ELYTRA")) {
			slot = EquipmentSlot.CHEST;
		} else if(material.name().contains("LEGGINGS")) {
			slot = EquipmentSlot.LEGS;
		} else if(material.name().contains("BOOTS")) {
			slot = EquipmentSlot.FEET;
		}

		if(section.contains("armor")) {
			builder.setArmor(section.getDouble("armor"), slot);
		}
		if(section.contains("movement-speed")) {
			builder.setMovementSpeed(section.getDouble("movement-speed"), slot);
		}
		if(section.contains("max-health")) {
			builder.setMaxHealth(section.getDouble("max-health"), slot);
		}
		if(section.contains("armor-toughness")) {
			builder.setArmorToughness(section.getDouble("armor-toughness"), slot);
		}
		if(section.contains("attack-speed")) {
			builder.setAttackSpeed(section.getDouble("attack-speed") - 4, slot);
		}
		if(section.contains("attack-damage")) {
			builder.setAttackDamage(section.getDouble("attack-damage"), slot);
		}
		if(section.contains("knockback-resistance")) {
			builder.setKnockbackResistance(section.getDouble("knockback-resistance"), slot);
		}

		return builder;
	}

	public SimpleItem getItemFromFile(@NotNull final String id) {
		return getBuilderFromFile(id).build();
	}

	public SimpleItem.Builder getBuilderFromFile() {
		return getBuilderFromFile("");
	}

	public SimpleItem getItemFromFile() {
		return getBuilderFromFile().build();
	}

	public static YamlItem fromConfiguration(@NotNull final ConfigurationSection configuration) {
		return new YamlItem(configuration);
	}

	public static YamlItem fromFile(@NotNull final File file) {
		return new YamlItem(FileUtils.getConfiguration(file));
	}

	public static YamlItem fromPath(@NotNull final String path) {
		return new YamlItem(FileUtils.getResource(path));
	}

}
