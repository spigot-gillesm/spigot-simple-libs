package com.github.spigot_gillesm.file_utils;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class FileUtils {

	public String PLUGIN_DATA_FOLDER_PATH = "";

	private String buildPath(String path) {
		return StringUtils.isEmpty(PLUGIN_DATA_FOLDER_PATH) ? path : PLUGIN_DATA_FOLDER_PATH + File.separator + path;
	}

	public File getFile(@NotNull String path, final boolean createIfNotExists) throws IOException {
		final var file = new File(path);

		if(!file.exists() && createIfNotExists) {
			//Make sure the parent file exists and create it if needed
			if(!file.getParentFile().exists()) {
				if(!file.getParentFile().mkdirs()) {
					Formatter.error(String.format("Error creating %s: Unable to create parent directories.", path));
				}
			}
			if(path.endsWith("/") || path.endsWith(File.separator)) {
				if(!file.mkdir()) {
					Formatter.error(String.format("Error creating %s: Unable to create directory.", path));
				}
			} else {
				if(!file.createNewFile()) {
					Formatter.error(String.format("Error creating %s: Unable to create file.", path));
				}
			}
		}

		return file;
	}

	public File getFile(@NotNull String path) throws IOException {
		return getFile(path, true);
	}

	public File getResource(@NotNull String path, final boolean createIfNotExists) throws IOException {
		return getFile(buildPath(path), createIfNotExists);
	}

	public File getResource(@NotNull String path) throws IOException {
		return getFile(buildPath(path));
	}

	public YamlConfiguration getConfiguration(@NotNull File file) throws IOException, InvalidConfigurationException {
		if(!file.getName().endsWith(".yml")) {
			throw new IllegalArgumentException(String.format("Error retrieving yaml configuration from %s: Not a YAML file.", file.getName()));
		}
		final var configuration = new YamlConfiguration();
		configuration.load(file);

		return configuration;

	}

	public YamlConfiguration getConfiguration(@NotNull String path) throws IOException, InvalidConfigurationException {
		return getConfiguration(getResource(path));
	}

	public YamlConfiguration saveConfiguration(@NotNull File file, @NotNull YamlConfiguration configuration) throws IOException {
		if(!file.getName().endsWith(".yml")) {
			throw new IllegalArgumentException(String.format("Error saving yaml configuration file %s: Not a YAML file.", file.getName()));
		}
		configuration.save(file);

		return configuration;
	}

	public YamlConfiguration saveConfiguration(@NotNull File file) throws IOException, InvalidConfigurationException {
		return saveConfiguration(file, getConfiguration(file));
	}

	public boolean deleteFile(@NotNull File file) throws IOException {
		return Files.deleteIfExists(file.toPath());
	}

	public YamlConfiguration clearContent(@NotNull File file) throws IOException, InvalidConfigurationException {
		if(deleteFile(file) && file.createNewFile()) {
			return getConfiguration(file);
		} else {
			Formatter.error(String.format("Error clearing %s content", file.getPath()));

			return null;
		}
	}

	/**
	 * Check whether the specified resource exists.
	 *
	 * @param path the resource path. The path is prepended by the plugin data folder path if one exists.
	 * @return true of the file exists
	 */
	public boolean doResourceExists(@NotNull String path) {
		return new File(buildPath(path)).exists();
	}

}
