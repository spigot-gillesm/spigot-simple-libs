package com.github.spigot_gillesm.rp_classes.rp_class;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class ClassManager {

	private final Map<String, RpClass> LOADED_CLASSES = new LinkedHashMap<>();

	public void loadClasses() {
		LOADED_CLASSES.clear();
		Formatter.info("Loading classes...");
		final var configuration = FileUtils.getConfiguration("classes.yml");

		for(final String id : configuration.getKeys(false)) {
			if(configuration.isConfigurationSection(id)) {
				RpClass.YamlClass.fromConfiguration(configuration).getClassFromFile(id)
						.ifPresentOrElse(c -> LOADED_CLASSES.put(id.toUpperCase(), c),
								() -> Formatter.warning("Could not load class " + id + "."));
			}
		}
		Formatter.info("Loaded " + LOADED_CLASSES.size() + " class(es).");
	}

	public List<RpClass> getClasses() {
		return new ArrayList<>(LOADED_CLASSES.values());
	}

	public Optional<RpClass> getRpClass(final String id) {
		return LOADED_CLASSES.entrySet().stream()
				.filter(entry -> entry.getKey().equals(id))
				.map(Map.Entry::getValue)
				.findFirst();
	}

	public String getId(final RpClass rpClass) {
		//Since all the classes are registered in the map -> impossible to have an empty optional
		return LOADED_CLASSES.entrySet().stream()
				.filter(entry -> entry.getValue().equals(rpClass))
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(null);
	}

}
