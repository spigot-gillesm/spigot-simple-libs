package com.github.spigot_gillesm.item_lib.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.spigot_gillesm.item_lib.dto.ItemConfigurationDTO;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ItemLoader {

    private static final ItemLoader INSTANCE = new ItemLoader();

    private static final String YAML_EXTENSION = ".yml";

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

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
        } else {
            throw new IllegalArgumentException("The file must be a .yml file");
        }
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
            return ItemMapper.getInstance()
                    .getModelMapper()
                    .map(yamlMapper.readValue(file, ItemConfigurationDTO.ConfigurationMap.class),
                            ItemConfiguration.ConfigurationMap.class)
                    .getConfigurationMap();
        } else {
            throw new IllegalArgumentException("The file must be a .yml file");
        }
    }

    public static ItemLoader getInstance() {
        return INSTANCE;
    }

}
