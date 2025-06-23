package com.github.spigot_gillesm.item_lib.version;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

/**
 * An interface designed to abstract version specific API calls.
 */
public interface VersionWrapper {

    AttributeModifier buildAttackDamageModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildAttackSpeedModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildArmorModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildArmorToughnessModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildMaxHealthModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildKnockbackResistanceModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildMovementSpeedModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    AttributeModifier buildFlyingSpeedModifier(double amount, @NotNull AttributeModifier.Operation operation, @NotNull EquipmentSlot slot);

    String generateAttributeString(@NotNull Attribute attribute, @NotNull AttributeModifier modifier);

    void setBasePotionData(@NotNull PotionMeta potionMeta, @NotNull PotionType potionType, boolean extended, boolean upgraded);

    void setLocalizedName(@NotNull ItemMeta itemMeta, String string);

    void setCustomModelData(@NotNull ItemMeta itemMeta, int modelData);

    Enchantment getEnchantment(@NotNull String enchantment);

    PotionEffectType getPotionEffectType(@NotNull String potionEffectType);

    Attribute getAttackSpeedAttribute();

    Attribute getAttackDamageAttribute();

    Attribute getArmorAttribute();

    Attribute getArmorToughnessAttribute();

    Attribute getMaxHealthAttribute();

    Attribute getKnockbackResistanceAttribute();

    Attribute getMovementSpeedAttribute();

    Attribute getFlyingSpeedAttribute();

}
