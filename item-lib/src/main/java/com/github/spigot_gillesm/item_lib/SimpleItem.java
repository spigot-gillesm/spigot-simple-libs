package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.*;

/**
 * The SimpleItem class simplifies the creation of ItemStack through the use of its Builder.
 * @see SimpleItem.Builder
 *
 * @version 1.0
 * @author Gilles_M
 */
public class SimpleItem {

    @Getter
    private final Material material;

    @Getter
    private final int amount;

    @Getter
    private final int damage;

    @Getter
    private final String displayName;

    @Getter
    private final List<String> lore;

    @Getter
    private final ItemFlag[] itemFlags;

    @Getter
    private final Map<Enchantment, Integer> enchantments;

    @Getter
    private final boolean unbreakable;

    @Getter
    private final Integer customModelData;

    @Getter
    private final String localizedName;

    @Getter
    private final Map<Attribute, AttributeModifier> attributeModifiers;

    /**
     * SimpleItem cannot be instantiated. Use its builder to create an instance of SimpleItem
     * @see SimpleItem.Builder
     *
     * @param builder the builder
     */
    private SimpleItem(final Builder builder) {
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.displayName = builder.displayName;
        this.lore = builder.lore;
        this.itemFlags = builder.itemFlags;
        this.enchantments = builder.enchantments;
        this.unbreakable = builder.unbreakable;
        this.customModelData = builder.customModelData;
        this.localizedName = builder.localizedName;
        this.attributeModifiers = builder.attributeModifiers;
    }

    /**
     * Transform the current instance of SimpleItem into a new instance of ItemStack.
     *
     * @return a new instance of ItemStack matching the current instance data
     */
    public ItemStack getItemStack() {
        if(material == null) {
            throw new IllegalArgumentException("material cannot be null.");
        }
        if(amount < 1) {
            throw new IllegalArgumentException("amount must be greater than 0.");
        }
        if(customModelData != null && customModelData < 1) {
            throw new IllegalArgumentException("customModelData must be greater than 0.");
        }
        if(damage < 0) {
            throw new IllegalArgumentException("damage cannot be negative.");
        }

        final var item = new ItemStack(material);
        final var meta = Bukkit.getServer().getItemFactory().getItemMeta(material);
        item.setAmount(amount);

        if(meta != null) {
            meta.setDisplayName(Formatter.colorize(displayName));
            meta.setLore(Formatter.colorize(lore));
            meta.addItemFlags(itemFlags);
            enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true));
            meta.setUnbreakable(unbreakable);
            meta.setCustomModelData(customModelData);
            meta.setLocalizedName(localizedName);
            attributeModifiers.forEach(meta::addAttributeModifier);

            if(meta instanceof Damageable && damage > 0) {
                ((Damageable) meta).setDamage(damage);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * @return a new SimpleItem builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * The builder class of SimpleItem
     */
    public static class Builder {

        private Material material = Material.WOODEN_SWORD;

        private int amount = 1;

        private int damage = 0;

        private String displayName;

        private List<String> lore = new ArrayList<>();

        private ItemFlag[] itemFlags = new ItemFlag[]{};

        private Map<Enchantment, Integer> enchantments = new HashMap<>();

        private boolean unbreakable = false;

        private Integer customModelData;

        private String localizedName;

        private Map<Attribute, AttributeModifier> attributeModifiers = new EnumMap<>(Attribute.class);

        private Builder() { }

        /**
         * Set the item's material.
         *
         * @param material the material
         * @return the builder
         */
        public Builder material(final Material material) {
            this.material = material;
            return this;
        }

        /**
         * Set the item's amount.
         *
         * @param amount the amount
         * @return the builder
         */
        public Builder amount(final int amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Set the item's damage. Not to be confused with the item's attack damage.
         *
         * @param damage the item's damage
         * @return the builder
         */
        public Builder damage(final int damage) {
            this.damage = damage;
            return this;
        }

        /**
         * Set the item's displayed name. Supports color codes.
         *
         * @param displayName the displayed name
         * @return the builder
         */
        public Builder displayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Set the item's lore. Supports color codes.
         *
         * @param lore the lore
         * @return the builder
         */
        public Builder lore(final List<String> lore) {
            this.lore = lore;
            return this;
        }

        /**
         * Set the item's lore. Supports color codes.
         *
         * @param lore the lore
         * @return the builder
         */
        public Builder lore(final String... lore) {
            this.lore = Arrays.asList(lore);
            return this;
        }

        /**
         * Add new lines of lore to the item's current lore. Supports color codes.
         *
         * @param lines the lines of lore to add
         * @return the builder
         */
        public Builder addLore(final String... lines) {
            lore.addAll(Arrays.asList(lines));
            return this;
        }

        /**
         * Set the item's item flags.
         *
         * @param itemFlags the item flags
         * @return the builder
         */
        public Builder itemFlags(final ItemFlag... itemFlags) {
            this.itemFlags = itemFlags;
            return this;
        }

        /**
         * Set the item's item flags.
         *
         * @param itemFlags the item flags
         * @return the builder
         */
        public Builder itemFlags(final List<ItemFlag> itemFlags) {
            this.itemFlags = itemFlags.toArray(new ItemFlag[0]);
            return this;
        }

        /**
         * Add item flags to the item's current item flags.
         *
         * @param itemFlags the item flags to add
         * @return the builder
         */
        public Builder addItemFlags(final ItemFlag... itemFlags) {
            final List<ItemFlag> currentFlags = new ArrayList<>(Arrays.asList(itemFlags));
            currentFlags.addAll(Arrays.asList(itemFlags));
            this.itemFlags = currentFlags.toArray(new ItemFlag[0]);
            return this;
        }

        /**
         * Set the item's enchantments.
         *
         * @param enchantments the enchantments map
         * @return the builder
         */
        public Builder enchantments(final Map<Enchantment, Integer> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        /**
         * Add an enchantment the the item's current enchantments.
         *
         * @param enchantment the enchantment to add
         * @param level the enchantment's level
         * @return the builder
         */
        public Builder addEnchantment(final Enchantment enchantment, int level) {
            this.enchantments.put(enchantment, level);
            return this;
        }

        /**
         * Set the item's unbreakable state.
         *
         * @param unbreakable the state
         * @return the builder
         */
        public Builder unbreakable(final boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        /**
         * Set the item's custom model data.
         *
         * @param customModelData the custom model data
         * @return the builder
         */
        public Builder customModelData(final Integer customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        /**
         * Set the item's localized name.
         *
         * @param localizedName the localized name
         * @return the builder
         */
        public Builder localizedName(final String localizedName) {
            this.localizedName = localizedName;
            return this;
        }

        /**
         * Set the item's attribute modifiers.
         *
         * @param attributeModifiers the attribute modifiers
         * @return the builder
         */
        public Builder attributeModifiers(final Map<Attribute, AttributeModifier> attributeModifiers) {
            this.attributeModifiers = new EnumMap<>(attributeModifiers);
            return this;
        }

        /**
         * Add an attribute modifier to the item's current attribute modifiers.
         *
         * @param attribute the attribute
         * @param attributeModifier the attribute modifier
         * @return the builder
         */
        public Builder addAttributeModifier(final Attribute attribute, final AttributeModifier attributeModifier) {
            this.attributeModifiers.put(attribute, attributeModifier);
            return this;
        }

        /**
         * Set the item's attack damage by converting the values to a new attribute modifier.
         *
         * @param attackDamage the attack damage
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setAttackDamage(final double attackDamage, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                    new AttributeModifier(UUID.randomUUID(), "attackDamage", attackDamage,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         * Set the item's attack speed by converting the values to a new attribute modifier.
         *
         * @param attackSpeed the attack speed
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setAttackSpeed(final double attackSpeed, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                    new AttributeModifier(UUID.randomUUID(), "attackSpeed", attackSpeed,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         * Set the item's armor by converting the values to a new attribute modifier.
         *
         * @param armor the armor
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setArmor(final double armor, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_ARMOR,
                    new AttributeModifier(UUID.randomUUID(), "armor", armor,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         *  Set the item's armor toughness by converting the values to a new attribute modifier.
         *
         * @param armorToughness the armor toughness
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setArmorToughness(final double armorToughness, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS,
                    new AttributeModifier(UUID.randomUUID(), "armorToughness", armorToughness,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         * Set the item's bonus health by converting the values to a new attribute modifier.
         *
         * @param maxHealth the max health
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setMaxHealth(final double maxHealth, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
                    new AttributeModifier(UUID.randomUUID(), "maxHealth", maxHealth,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         * Set the item's knockback resistance by converting the values to a new attribute modifier.
         *
         * @param knockbackResistance the knockback resistance
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setKnockbackResistance(final double knockbackResistance, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE,
                    new AttributeModifier(UUID.randomUUID(), "knockbackResistance", knockbackResistance,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         * Set the item's movement speed by converting the values to a new attribute modifier.
         *
         * @param movementSpeed the movement speed
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setMovementSpeed(final double movementSpeed, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
                    new AttributeModifier(UUID.randomUUID(), "movementSpeed", movementSpeed,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         * Set the item's flying speed by converting the values to a new attribute modifier.
         *
         * @param flyingSpeed the flying speed
         * @param slot the equipment slot
         * @return the builder
         */
        public Builder setFlyingSpeed(final double flyingSpeed, final EquipmentSlot slot) {
            return addAttributeModifier(Attribute.GENERIC_FLYING_SPEED,
                    new AttributeModifier(UUID.randomUUID(), "flyingSpeed", flyingSpeed,
                            AttributeModifier.Operation.ADD_NUMBER, slot));
        }

        /**
         *
         * @return a new instance of SimpleItem matching the builder's data.
         */
        public SimpleItem build() {
            return new SimpleItem(this);
        }

    }

}
