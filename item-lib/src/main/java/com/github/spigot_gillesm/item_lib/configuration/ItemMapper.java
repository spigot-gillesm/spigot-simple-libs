package com.github.spigot_gillesm.item_lib.configuration;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.dto.ColorDTO;
import com.github.spigot_gillesm.item_lib.dto.ItemConfigurationDTO;
import com.github.spigot_gillesm.item_lib.dto.PotionEffectDTO;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.*;
import java.util.stream.Collectors;

public class ItemMapper {

    private static final ItemMapper INSTANCE = new ItemMapper();

    @Getter
    protected final ModelMapper modelMapper = new ModelMapper();

    protected ItemMapper() {
        configurePotionEffectMapper();
        configureColorMapper();
        configureItemMapper();
        configureItemMapMapper();
    }

    private void configurePotionEffectMapper() {
        modelMapper.typeMap(PotionEffectDTO.class, PotionEffectData.class)
                .addMappings(mapper -> mapper.map(PotionEffectDTO::getAmplifier, PotionEffectData::setAmplifier))
                .addMappings(mapper -> mapper.map(PotionEffectDTO::getDuration, PotionEffectData::setDuration));
    }

    private void configureColorMapper() {
        modelMapper.typeMap(ColorDTO.class, ColourData.class)
                .addMappings(mapper -> mapper.map(ColorDTO::getRed, ColourData::setRed))
                .addMappings(mapper -> mapper.map(ColorDTO::getGreen, ColourData::setGreen))
                .addMappings(mapper -> mapper.map(ColorDTO::getBlue, ColourData::setBlue));
    }

    private void configureItemMapper() {
        //MATERIAL
        modelMapper.addMappings(new PropertyMap<ItemConfigurationDTO, ItemConfiguration>() {
            @Override
            protected void configure() {
                using((Converter<ItemConfigurationDTO, Material>) context -> {
                    final String material = context.getSource().getMaterial();

                    try {
                        return Material.valueOf(material.toUpperCase());
                    } catch(final IllegalArgumentException exception) {
                        Formatter.error(String.format("Invalid material: %s", material));
                        throw new IllegalArgumentException("Invalid material");
                    }
                }).map(source).setMaterial(Material.STICK);
            }
        });
        //ENCHANTMENTS
        modelMapper.addMappings(new PropertyMap<ItemConfigurationDTO, ItemConfiguration>() {
            @Override
            protected void configure() {
                //Convert <string, int> to <enchantment, int>
                using((Converter<ItemConfigurationDTO, Map<Enchantment, Integer>>) context ->
                        context.getSource()
                                .getEnchantments()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(entry -> {
                                    final Enchantment enchantment = Enchantment.getByKey(
                                            NamespacedKey.minecraft(entry.getKey().toLowerCase())
                                    );
                                    if(enchantment == null) {
                                        Formatter.error(String.format("Invalid enchantment name: %s", entry.getKey()));
                                        throw new IllegalArgumentException("Invalid enchantment name");
                                    }

                                    return enchantment;
                                }, Map.Entry::getValue)))
                        .map(source).setEnchantments(new HashMap<>());
            }
        });
        //POTION TYPE
        modelMapper.addMappings(new PropertyMap<ItemConfigurationDTO, ItemConfiguration>() {
            @Override
            protected void configure() {
                using((Converter<ItemConfigurationDTO, PotionType>) context -> {
                    final var potionType = context.getSource().getPotionType();

                    try {
                        return PotionType.valueOf(potionType.toUpperCase());
                    } catch(final IllegalArgumentException exception) {
                        Formatter.error(String.format("Invalid potion type: %s", potionType));
                        throw new IllegalArgumentException("Invalid potion type");
                    }
                }).map(source).setPotionType(null);
            }
        });
        //POTION EFFECT DATA
        modelMapper.addMappings(new PropertyMap<ItemConfigurationDTO, ItemConfiguration>() {
            @Override
            protected void configure() {
                using((Converter<ItemConfigurationDTO, List<PotionEffectData>>) context ->
                        context.getSource()
                                .getPotionEffects()
                                .entrySet()
                                .stream()
                                .map(entry -> {
                                    //The map key is the potion effect type, the value is the rest of the data
                                    final String potionEffectTypeId = entry.getKey();
                                    final var potionEffectType = PotionEffectType.getByName(potionEffectTypeId);

                                    if(potionEffectType == null) {
                                        Formatter.error(String.format("Invalid potion effect type: %s", potionEffectTypeId));
                                        throw new IllegalArgumentException("Invalid potion effect type");
                                    }
                                    final var potionEffectData = modelMapper.map(entry.getValue(), PotionEffectData.class);
                                    potionEffectData.setPotionEffectType(potionEffectType);

                                    return potionEffectData;
                                })
                                .collect(Collectors.toList()))
                        .map(source).setPotionEffectData(new ArrayList<>());
            }
        });
        //COLOUR DATA
        modelMapper.addMappings(new PropertyMap<ItemConfigurationDTO, ItemConfiguration>() {
            @Override
            protected void configure() {
                using((Converter<ItemConfigurationDTO, ColourData>) context ->
                        modelMapper.map(context.getSource().getColor(), ColourData.class))
                        .map(source).setColorData(null);
            }
        });
        //Straightforward mappings
        modelMapper.typeMap(ItemConfigurationDTO.class, ItemConfiguration.class)
                //AMOUNT
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getAmount, ItemConfiguration::setAmount))
                //DAMAGE
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getDamage, ItemConfiguration::setDamage))
                //DISPLAYNAME
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getDisplayName, ItemConfiguration::setDisplayName))
                //LORE
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getLore, ItemConfiguration::setLore))
                //ITEM FLAGS
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getItemFlags, ItemConfiguration::setItemFlags))
                //UPGRADED
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::isUpgraded, ItemConfiguration::setUpgraded))
                //EXTENDED
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::isExtended, ItemConfiguration::setExtended))
                //CUSTOM MODEL DATA
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getCustomModelData, ItemConfiguration::setCustomModelData))
                //UNBREAKABLE
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::isUnbreakable, ItemConfiguration::setUnbreakable))
                //LOCALIZED NAME
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getLocalizedName, ItemConfiguration::setLocalizedName))
                //ATTACK DAMAGE
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getAttackDamage, ItemConfiguration::setAttackDamage))
                //ATTACK SPEED
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getAttackSpeed, ItemConfiguration::setAttackSpeed))
                //ARMOR
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getArmor, ItemConfiguration::setArmor))
                //ARMOR TOUGHNESS
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getArmorToughness, ItemConfiguration::setArmorToughness))
                //MAX HEALTH
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getMaxHealth, ItemConfiguration::setMaxHealth))
                //KNOCKBACK RESISTANCE
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getKnockbackResistance, ItemConfiguration::setKnockbackResistance))
                //MOVEMENT SPEED
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getMovementSpeed, ItemConfiguration::setMovementSpeed))
                //FLYING SPEED
                .addMappings(mapper -> mapper.map(ItemConfigurationDTO::getFlyingSpeed, ItemConfiguration::setFlyingSpeed));
    }

    private void configureItemMapMapper() {
        modelMapper.addMappings(new PropertyMap<ItemConfigurationDTO.ConfigurationMap, ItemConfiguration.ConfigurationMap>() {
            @Override
            protected void configure() {
                using((Converter<ItemConfigurationDTO.ConfigurationMap, Map<String, ItemConfiguration>>) context ->
                        context.getSource()
                                .getConfigurationMap()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> modelMapper.map(entry.getValue(), ItemConfiguration.class)
                                )))
                        .map(source).setConfigurationMap(new HashMap<>());
            }
        });
    }

    public static ItemMapper getInstance() {
        return INSTANCE;
    }

}
