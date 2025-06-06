package com.github.spigot_gillesm.item_lib.configuration;

import com.github.spigot_gillesm.item_lib.ItemUtil;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class ItemConfiguration {

    private static final double ATTACKS_SPEED_OFFSET = -4;

    private Material material;

    private int amount;

    private int damage;

    private String displayName;

    private List<String> lore = new ArrayList<>();

    private ItemFlag[] itemFlags = new ItemFlag[] {};

    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    private PotionType potionType;

    private boolean upgraded;

    private boolean extended;

    private List<PotionEffectData> potionEffectData = new ArrayList<>();

    private boolean unbreakable;

    private int customModelData;

    private ColorData colorData;

    private String localizedName;

    private double attackDamage;

    private double attackSpeed;

    private double armor;

    private double armorToughness;

    private double maxHealth;

    private double knockbackResistance;

    private double movementSpeed;

    private double flyingSpeed;

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
        final EquipmentSlot slot = ItemUtil.getEquipmentSlot(material);

        if(customModelData > 0) {
            builder.customModelData(customModelData);
        }
        if(attackDamage > 0) {
            builder.setAttackDamage(attackDamage, slot);
        }
        if(attackSpeed > 0) {
            builder.setAttackSpeed(attackSpeed + ATTACKS_SPEED_OFFSET, slot);
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
            builder.color(colorData.getRed(), colorData.getGreen(), colorData.getBlue());
        }
        for(final var line : lore) {
            builder.addLore(line);
        }
        for(final PotionEffectData potionEffectData : potionEffectData) {
            builder.addPotionEffect(potionEffectData.getPotionEffectType(),
                    //* 20 : ticks -> seconds
                    (int) potionEffectData.getDuration() * 20,
                    potionEffectData.getAmplifier());
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
                .add("potionEffectData", potionEffectData)
                .add("upgraded", upgraded)
                .add("extended", extended)
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

    @Setter
    @Getter
    public static class ConfigurationMap {

        private Map<String, ItemConfiguration> configurationMap;

    }

}
