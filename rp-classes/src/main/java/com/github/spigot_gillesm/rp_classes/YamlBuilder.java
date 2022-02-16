package com.github.spigot_gillesm.rp_classes;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Optional;

public abstract class YamlBuilder<T> {

	protected final ConfigurationSection configuration;

	protected YamlBuilder(final ConfigurationSection configuration) {
		this.configuration = configuration;
	}

	protected YamlBuilder(final File file) {
		this(FileUtils.getConfiguration(file));
	}

	protected boolean containsFields(final ConfigurationSection section, final String... fields) {
		var contains = true;

		for(final var field : fields) {
			if(!section.contains(field)) {
				Formatter.error("Missing mandatory field: " + field + ".");
				contains = false;
			}
		}

		return contains;
	}

	public abstract Optional<T> getBuilderFromFile(String id);

	public Optional<T> getBuilderFromFile() {
		return getBuilderFromFile("");
	}

	/*public static YamlBuilder<? extends YamlBuilder<?>> fromConfiguration(final ConfigurationSection configuration) {
		return new YamlBuilder(configuration);
	}

	public static YamlBuilder<? extends YamlBuilder<?>> fromFile(final File file) {
		return new YamlBuilder(FileUtils.getConfiguration(file));
	}*/

}
