package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

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
    private final PotionType potionType;

    @Getter
    private final boolean upgraded;

    @Getter
    private final boolean extended;

    @Getter
    private final Set<PotionEffect> potionEffects;

    @Getter
    private final boolean unbreakable;

    @Getter
    private final Integer customModelData;

    @Getter
    private final Color color;

    @Getter
    private final String localizedName;

    @Getter
    private final Map<Attribute, AttributeModifier> attributeModifiers;

    private final Set<SimplePersistentData<String, String>> persistentDataString;

    @Getter
    private ItemStack itemStack;

    /**
     * SimpleItem cannot be instantiated. Use its builder to create an instance of SimpleItem
     * @see SimpleItem.Builder
     *
     * @param builder the builder
     */
    private SimpleItem(final Builder builder) {
        this.itemStack = builder.itemStack;
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.displayName = builder.displayName;
        this.lore = builder.lore;
        this.itemFlags = builder.itemFlags;
        this.enchantments = builder.enchantments;
        this.potionType = builder.potionType;
        this.upgraded = builder.upgraded;
        this.extended = builder.extended;
        this.potionEffects = builder.potionEffects;
        this.unbreakable = builder.unbreakable;
        this.customModelData = builder.customModelData;
        this.color = builder.color;
        this.localizedName = builder.localizedName;
        this.attributeModifiers = builder.attributeModifiers;
        this.persistentDataString = builder.persistentDataString;
    }

    /**
     * Transform the current instance of SimpleItem into a new instance of ItemStack.
     *
     * @return a new instance of ItemStack matching the current instance data
     */
    public SimpleItem make() {
        if(itemStack != null) {
            final var meta = itemStack.getItemMeta();
            setPersistentData(meta);
            itemStack.setItemMeta(meta);

            return this;
        }
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

        if(meta != null) {
            meta.setDisplayName(Formatter.colorize(displayName));
            meta.addItemFlags(itemFlags);
            meta.setUnbreakable(unbreakable);
            meta.setCustomModelData(customModelData);
            meta.setLocalizedName(localizedName);

            setEnchantmentsData(meta);
            setPotionData(meta);
            setDamageData(meta);
            setColorData(meta);
            setAttributeModifiersData(meta);
            setPersistentData(meta);

            meta.setLore(Formatter.colorize(lore));
            item.setItemMeta(meta);
        }

        item.setAmount(amount);
        this.itemStack = item;

        return this;
    }

    private void setEnchantmentsData(final ItemMeta itemMeta) {
        if(itemMeta instanceof EnchantmentStorageMeta) {
            enchantments.forEach((enchantment, level) -> ((EnchantmentStorageMeta) itemMeta)
                    .addStoredEnchant(enchantment, level,true));
        } else {
            enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));
        }
    }

    private void setPotionData(final ItemMeta itemMeta) {
        if(itemMeta instanceof PotionMeta && potionType != null) {
            var potionMeta = (PotionMeta) itemMeta;
            potionMeta.setBasePotionData(new PotionData(potionType, extended, upgraded));
            potionEffects.forEach(e -> potionMeta.addCustomEffect(e, true));
        }
    }

    private void setDamageData(final ItemMeta itemMeta) {
        if(damage > 0 && itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(damage);
        }
    }

    private void setColorData(final ItemMeta itemMeta) {
        if(color != null && itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
        }
    }

    private void setAttributeModifiersData(final ItemMeta itemMeta) {
        attributeModifiers.forEach(itemMeta::addAttributeModifier);

        final var isArmor = material.name().contains("HELMET") || material.name().contains("CHESTPLATE")
                || material.name().contains("LEGGINGS") || material.name().contains("BOOTS");
        //If there are attributes and the item is not an armor and the item is not hiding its attributes
        //-> override them with a clean lore
        if(!attributeModifiers.isEmpty() && !isArmor && Arrays.stream(itemFlags).noneMatch(f -> f == ItemFlag.HIDE_ATTRIBUTES)) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            lore.add("");
            lore.add("&7When in Main Hand:");

            for(final var attributeSet : attributeModifiers.entrySet()) {
                lore.add(generateAttributeString(attributeSet.getKey(), attributeSet.getValue()));
            }
        }
    }

    private void setPersistentData(final ItemMeta itemMeta) {
        for(final var data : persistentDataString) {
            itemMeta.getPersistentDataContainer()
                    .set(new NamespacedKey(ItemLib.getPlugin(), data.getKey()), data.getPersistentDataType(), data.getValue());
        }
    }

    private String generateAttributeString(final Attribute attribute, final AttributeModifier modifier) {
        final var value = attribute == Attribute.GENERIC_ATTACK_SPEED ? modifier.getAmount() + 4 : modifier.getAmount();
        final var string = attribute.name().replace("GENERIC_", "").split("_");
        final var stringBuilder = new StringBuilder(" &2" + value + " ");

        for(final var word : string) {
            //Capitalize the first letter and lower the others and add a space at the end
            stringBuilder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        if(!stringBuilder.isEmpty()) {
            //Remove the useless last space
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
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

        private ItemStack itemStack;

        private Material material = Material.WOODEN_SWORD;

        private int amount = 1;

        private int damage = 0;

        private String displayName;

        private List<String> lore = new ArrayList<>();

        private ItemFlag[] itemFlags = new ItemFlag[]{};

        private Map<Enchantment, Integer> enchantments = new HashMap<>();

        private PotionType potionType;

        private boolean upgraded = false;

        private boolean extended = false;

        private final Set<PotionEffect> potionEffects = new HashSet<>();

        private boolean unbreakable = false;

        private Integer customModelData;

        private Color color;

        private String localizedName;

        private Map<Attribute, AttributeModifier> attributeModifiers = new EnumMap<>(Attribute.class);

        private final Set<SimplePersistentData<String, String>> persistentDataString = new HashSet<>();

        private Builder() { }

        public Builder itemStack(final ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

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
            final List<ItemFlag> currentFlags = new ArrayList<>(Arrays.asList(this.itemFlags));
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
            if(enchantment == null) {
                throw new IllegalArgumentException("Enchantment cannot be null.");
            }
            if(level < 1) {
                throw new IllegalArgumentException("Enchantments level must be greater than 0.");
            }
            this.enchantments.put(enchantment, level);
            return this;
        }

        /**
         * Set the item's option type
         *
         * @param type the potion type
         * @return the builder
         */
        public Builder potionType(final PotionType type) {
            this.potionType = type;
            return this;
        }

        public Builder upgraded(final boolean upgraded) {
            this.upgraded = upgraded;
            return this;
        }

        public Builder extended(final boolean extended) {
            this.extended = extended;
            return this;
        }

        public Builder addPotionEffect(final PotionEffect potionEffect) {
            potionEffects.add(potionEffect);
            return this;
        }

        public Builder addPotionEffects(final Collection<PotionEffect> potionEffects) {
            this.potionEffects.addAll(potionEffects);
            return this;
        }

        public Builder addPotionEffects(final PotionEffect... potionEffects) {
            for(var effect : potionEffects) {
                addPotionEffect(effect);
            }
            return this;
        }

        public Builder addPotionEffect(final PotionEffectType type, final int duration, final int amplifier,
                                       final boolean ambient, final boolean particles, final boolean icon) {
            return addPotionEffect(new PotionEffect(type, duration, amplifier, ambient, particles, icon));
        }

        public Builder addPotionEffect(final PotionEffectType type, final int duration, final int amplifier) {
            return addPotionEffect(type, duration, amplifier, true, true, true);
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

        public Builder color(final Color color) {
            this.color = color;
            return this;
        }

        /**
         * Set the item's color.
         *
         * @param r the red value from 0 to 255
         * @param g the green value from 0 to 255
         * @param b the blue value from 0 to 255
         * @return the builder
         */
        public Builder color(final int r, final int g, final int b) {
            return color(Color.fromRGB(r, g, b));
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

        public Builder addPersistentString(final String key, final String value) {
            persistentDataString.add(new SimplePersistentData<>(key, PersistentDataType.STRING, value));
            return this;
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
