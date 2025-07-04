package com.github.spigot_gillesm.item_lib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionType;

import java.io.IOException;

class ItemDeserializer {

    protected ItemDeserializer() { }

    public static class MaterialDeserializer extends StdDeserializer<Material> {

        public MaterialDeserializer(Class<?> vc) {
            super(vc);
        }

        public MaterialDeserializer() {
            this(null);
        }

        @Override
        public Material deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            final String data = parser.getText();

            try {
                return Material.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                Formatter.error(String.format("Invalid material: %s", data));

                throw new IllegalArgumentException("Invalid material");
            }
        }

    }

    public static class ItemFlagDeserializer extends StdDeserializer<ItemFlag> {

        public ItemFlagDeserializer(Class<?> vc) {
            super(vc);
        }

        public ItemFlagDeserializer() {
            this(null);
        }

        @Override
        public ItemFlag deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            final String data = parser.getText();

            try {
                return ItemFlag.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                Formatter.error(String.format("Invalid item flag: %s", data));

                throw new IllegalArgumentException("Invalid item flag");
            }
        }

    }

    public static class EnchantmentDeserializer extends KeyDeserializer {

        @Override
        public Enchantment deserializeKey(String key, DeserializationContext context) {
            final Enchantment enchant = ItemLib.getVersionWrapper().getEnchantment(key.toLowerCase());

            if(enchant == null) {
                Formatter.error(String.format("Invalid enchantment name: %s", key));

                throw new IllegalArgumentException("Invalid enchantment name");
            }

            return enchant;
        }
    }

    public static class PotionTypeDeserializer extends StdDeserializer<PotionType> {

        public PotionTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        public PotionTypeDeserializer() {
            this(null);
        }

        @Override
        public PotionType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            final String data = parser.getText();

            try {
                return PotionType.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                Formatter.error(String.format("Invalid potion type: %s", data));

                throw new IllegalArgumentException("Invalid potion type");
            }
        }

    }

}
