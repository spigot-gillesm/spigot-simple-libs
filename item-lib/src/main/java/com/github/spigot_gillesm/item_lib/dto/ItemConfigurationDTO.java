package com.github.spigot_gillesm.item_lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemConfigurationDTO {

    @JsonProperty(value = "material", required = true)
    private String material = "";

    @JsonProperty("amount")
    private int amount = 1;

    @JsonProperty("damage")
    private int damage = 0;

    @JsonProperty("display-name")
    private String displayName;

    @JsonProperty("lore")
    private List<String> lore = new ArrayList<>();

    @JsonProperty("item-flags")
    private List<String> itemFlags = new ArrayList<>();

    @JsonProperty("enchantments")
    private Map<String, Integer> enchantments = new HashMap<>();

    @JsonProperty("potion-type")
    private String potionType;

    @JsonProperty("upgraded")
    private boolean upgraded;

    @JsonProperty("extended")
    private boolean extended;

    @JsonProperty("potion-effects")
    private Map<String, PotionEffectDTO> potionEffects = new HashMap<>();

    @JsonProperty("unbreakable")
    private boolean unbreakable;

    @JsonProperty("custom-model-data")
    private int customModelData;

    @JsonProperty("color")
    private ColorDTO color;

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

    @Getter
    public static class ConfigurationMap {

        private Map<String, ItemConfigurationDTO> configurationMap;

    }

}
