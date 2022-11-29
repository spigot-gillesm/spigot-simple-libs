package com.github.spigot_gillesm.item_lib;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.google.common.base.MoreObjects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationItem {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    @Getter
    @JsonProperty("material")
    @JsonDeserialize(using = ItemDeserializer.MaterialDeserializer.class)
    private Material material = Material.STICK;

    @Getter
    @JsonProperty("amount")
    private int amount = 1;

    @Getter
    @JsonProperty("damage")
    private int damage = 0;

    @Getter
    @JsonProperty("display-name")
    private String displayName;

    @Getter
    @JsonProperty("lore")
    private List<String> lore = new ArrayList<>();

    @Getter
    @JsonProperty("item-flags")
    @JsonDeserialize(using = ItemDeserializer.ItemFlagDeserializer.class)
    private ItemFlag[] itemFlags = new ItemFlag[] {};

    @Getter
    @JsonProperty("enchantments")
    @JsonDeserialize(keyUsing = ItemDeserializer.EnchantmentDeserializer.class)
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

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

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private Set<PotionEffectData> potionEffectsData = new HashSet<>();

    @Getter
    @JsonProperty("unbreakable")
    private boolean unbreakable;

    @Getter
    @JsonProperty("custom-model-data")
    private int customModelData;

    @Getter
    @JsonProperty("color")
    private ColorData colorData;

    @Getter
    @JsonProperty("localized-name")
    private String localizedName;

    @Getter
    @JsonProperty("attack-damage")
    private double attackDamage = 0;

    @Getter
    @JsonProperty("attack-speed")
    private double attackSpeed = 0;

    @Getter
    @JsonProperty("armor")
    private double armor = 0;

    @Getter
    @JsonProperty("armor-toughness")
    private double armorToughness = 0;

    @Getter
    @JsonProperty("max-health")
    private double maxHealth = 0;

    @Getter
    @JsonProperty("knockback-resistance")
    private double knockbackResistance = 0;

    @Getter
    @JsonProperty("movement-speed")
    private double movementSpeed = 0;

    @Getter
    @JsonProperty("flying-speed")
    private double flyingSpeed = 0;

    private ConfigurationItem() { }

    public SimpleItem.Builder toSimpleBuilder() {
        var slot = EquipmentSlot.HAND;

        if(material.name().contains("SHIELD")) {
            slot = EquipmentSlot.OFF_HAND;
        } else if(material.name().contains("HELMET")) {
            slot = EquipmentSlot.HEAD;
        } else if(material.name().contains("CHESTPLATE") ||
                material.name().contains("ELYTRA")) {
            slot = EquipmentSlot.CHEST;
        } else if(material.name().contains("LEGGINGS")) {
            slot = EquipmentSlot.LEGS;
        } else if(material.name().contains("BOOTS")) {
            slot = EquipmentSlot.FEET;
        }

        final var builder = SimpleItem.newBuilder()
                .material(material)
                .amount(amount)
                .damage(damage)
                .displayName(displayName)
                .itemFlags(itemFlags)
                .enchantments(enchantments)
                .potionType(potionType)
                .upgraded(upgraded)
                .extended(extended)
                .unbreakable(unbreakable)
                .localizedName(localizedName);

        if(customModelData > 0) {
            builder.customModelData(customModelData);
        }
        if(attackDamage > 0) {
            builder.setAttackDamage(attackDamage, slot);
        }
        if(attackSpeed > 0) {
            builder.setAttackSpeed(attackSpeed - 4, slot);
        }
        if(armor> 0) {
            builder.setArmor(armor, slot);
        }
        if(armorToughness > 0) {
            builder.setArmorToughness(armorToughness, slot);
        }
        if(knockbackResistance > 0) {
            builder.setKnockbackResistance(knockbackResistance, slot);
        }
        if(movementSpeed > 0) {
            builder.setMovementSpeed(movementSpeed, slot);
        }
        if(flyingSpeed > 0) {
            builder.setFlyingSpeed(flyingSpeed, slot);
        }
        if(colorData != null) {
            builder.color(colorData.red, colorData.green,
                    colorData.blue);
        }
        for(final var line : lore) {
            builder.addLore(line);
        }
        for(final var potionEffectData : potionEffectsData) {
            builder.addPotionEffect(potionEffectData.potionEffectType, potionEffectData.duration, potionEffectData.amplifier);
        }

        return builder;
    }

    public SimpleItem toSimpleItem() {
        return toSimpleBuilder().build().make();
    }

    public ItemStack toItemStack() {
        return toSimpleItem().getItemStack();
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("material", material)
                .add("amount", amount)
                .add("damage", damage)
                .add("displayName", displayName)
                .add("lore", lore)
                .add("itemFlags", itemFlags)
                .add("enchantments", enchantments)
                .add("potionType", potionType)
                .add("potionEffectsData", potionEffectsData)
                .add("upgraded", upgraded)
                .add("extended", extended)
                .add("potionEffects", potionEffectsData)
                .add("unbreakable", unbreakable)
                .add("customModelData", customModelData)
                .add("colorData", colorData)
                .add("localizedName", localizedName)
                .add("attackDamage", attackDamage)
                .add("attackSpeed", attackSpeed)
                .add("armor", armor)
                .add("armorToughness", armorToughness)
                .add("maxHealth", maxHealth)
                .add("knockbackResistance", knockbackResistance)
                .add("movementSpeed", movementSpeed)
                .add("flyingSpeed", flyingSpeed)
                .toString();
    }

    public static ConfigurationItem fromFile(@NotNull final File file, @NotNull final String... ids) throws IOException {
        final var tree = OBJECT_MAPPER.readTree(file);

        for(final var id : ids) {
            tree.path(id);
        }
        final var itemRawData = tree.toString();
        Formatter.info("String: " + itemRawData);
        final var configurationItem = OBJECT_MAPPER.readValue(itemRawData, ConfigurationItem.class);
        configurationItem.setPotionEffectsData(loadPotionEffectsData(file));

        return configurationItem;
    }

    public static ConfigurationItem fromFile(@NotNull final File file) throws IOException {
        final var configurationItem = OBJECT_MAPPER.readValue(file, ConfigurationItem.class);
        configurationItem.setPotionEffectsData(loadPotionEffectsData(file));

        return configurationItem;
    }

    private static Set<PotionEffectData> loadPotionEffectsData(final File file) throws IOException {
        final var effectsIterator = OBJECT_MAPPER.readTree(file).path("potion-effects").elements();
        final Set<PotionEffectData> potionEffectsData = new HashSet<>();
        final List<String> effectNames = new ArrayList<>();

        OBJECT_MAPPER.readTree(file).path("potion-effects")
                .fieldNames()
                .forEachRemaining(effectNames::add);

        for(final var effectName : effectNames) {
            if(effectsIterator.hasNext()) {
                loadPotionEffectData(effectName, effectsIterator.next()).ifPresent(potionEffectsData::add);
            }
        }

        return potionEffectsData;
    }

    private static Optional<PotionEffectData> loadPotionEffectData(final String effectName, final JsonNode jsonNode)
            throws JsonProcessingException {
        final var effectData = OBJECT_MAPPER.readValue(jsonNode.toString(), PotionEffectData.class);

        try {
            effectData.setPotionEffectType(PotionEffectType.getByName(effectName));
            return Optional.of(effectData);
        } catch(final IllegalArgumentException exception) {
            Formatter.error(String.format("Invalid potion effect type: %s", effectName));
            return Optional.empty();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PotionEffectData {

        @Setter(AccessLevel.PACKAGE)
        @Getter(AccessLevel.PRIVATE)
        @JsonIgnore
        private PotionEffectType potionEffectType;

        @Setter(AccessLevel.PACKAGE)
        @JsonProperty("duration")
        private int duration;

        @Setter(AccessLevel.PACKAGE)
        @JsonProperty("amplifier")
        private int amplifier;

        @Override
        public final String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("potionEffectType", potionEffectType)
                    .add("duration", duration)
                    .add("amplifier", amplifier)
                    .toString();
        }

        @Override
        public final int hashCode() {
            return Objects.hash(potionEffectType, duration, amplifier);
        }

        @Override
        public final boolean equals(final Object other) {
            if(!(other instanceof PotionEffectData)) {
                return false;
            }
            final var otherData = (PotionEffectData) other;

            if(this == otherData) {
                return true;
            }

            return Objects.equals(this.potionEffectType, otherData.potionEffectType) &&
                    Objects.equals(this.duration, otherData.duration) &&
                    Objects.equals(this.amplifier, otherData.amplifier);
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ColorData {

        @JsonProperty("red")
        private int red = 0;

        @JsonProperty("green")
        private int green = 0;

        @JsonProperty("blue")
        private int blue = 0;

        @Override
        public final String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("red", red)
                    .add("green", green)
                    .add("blue", blue)
                    .toString();
        }

    }



}
