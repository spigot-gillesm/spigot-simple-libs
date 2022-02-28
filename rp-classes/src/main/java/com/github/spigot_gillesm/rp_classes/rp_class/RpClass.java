package com.github.spigot_gillesm.rp_classes.rp_class;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.rp_classes.GearRestriction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.*;

public class RpClass {

	@Getter
	private final String id;

	@Getter
	private final Material icon;

	@Getter
	private final String displayName;

	@Getter
	private final List<String> description;

	@Getter
	private final List<GearRestriction> gearRestrictions;

	/*@Getter
	private final String resourceName;

	private final Map<Statistic, Double> baseStats;

	private final Map<Statistic, Double> scaleStats;

	private final Set<TalentTree> talents = new HashSet<>();*/

	private RpClass(final Builder builder) {
		this.id = builder.id;
		this.icon = builder.icon;
		this.displayName = builder.displayName;
		this.description = builder.description;
		this.gearRestrictions = builder.gearRestrictions;
		/*this.resourceName = builder.resourceName;
		this.baseStats = builder.baseStats;
		this.scaleStats = builder.scaleStats;*/
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Builder {

		private String id;

		private Material icon;

		private String displayName;

		private List<String> description = new ArrayList<>();

		private List<GearRestriction> gearRestrictions = new ArrayList<>();

		/*private String resourceName;

		private final Map<Statistic, Double> baseStats = new EnumMap<>(Statistic.class);

		private final Map<Statistic, Double> scaleStats = new EnumMap<>(Statistic.class);*/

		public Builder id(final String id) {
			this.id = id;
			return this;
		}

		public Builder icon(final Material icon) {
			this.icon = icon;
			return this;
		}

		public Builder displayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder description(final List<String> description) {
			this.description = description;
			return this;
		}

		public Builder addDescription(final String... description) {
			this.description.addAll(List.of(description));
			return this;
		}

		public Builder gearRestrictions(final List<GearRestriction> gearRestrictions) {
			this.gearRestrictions = gearRestrictions;
			return this;
		}

		/*public Builder resourceName(final String resourceName) {
			this.resourceName = resourceName;
			return this;
		}

		public Builder addBaseStat(final Statistic statistic, final double value) {
			baseStats.put(statistic, value);
			return this;
		}

		public Builder addScaleStat(final Statistic statistic, final double value) {
			scaleStats.put(statistic, value);
			return this;
		}*/

		public RpClass build() {
			if(icon == null) {
				throw new IllegalArgumentException("icon cannot be null.");
			}
			if(displayName == null) {
				throw new IllegalArgumentException("displayName cannot be null.");
			}

			return new RpClass(this);
		}

	}

	public static class YamlClass {

		public final ConfigurationSection configuration;

		private YamlClass(final ConfigurationSection configuration) {
			this.configuration = configuration;
		}

		private YamlClass(final File file) {
			this(FileUtils.getConfiguration(file));
		}

		private List<GearRestriction> loadGearRestrictions(final ConfigurationSection section) {
			if(section.contains("gear-restrictions")) {
				final List<GearRestriction> gearRestrictions = new ArrayList<>();

				for(final var restriction : section.getStringList("gear-restrictions")) {
					try {
						gearRestrictions.add(GearRestriction.valueOf(restriction.toUpperCase()));
					} catch(final IllegalArgumentException exception) {
						Formatter.error("Gear restriction " + restriction.toUpperCase() + " doesn't exist.");
					}
				}

				return gearRestrictions;
			} else {
				return Collections.emptyList();
			}
		}

		private boolean containsFields(final ConfigurationSection section, final String... fields) {
			var contains = true;

			for(final var field : fields) {
				if(!section.contains(field)) {
					Formatter.error("All classes must contain " + field + ".");
					contains = false;
				}
			}

			return contains;
		}

		public Optional<Builder> getBuilderFromFile(final String id) {
			if(!configuration.isConfigurationSection(id)) {
				throw new IllegalArgumentException("Id " + id + " doesn't exist in this context.");
			}
			final var section = configuration.getConfigurationSection(id);
			final var builder = RpClass.newBuilder();

			if(!containsFields(section, "display-name", "icon"/*"resource-name", "base-resource", "scale-resource",
					"base-health", "scale-health", */)) {
				return Optional.empty();
			}
			final var restrictions = loadGearRestrictions(section);

			if(restrictions.isEmpty()) {
				Formatter.error("Class " + id + " has no gear restrictions.");
				return Optional.empty();
			}
			builder
					.id(id)
					.displayName(section.getString("display-name"))
					.description(section.getStringList("description"))
					.gearRestrictions(restrictions);

					//.resourceName(section.getString("resource-name"))
					/*.addBaseStat(Statistic.HEALTH, section.getDouble("base-health"))
					.addBaseStat(Statistic.MANA, section.getDouble("base-resource"))
					.addScaleStat(Statistic.HEALTH, section.getDouble("scale-health"))
					.addScaleStat(Statistic.MANA, section.getDouble("scale-resource"));*/

			try {
				builder.icon(Material.valueOf(section.getString("icon").toUpperCase()));
			} catch(final IllegalArgumentException e) {
				Formatter.error("Invalid icon for class " + id + ".");
				return Optional.empty();
			}

			return Optional.of(builder);
		}

		public Optional<Builder> getBuilderFromFile() {
			return getBuilderFromFile("");
		}

		public Optional<RpClass> getClassFromFile(final String id) {
			return getBuilderFromFile(id).map(RpClass.Builder::build);
		}

		public Optional<RpClass> getClassFromFile() {
			return getClassFromFile("");
		}

		public static YamlClass fromConfiguration(final ConfigurationSection configuration) {
			return new YamlClass(configuration);
		}

		public static YamlClass fromFile(final File file) {
			return new YamlClass(FileUtils.getConfiguration(file));
		}

	}

}
