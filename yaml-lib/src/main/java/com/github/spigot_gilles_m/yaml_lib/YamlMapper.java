package com.github.spigot_gilles_m.yaml_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class YamlMapper {

	public <T> T readValue(final ConfigurationSection configurationSection, final Class<T> clazz) {
		final var constructor = Arrays.stream(clazz.getDeclaredConstructors()).findFirst();
		if(constructor.isPresent()) {
			try {
				final var object = clazz.cast(constructor.get().newInstance());
				Formatter.info("Class: " + clazz);
				final var field = clazz.getDeclaredField("id");
				field.setAccessible(true);
				field.set(object, configurationSection.getName());

				return object;
			} catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
