package com.github.spigot_gillesm.item_lib;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.io.IOException;

public class ItemDeserializer {

    public static class MaterialDeserializer extends StdDeserializer<Material> {

        public MaterialDeserializer(final Class<?> vc) {
            super(vc);
        }

        public MaterialDeserializer() {
            this(null);
        }

        @Override
        public Material deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final var data = parser.getText();

            try {
                return Material.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                throw new IllegalArgumentException(String.format("Invalid material: %s", data));
            }
        }

    }

    public static class ItemFlagDeserializer extends StdDeserializer<ItemFlag> {

        public ItemFlagDeserializer(final Class<?> vc) {
            super(vc);
        }

        public ItemFlagDeserializer() {
            this(null);
        }

        @Override
        public ItemFlag deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final var data = parser.getText();

            try {
                return ItemFlag.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                Formatter.error(String.format("Invalid item flag: %s", data));
                throw new IllegalArgumentException(String.format("Invalid item flag: %s", data));
            }
        }

    }

    public static class EnchantmentDeserializer extends KeyDeserializer {

        @Override
        public Enchantment deserializeKey(final String key, final DeserializationContext ctxt) throws IOException {
            try {
                return Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
            } catch(final IllegalArgumentException | ClassCastException exception) {
                Formatter.error(String.format("Invalid enchantment: %s", key));
                throw new IllegalArgumentException(String.format("Invalid enchantment: %s", key));
            }
        }

    }

    public static class PotionTypeDeserializer extends StdDeserializer<PotionType> {

        public PotionTypeDeserializer(final Class<?> vc) {
            super(vc);
        }

        public PotionTypeDeserializer() {
            this(null);
        }

        @Override
        public PotionType deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final var data = parser.getText();

            try {
                return PotionType.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                Formatter.error(String.format("Invalid potion type: %s", data));
                throw new IllegalArgumentException(String.format("Invalid potion type: %s", data));
            }
        }

    }

    /*public static class PotionEffectDeserializer extends StdDeserializer<PotionEffect> {

        public PotionEffectDeserializer(final Class<?> vc) {
            super(vc);
        }

        public PotionEffectDeserializer() {
            this(null);
        }

        @Override
        public PotionEffect deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final var data = parser.getText();

            try {
                return PotionEffect.valueOf(data.toUpperCase());
            } catch(final IllegalArgumentException exception) {
                Formatter.error(String.format("Invalid potion type: %s", data));
                throw new IllegalArgumentException(String.format("Invalid potion type: %s", data));
            }
        }

    }*/

}
