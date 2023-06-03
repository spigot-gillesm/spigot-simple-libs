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
import org.bukkit.configuration.ConfigurationSection;
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

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty(value = "material", required = true)
    @JsonDeserialize(using = ItemDeserializer.MaterialDeserializer.class)
    private Material material = Material.STICK;

    @JsonProperty("amount")
    private int amount = 1;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("damage")
    private int damage = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("display-name")
    private String displayName;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("lore")
    private List<String> lore = new ArrayList<>();

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("item-flags")
    @JsonDeserialize(contentUsing = ItemDeserializer.ItemFlagDeserializer.class)
    private ItemFlag[] itemFlags = new ItemFlag[] {};

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("enchantments")
    @JsonDeserialize(keyUsing = ItemDeserializer.EnchantmentDeserializer.class)
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("potion-type")
    @JsonDeserialize(using = ItemDeserializer.PotionTypeDeserializer.class)
    private PotionType potionType;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("upgraded")
    private boolean upgraded;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("extended")
    private boolean extended;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PACKAGE)
    private Set<PotionEffectData> potionEffectsData = new HashSet<>();

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("unbreakable")
    private boolean unbreakable;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("custom-model-data")
    private int customModelData;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("color")
    private ColorData colorData;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("localized-name")
    private String localizedName;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("attack-damage")
    private double attackDamage = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("attack-speed")
    private double attackSpeed = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("armor")
    private double armor = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("armor-toughness")
    private double armorToughness = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("max-health")
    private double maxHealth = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("knockback-resistance")
    private double knockbackResistance = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("movement-speed")
    private double movementSpeed = 0;

    @Getter(AccessLevel.PACKAGE)
    @JsonProperty("flying-speed")
    private double flyingSpeed = 0;

    private ConfigurationItem() { }

    private EquipmentSlot getEquipmentSlot() {
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

        return slot;
    }

    /**
     * @return the equivalent SimpleItem.Builder instance
     */
    public SimpleItem.Builder toSimpleBuilder() {
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
        final var slot = getEquipmentSlot();

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
            builder.addPotionEffect(potionEffectData.potionEffectType,
                    //* 20 : ticks -> seconds
                    (int) potionEffectData.duration * 20,
                    potionEffectData.amplifier);
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

    public static ConfigurationItem fromConfiguration(@NotNull final Map<String, Object> data) throws IOException,
            IllegalArgumentException {

        final var configurationItem = OBJECT_MAPPER.convertValue(data, ConfigurationItem.class);
        final var dataAsNode = OBJECT_MAPPER.convertValue(data, JsonNode.class);
        configurationItem.setPotionEffectsData(loadPotionEffectsData(dataAsNode));

        return configurationItem;
    }

    /**
     * Creates a matching instance of ConfigurationItem using the given configuration.
     *
     * @param configuration the configuration
     * @return a populated instance of ConfigurationItem
     */
    public static ConfigurationItem fromConfiguration(@NotNull final ConfigurationSection configuration)
            throws IllegalArgumentException {

        final Map<String, Object> content = removeRecursive(configuration.getValues(true));
        final var configurationItem = OBJECT_MAPPER.convertValue(content, ConfigurationItem.class);
        configurationItem.setPotionEffectsData(loadPotionEffectsData(configuration));

        return configurationItem;
    }

    /**
     * Creates a matching instance of ConfigurationItem using the given configuration and id.
     *
     * @param configuration the configuration
     * @param id the section's id to get the item's data from
     * @return a populated instance of ConfigurationItem
     */
    public static ConfigurationItem fromConfiguration(@NotNull final ConfigurationSection configuration, @NotNull final String id)
            throws IllegalArgumentException {

        if(!configuration.isConfigurationSection(id)) {
            throw new IllegalArgumentException(String.format("No such configuration: %s", id));
        }

        return fromConfiguration(configuration.getConfigurationSection(id));
    }

    /**
     * Creates a matching instance of ConfigurationItem using the given (yaml) file.
     *
     * @param file the (yaml) file
     * @return a populated instance of ConfigurationItem
     */
    public static ConfigurationItem fromFile(@NotNull final File file) throws IOException, IllegalArgumentException {
        final var configurationItem = OBJECT_MAPPER.readValue(file, ConfigurationItem.class);
        configurationItem.setPotionEffectsData(loadPotionEffectsData(file));

        return configurationItem;
    }

    private static Map<String, Object> removeRecursive(final Map<String, Object> map) {
        final Map<String, Object> copy = new HashMap<>();

        for(final var entrySet : map.entrySet()) {
            if(entrySet.getValue() instanceof ConfigurationSection) {
                copy.put(entrySet.getKey(), removeRecursive(((ConfigurationSection) entrySet.getValue()).getValues(true)));
            } else {
                copy.put(entrySet.getKey(), entrySet.getValue());
            }
        }

        return copy;
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

    private static Set<PotionEffectData> loadPotionEffectsData(final JsonNode jsonNode) throws IOException {
        final var effectsIterator = jsonNode.path("potion-effects").elements();

        final Set<PotionEffectData> potionEffectsData = new HashSet<>();
        final List<String> effectNames = new ArrayList<>();

        jsonNode.path("potion-effects")
                .fieldNames()
                .forEachRemaining(effectNames::add);

        for(final var effectName : effectNames) {
            if(effectsIterator.hasNext()) {
                loadPotionEffectData(effectName, effectsIterator.next())
                        .ifPresent(potionEffectsData::add);
            }
        }

        return potionEffectsData;
    }

    private static Set<PotionEffectData> loadPotionEffectsData(final ConfigurationSection configuration) {
        final Set<PotionEffectData> potionEffectData = new HashSet<>();

        if(!configuration.isConfigurationSection("potion-effects")) {
            return potionEffectData;
        }
        final var effectsSection = configuration.getConfigurationSection("potion-effects");

        //Loop on each effect name and get its data
        for(final var effectName : effectsSection.getKeys(false)) {
            if(effectsSection.isConfigurationSection(effectName)) {
                loadPotionEffectData(effectName, effectsSection.getConfigurationSection(effectName))
                        .ifPresent(potionEffectData::add);
            }
        }

        return potionEffectData;
    }

    private static Optional<PotionEffectData> loadPotionEffectData(final String effectName, final JsonNode jsonNode)
            throws JsonProcessingException {
        final var effectData = OBJECT_MAPPER.readValue(jsonNode.toString(), PotionEffectData.class);

        try {
            final var effect = PotionEffectType.getByName(effectName);

            if(effect == null) {
                Formatter.error(String.format("Invalid potion effect type: %s", effectName));
                return Optional.empty();
            }
            effectData.setPotionEffectType(effect);

            return Optional.of(effectData);
        } catch(final IllegalArgumentException exception) {
            Formatter.error(String.format("Invalid potion effect type: %s", effectName));
            return Optional.empty();
        }
    }

    private static Optional<PotionEffectData> loadPotionEffectData(final String effectName,
                                                                   final ConfigurationSection configurationSection) {

        final Map<String, Object> sectionData = configurationSection.getValues(false);
        final var effectData = OBJECT_MAPPER.convertValue(sectionData, PotionEffectData.class);

        try {
            final var effect = PotionEffectType.getByName(effectName);

            if(effect == null) {
                Formatter.error(String.format("Invalid potion effect type: %s", effectName));
                return Optional.empty();
            }
            effectData.setPotionEffectType(effect);

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
        private double duration;

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
