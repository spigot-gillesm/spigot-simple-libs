package com.github.spigot_gillesm.item_lib.version;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VersionWrapper_1_21_R4 implements VersionWrapper {

    @Override
    public AttributeModifier buildAttackDamageModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                       @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.ATTACK_DAMAGE.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildAttackSpeedModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                      @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.ATTACK_SPEED.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildArmorModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.ARMOR.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildArmorToughnessModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                         @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.ARMOR_TOUGHNESS.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildMaxHealthModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                    @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.MAX_HEALTH.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildKnockbackResistanceModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                              @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.KNOCKBACK_RESISTANCE.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildMovementSpeedModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                        @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.MOVEMENT_SPEED.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public AttributeModifier buildFlyingSpeedModifier(final double amount, AttributeModifier.@NotNull Operation operation,
                                                      @Nullable EquipmentSlot slot) {

        return new AttributeModifier(Attribute.FLYING_SPEED.getKey(),
                amount,
                operation,
                EquipmentSlotGroup.getByName(slot.name())
        );
    }

    @Override
    public String generateAttributeString(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        final double value = attribute == Attribute.ATTACK_SPEED ? modifier.getAmount() + 4 : modifier.getAmount();
        //The 'generic'/'player'... prefix is gone now, no need to split on '.'
        final String[] string = attribute.getKey().getKey().split("_");
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
        //Does nothing as localized name has no use in 1.21+
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
        return Attribute.ATTACK_SPEED;
    }

    @Override
    public Attribute getAttackDamageAttribute() {
        return Attribute.ATTACK_DAMAGE;
    }

    @Override
    public Attribute getArmorAttribute() {
        return Attribute.ARMOR;
    }

    @Override
    public Attribute getArmorToughnessAttribute() {
        return Attribute.ARMOR_TOUGHNESS;
    }

    @Override
    public Attribute getMaxHealthAttribute() {
        return Attribute.MAX_HEALTH;
    }

    @Override
    public Attribute getKnockbackResistanceAttribute() {
        return Attribute.KNOCKBACK_RESISTANCE;
    }

    @Override
    public Attribute getMovementSpeedAttribute() {
        return Attribute.MOVEMENT_SPEED;
    }

    @Override
    public Attribute getFlyingSpeedAttribute() {
        return Attribute.FLYING_SPEED;
    }

}
