package com.github.spigot_gillesm.item_lib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SimpleItemTest {

    @Test
    void given_null_material_to_get_item_stack_then_illegal_argument_exception_is_thrown() {
        final SimpleItem item = SimpleItem.newBuilder()
                .material(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, item::getItemStack);
    }

    @Test
    void given_non_positive_amount_to_get_item_stack_then_illegal_argument_exception_is_thrown() {
        final SimpleItem item = SimpleItem.newBuilder()
                .amount(0)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, item::getItemStack);
    }

    @Test
    void given_non_positive_custom_model_data_to_get_item_stack_then_illegal_argument_exception_is_thrown() {
        final SimpleItem item = SimpleItem.newBuilder()
                .customModelData(0)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, item::getItemStack);
    }

    @Test
    void given_negative_damage_to_get_item_stack_then_illegal_argument_exception_is_thrown() {
        final SimpleItem item = SimpleItem.newBuilder()
                .damage(-1)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, item::getItemStack);
    }

    @Test
    void using_default_builder_then_default_values_are_used() {
        final ItemFactory itemFactory = mock(ItemFactory.class);
        final MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
        final SimpleItem item = SimpleItem.newBuilder().build();

        mockBukkit.when(Bukkit::getItemFactory).thenReturn(itemFactory);

        assertThat(item.getMaterial()).isEqualTo(Material.WOODEN_SWORD);
        assertThat(item.getDisplayName()).isNull();
        assertThat(item.getLore()).isEmpty();
        assertThat(item.getItemFlags()).isEmpty();
        assertThat(item.getAmount()).isEqualTo(1);
        assertThat(item.getAttributeModifiers()).isEmpty();
        assertThat(item.getCustomModelData()).isNull();
        assertThat(item.getEnchantments()).isEmpty();
        assertThat(item.getDamage()).isZero();
        assertThat(item.getLocalizedName()).isNull();
        assertThat(item.isUnbreakable()).isFalse();
        assertThat(item.getItemStack()).isNotNull();

        mockBukkit.close();
    }

    @Test
    void given_valid_values_to_builder_then_valid_item_is_returned() {
        final ItemFactory itemFactory = mock(ItemFactory.class);
        final MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);

        mockBukkit.when(Bukkit::getItemFactory).thenReturn(itemFactory);

        final SimpleItem item = SimpleItem.newBuilder()
                .material(Material.IRON_SWORD)
                .displayName("&2Steel Sword")
                .lore("", "&7Sword of steel")
                .itemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
                .amount(3)
                .customModelData(1003)
                .damage(250)
                .localizedName("item:001")
                .unbreakable(false)
                .build();

        assertThat(item.getMaterial()).isEqualTo(Material.IRON_SWORD);
        assertThat(item.getDisplayName()).isEqualTo("&2Steel Sword");
        assertThat(item.getLore()).isEqualTo(new ArrayList<>(Arrays.asList("", "&7Sword of steel")));
        assertThat(item.getItemFlags()).isEqualTo(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES});
        assertThat(item.getAmount()).isEqualTo(3);
        assertThat(item.getCustomModelData()).isEqualTo(1003);
        assertThat(item.getDamage()).isEqualTo(250);
        assertThat(item.getLocalizedName()).isEqualTo("item:001");
        assertThat(item.isUnbreakable()).isFalse();
        assertThat(item.getItemStack()).isNotNull();

        mockBukkit.close();
    }

}
