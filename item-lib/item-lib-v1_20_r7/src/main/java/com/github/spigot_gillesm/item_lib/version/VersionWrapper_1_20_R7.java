package com.github.spigot_gillesm.item_lib.version;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VersionWrapper_1_20_R7 implements VersionWrapper {

    @Override
    public AttributeModifier buildAttackDamageModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                       @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "attackDamage",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildAttackSpeedModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                      @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "attackSpeed",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildArmorModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "armor",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildArmorToughnessModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                         @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "armorToughness",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildMaxHealthModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                    @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "maxHealth",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildKnockbackResistanceModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                              @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "knockbackResistance",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildMovementSpeedModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                        @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "movementSpeed",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public AttributeModifier buildFlyingSpeedModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                      @NotNull EquipmentSlot slot) {

        return new AttributeModifier(UUID.randomUUID(),
                "flyingSpeed",
                amount,
                AttributeModifier.Operation.ADD_NUMBER,
                slot);
    }

    @Override
    public String generateAttributeString(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        final double value = attribute == Attribute.GENERIC_ATTACK_SPEED ? modifier.getAmount() + 4 : modifier.getAmount();
        final String[] string = attribute.getKey().getKey().split("\\.")[1].split("_");
        final var stringBuilder = new StringBuilder(" &2" + value + " ");

        for(final String word : string) {
            //Capitalize the first letter and lower the others and add a space at the end
            stringBuilder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        if(!stringBuilder.isEmpty()) {
            //Remove the useless last space
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    @Override
    public void setBasePotionData(@NotNull PotionMeta potionMeta, @NotNull PotionType potionType, final boolean extended, final boolean upgraded) {
        //Check if the extended version should be applied
        if(potionType.isExtendable() && extended && !potionType.name().startsWith("LONG_")) {
            potionMeta.setBasePotionType(PotionType.valueOf("LONG_" + potionType.name()));
        }
        //Check if the upgraded version should be applied
        else if(potionType.isExtendable() && upgraded && !potionType.name().startsWith("STRONG_")) {
            potionMeta.setBasePotionType(PotionType.valueOf("STRONG_" + potionType.name()));
        }
        //Otherwise simply apply the provided potion type
        else {
            potionMeta.setBasePotionType(potionType);
        }
    }

    public void setLocalizedName(@NotNull ItemMeta itemMeta, String string) {
        //Does nothing as localized name has no use
    }

    @Override
    public void setCustomModelData(@NotNull ItemMeta itemMeta, final int modelData) {
        itemMeta.setCustomModelData(modelData);
    }

    public Enchantment getEnchantment(@NotNull String enchantment) {
        return Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantment));
    }

    public PotionEffectType getPotionEffectType(@NotNull String potionEffectType) {
        return Registry.EFFECT.get(NamespacedKey.minecraft(potionEffectType));
    }

    @Override
    public Attribute getAttackSpeedAttribute() {
        return Attribute.GENERIC_ATTACK_SPEED;
    }

    @Override
    public Attribute getAttackDamageAttribute() {
        return Attribute.GENERIC_ATTACK_DAMAGE;
    }

    @Override
    public Attribute getArmorAttribute() {
        return Attribute.GENERIC_ARMOR;
    }

    @Override
    public Attribute getArmorToughnessAttribute() {
        return Attribute.GENERIC_ARMOR_TOUGHNESS;
    }

    @Override
    public Attribute getMaxHealthAttribute() {
        return Attribute.GENERIC_MAX_HEALTH;
    }

    @Override
    public Attribute getKnockbackResistanceAttribute() {
        return Attribute.GENERIC_KNOCKBACK_RESISTANCE;
    }

    @Override
    public Attribute getMovementSpeedAttribute() {
        return Attribute.GENERIC_MOVEMENT_SPEED;
    }

    @Override
    public Attribute getFlyingSpeedAttribute() {
        return Attribute.GENERIC_FLYING_SPEED;
    }

}
