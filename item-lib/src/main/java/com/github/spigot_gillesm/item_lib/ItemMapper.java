package com.github.spigot_gillesm.item_lib;

import com.github.spigot_gillesm.item_lib.configuration.ColorData;
import com.github.spigot_gillesm.item_lib.configuration.ItemConfiguration;
import com.github.spigot_gillesm.item_lib.configuration.PotionEffectData;
import com.github.spigot_gillesm.item_lib.dto.ColorDTO;
import com.github.spigot_gillesm.item_lib.dto.ItemConfigurationDTO;
import com.github.spigot_gillesm.item_lib.dto.PotionEffectDTO;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemMapper {

    private static final ItemMapper INSTANCE = new ItemMapper();

    @Getter
    protected final ModelMapper modelMapper = new ModelMapper();

	protected TypeMap<ItemConfigurationDTO, ItemConfiguration> baseTypeMap;

    protected ItemMapper() {
        configurePotionEffectMapper();
        configureColorMapper();
        configureItemMapper();
        configureItemMapMapper();
    }

    private void configurePotionEffectMapper() {
        modelMapper.createTypeMap(PotionEffectDTO.class, PotionEffectData.class)
                .addMappings(mapper -> mapper.map(PotionEffectDTO::getAmplifier, PotionEffectData::setAmplifier))
                .addMappings(mapper -> mapper.map(PotionEffectDTO::getDuration, PotionEffectData::setDuration));
    }

    private void configureColorMapper() {
        modelMapper.createTypeMap(ColorDTO.class, ColorData.class)
                .addMappings(mapper -> mapper.map(ColorDTO::getRed, ColorData::setRed))
                .addMappings(mapper -> mapper.map(ColorDTO::getGreen, ColorData::setGreen))
                .addMappings(mapper -> mapper.map(ColorDTO::getBlue, ColorData::setBlue));
    }

    private void configureItemMapper() {
        //MATERIAL
        this.baseTypeMap = modelMapper
                .createTypeMap(ItemConfigurationDTO.class, ItemConfiguration.class)
                .addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<ItemConfigurationDTO, Material>) context -> {
							final String material = context.getSource().getMaterial();

							if(material == null) {
								throw new IllegalArgumentException("The material must be specified");
							}
							try {
								return Material.valueOf(material.toUpperCase());
							} catch (final IllegalArgumentException exception) {
								throw new IllegalArgumentException(String.format("Invalid material: %s", material));
							}
						}).map(source).setMaterial(Material.STICK);
					}
				})
                //ENCHANTMENTS
                .addMappings(new PropertyMap<>() {
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
												throw new IllegalArgumentException(String.format("Invalid enchantment name: %s", entry.getKey()));
											}

											return enchantment;
										}, Map.Entry::getValue)))
								.map(source).setEnchantments(new HashMap<>());
					}
				})
                //POTION TYPE
                .addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<ItemConfigurationDTO, PotionType>) context -> {
							final var potionType = context.getSource().getPotionType();

							if(potionType == null) {
								return null;
							}
							try {
								return PotionType.valueOf(potionType.toUpperCase());
							} catch (final IllegalArgumentException exception) {
								throw new IllegalArgumentException(String.format("Invalid potion type: %s", potionType));
							}
						}).map(source).setPotionType(null);
					}
				})
                //POTION EFFECT DATA
                .addMappings(new PropertyMap<>() {
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
												throw new IllegalArgumentException(String.format("Invalid potion effect type: %s", potionEffectTypeId));
											}
											final var potionEffectData = modelMapper.map(entry.getValue(), PotionEffectData.class);
											potionEffectData.setPotionEffectType(potionEffectType);

											return potionEffectData;
										})
										.toList())
								.map(source).setPotionEffectData(new ArrayList<>());
					}
				})
                //COLOUR DATA
                .addMappings(new PropertyMap<>() {
					@Override
					protected void configure() {
						using((Converter<ItemConfigurationDTO, ColorData>) context -> {
							if(context.getSource().getColor() == null) {
								return null;
							}

							return modelMapper.map(context.getSource().getColor(), ColorData.class);
						}).map(source).setColorData(null);
					}
				})
                //Straightforward mappings
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
        modelMapper
                .typeMap(ItemConfigurationDTO.ConfigurationMap.class, ItemConfiguration.ConfigurationMap.class)
                .addMappings(new PropertyMap<>() {
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

    public List<ItemStack> asItemStacks(List<ItemConfigurationDTO> itemConfigurationDTOList) {
        return itemConfigurationDTOList
                .stream()
                .map(itemConfigurationDTO -> ItemMapper.getInstance()
                        .getModelMapper()
                        .map(itemConfigurationDTO, ItemConfiguration.class)
                        .toItemStack())
                .toList();
    }

    public static ItemMapper getInstance() {
        return INSTANCE;
    }

}
