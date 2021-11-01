package com.github.spigot_gillesm.file_utils;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class FileUtils {

	public String PLUGIN_DATA_FOLDER_PATH = "";

	public File getFile(@NotNull final String path, final boolean createIfNotExists) {
		final var file = new File(path);

		if(!file.exists() && createIfNotExists) {
			//Make sure the parent file exists and create it if needed
			if(file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}

			try {
				file.createNewFile();
				return file;
			} catch (IOException e) {
				Formatter.error("Error creating file " + path);
				return null;
			}
		} else {
			return file;
		}
	}

	public File getFile(@NotNull final String path) {
		return getFile(path, true);
	}

	public File getResource(@NotNull final String path, final boolean createIfNotExists) {
		return getFile(StringUtils.isEmpty(PLUGIN_DATA_FOLDER_PATH) ? path : PLUGIN_DATA_FOLDER_PATH + File.separator + path,
				createIfNotExists);
	}

	public File getResource(@NotNull final String path) {
		return getFile(StringUtils.isEmpty(PLUGIN_DATA_FOLDER_PATH) ? path : PLUGIN_DATA_FOLDER_PATH + File.separator + path);
	}

	public YamlConfiguration getConfiguration(@NotNull final File file) {
		if(!file.getName().endsWith(".yml")) {
			throw new IllegalArgumentException("Configuration file must be yaml files.");
		}
		final var configuration = new YamlConfiguration();

		try {
			configuration.load(file);
			return configuration;
		} catch (InvalidConfigurationException | IOException e) {
			Formatter.error("Error loading configuration " + file.getPath());
			return null;
		}
	}

	public YamlConfiguration getConfiguration(@NotNull final String path) {
		return getConfiguration(getResource(path));
	}

	public YamlConfiguration saveConfiguration(@NotNull final File file, @NotNull final YamlConfiguration configuration) {
		if(!file.getName().endsWith(".yml")) {
			throw new IllegalArgumentException("Configuration file must be yaml files.");
		}

		try {
			configuration.save(file);
		} catch (final IOException e) {
			Formatter.error("Error saving configuration " + file.getPath());
		}

		return configuration;
	}

	public YamlConfiguration saveConfiguration(@NotNull final File file) {
		return saveConfiguration(file, getConfiguration(file));
	}

	public boolean deleteFile(@NotNull final File file) {
		try {
			return Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			Formatter.error("Error deleting file " + file.getPath());
			return false;
		}
	}

	public YamlConfiguration clearContent(@NotNull final File file) {
		try {
			if(deleteFile(file) && file.createNewFile()) {
				return getConfiguration(file);
			} else {
				Formatter.error("Could not delete and/or re-creating file " + file.getPath());
				return null;
			}
		} catch (IOException e) {
			Formatter.error("Error clearing file " + file.getPath());
			return null;
		}
	}

}
