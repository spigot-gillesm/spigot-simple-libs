package com.github.spigot_gillesm.item_lib;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.configuration.ItemConfiguration;
import com.github.spigot_gillesm.item_lib.dto.ItemConfigurationDTO;
import org.bukkit.inventory.ItemStack;
import org.modelmapper.MappingException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemLoader {

    private static final ItemLoader INSTANCE = new ItemLoader();

    private static final String YAML_EXTENSION = ".yml";

    private static final String JSON_EXTENSION = ".json";

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    private final ObjectMapper jsonMapper = new ObjectMapper();

    protected ItemLoader() { }

    /**
     * Create a new instance of item configuration from the given configuration file.
     *
     * @param file the configuration file
     *
     * @return an instance of ItemConfiguration
     */
    public ItemConfiguration asConfigurationItem(File file) throws IOException {
        if(file.getName().endsWith(YAML_EXTENSION)) {
            return ItemMapper.getInstance()
                    .getModelMapper()
                    .map(yamlMapper.readValue(file, ItemConfigurationDTO.class), ItemConfiguration.class);
        }
        if(file.getName().endsWith(JSON_EXTENSION)) {
            return ItemMapper.getInstance()
                    .getModelMapper()
                    .map(jsonMapper.readValue(file, ItemConfigurationDTO.class), ItemConfiguration.class);
        }

        throw new IllegalArgumentException("The file must be a .yml file");
    }

    /**
     * Get a map linking the key to its item configuration.
     *
     * @param file the configuration file containing a map of string to item configuration
     *
     * @return a new Map< String, ItemConfiguration>
     */
    public Map<String, ItemConfiguration> asConfigurationItemMap(File file) throws IOException {
        if(file.getName().endsWith(YAML_EXTENSION)) {
            final LinkedHashMap<String, ItemConfigurationDTO> configurationDTOMap = yamlMapper.readValue(file, new TypeReference<>() {});

            return configurationDTOMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            entry -> ItemMapper.getInstance()
									.getModelMapper()
									.map(entry.getValue(), ItemConfiguration.class))
                    );
        } else {
            throw new IllegalArgumentException("The file must be a .yml file");
        }
    }

    /**
     * Get a map linking the key to its item stack
     *
     * @param file the configuration file containing a map of string to item stack
     *
     * @return a new LinkedHashMap< String, ItemStack>
     */
    public Map<String, ItemStack> asItemStackMap(File file) throws IOException {
        if(file.getName().endsWith(YAML_EXTENSION)) {
            final LinkedHashMap<String, ItemConfigurationDTO> configurationDTOMap = yamlMapper.readValue(file, new TypeReference<>() {});

            return configurationDTOMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> {
                                try {
                                    return ItemMapper.getInstance()
                                            .getModelMapper()
                                            .map(entry.getValue(), ItemConfiguration.class)
                                            .toItemStack();
                                } catch(final MappingException exception) {
                                    //Specify where the error occur before re-throwing the exception. Helps with debugging
                                    Formatter.error(String.format("Error mapping %s:", entry.getKey()));
                                    throw exception;
                                }
                            },
                            (key, value) -> value,
                            LinkedHashMap::new)
                    );
        } else {
            throw new IllegalArgumentException("The file must be a .yml file");
        }
    }

    public SimpleItem asSimpleItem(File file) throws IOException {
        return asConfigurationItem(file).toSimpleItem();
    }

    public ItemStack asItemStack(File file) throws IOException {
        return asConfigurationItem(file).toItemStack();
    }

    public static ItemLoader getInstance() {
        return INSTANCE;
    }

}
