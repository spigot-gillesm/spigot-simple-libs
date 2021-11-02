package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class WorkstationManager {

	private final Map<String, Workstation> loadedWorkstations = new HashMap<>();

	public void loadWorkstations() {
		loadedWorkstations.clear();
		Formatter.info("Loading Workstations...");
		final var configuration = FileUtils.getConfiguration("workstations.yml");

		for(final String id : configuration.getKeys(false)) {
			if(configuration.isConfigurationSection(id)) {
				Formatter.info("Loading workstation " + id + ".");
				Workstation.YamlWorkstation.fromConfiguration(configuration).getWorkstationFromFile(id)
						.ifPresentOrElse(w -> loadedWorkstations.put(id, w),
								() -> Formatter.warning("Could not load workstation " + id + "."));
			}
		}
		Formatter.info("Loaded " + loadedWorkstations.size() + " workstation(s).");
	}

	public Optional<Workstation> getWorkstation(final String id) {
		return loadedWorkstations.containsKey(id) ? Optional.of(loadedWorkstations.get(id)) : Optional.empty();
	}

	public String getId(@NotNull final Workstation workstation) {
		for(final var set : loadedWorkstations.entrySet()) {
			if(set.getValue().equals(workstation)) {
				return set.getKey();
			}
		}

		return "";
	}

}
