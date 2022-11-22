package com.github.spigot_gillesm.item_lib;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.spigot_gillesm.file_utils.FileUtils;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigurationItem {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    @Getter
    @JsonProperty("material")
    @JsonDeserialize(using = ItemDeserializer.MaterialDeserializer.class)
    private Material material;

    @Getter
    @JsonProperty("amount")
    private int amount;

    @Getter
    @JsonProperty("damage")
    private int damage;

    @Getter
    @JsonProperty("display-name")
    private String displayName;

    @Getter
    @JsonProperty("lore")
    private List<String> lore;

    @Getter
    @JsonProperty("item-flags")
    private ItemFlag[] itemFlags;

    @Getter
    @JsonProperty("enchantments")
    @JsonDeserialize(keyUsing = ItemDeserializer.EnchantmentDeserializer.class)
    private Map<Enchantment, Integer> enchantments;

    @Getter
    @JsonProperty("potion-type")
    @JsonDeserialize(using = ItemDeserializer.PotionTypeDeserializer.class)
    private PotionType potionType;

    @Getter
    @JsonProperty("upgraded")
    private boolean upgraded;

    @Getter
    @JsonProperty("extended")
    private boolean extended;

    @Getter
    @JsonProperty("potion-effects")
    private Set<PotionEffectData> potionEffects;

    @Getter
    @JsonProperty("unbreakable")
    private boolean unbreakable;

    @Getter
    @JsonProperty("custom-model-data")
    private Integer customModelData;

    @Getter
    @JsonProperty("color")
    private Color color;

    @Getter
    @JsonProperty("localized-name")
    private String localizedName;

    @JsonProperty("attack-damage")
    private double attackDamage;

    @JsonProperty("attack-speed")
    private double attackSpeed;

    @JsonProperty("armor")
    private double armor;

    @JsonProperty("armor-toughness")
    private double armorToughness;

    @JsonProperty("max-health")
    private double maxHealth;

    @JsonProperty("knockback-resistance")
    private double knockbackResistance;

    @JsonProperty("movement-speed")
    private double movementSpeed;

    @JsonProperty("flying-speed")
    private double flyingSpeed;

    public static SimpleItem.Builder getBuilderFromFile(@NotNull final File file) throws IOException {
        final ConfigurationItem configurationItem = OBJECT_MAPPER.readValue(file, ConfigurationItem.class);

        return null;
    }

    public static class PotionEffectData {

        private PotionEffect potionEffect;

        private double duration;

        private double amplifier;

    }

}
