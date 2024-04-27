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

	public File getFile(@NotNull final String path, final boolean createIfNotExists) throws IOException {
		final var file = new File(path);

		if(!file.exists() && createIfNotExists) {
			//Make sure the parent file exists and create it if needed
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(path.endsWith("/") || path.endsWith(File.separator)) {
				file.mkdir();
			} else {
				file.createNewFile();
			}
		}

		return file;
	}

	public File getFile(@NotNull final String path) throws IOException {
		return getFile(path, true);
	}

	public File getResource(@NotNull final String path, final boolean createIfNotExists) throws IOException {
		return getFile(StringUtils.isEmpty(PLUGIN_DATA_FOLDER_PATH) ? path : PLUGIN_DATA_FOLDER_PATH + File.separator + path,
				createIfNotExists);
	}

	public File getResource(@NotNull final String path) throws IOException {
		return getFile(StringUtils.isEmpty(PLUGIN_DATA_FOLDER_PATH) ? path : PLUGIN_DATA_FOLDER_PATH + File.separator + path);
	}

	public YamlConfiguration getConfiguration(@NotNull final File file) throws IOException, InvalidConfigurationException {
		if(!file.getName().endsWith(".yml")) {
			throw new IllegalArgumentException("Configuration file must be yaml files.");
		}
		final var configuration = new YamlConfiguration();
		configuration.load(file);

		return configuration;

	}

	public YamlConfiguration getConfiguration(@NotNull final String path) throws IOException, InvalidConfigurationException {
		return getConfiguration(getResource(path));
	}

	public YamlConfiguration saveConfiguration(@NotNull final File file, @NotNull final YamlConfiguration configuration) throws IOException {
		if(!file.getName().endsWith(".yml")) {
			throw new IllegalArgumentException("Configuration file must be yaml files.");
		}
		configuration.save(file);

		return configuration;
	}

	public YamlConfiguration saveConfiguration(@NotNull final File file) throws IOException, InvalidConfigurationException {
		return saveConfiguration(file, getConfiguration(file));
	}

	public boolean deleteFile(@NotNull final File file) throws IOException {
		return Files.deleteIfExists(file.toPath());
	}

	public YamlConfiguration clearContent(@NotNull final File file) throws IOException, InvalidConfigurationException {
		if(deleteFile(file) && file.createNewFile()) {
			return getConfiguration(file);
		} else {
			Formatter.error("Could not delete and/or re-creating file " + file.getPath());
			return null;
		}
	}

	/**
	 * Check whether the specified resource exists.
	 *
	 * @param path the resource path. The path is prepended by the plugin data folder path if one exists.
	 * @return true of the file exists
	 */
	public boolean doResourceExists(@NotNull final String path) {
		final var resourcePath = StringUtils.isEmpty(PLUGIN_DATA_FOLDER_PATH) ? path :
				PLUGIN_DATA_FOLDER_PATH + File.separator + path;
		final var file = new File(resourcePath);

		return file.exists();
	}

}
