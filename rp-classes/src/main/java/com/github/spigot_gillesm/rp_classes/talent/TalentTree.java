package com.github.spigot_gillesm.rp_classes.talent;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.rp_classes.YamlBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class TalentTree {

	@Getter
	private final String displayName;

	@Getter
	private final Material icon;

	//Each index holds several talents of the same tier (= they require the same amount of talents to be unlocked)
	private final List<Set<Talent>> talents;

	private TalentTree(final Builder builder) {
		this.displayName = builder.displayName;
		this.icon = builder.icon;
		this.talents = sortTalents(builder.talents);
	}

	private List<Set<Talent>> sortTalents(final Set<Talent> talents) {
		//i.e: 0, 5, 10, 15, 20
		final List<Integer> tempTiers = new ArrayList<>();

		//Get all the different required points (= tier)
		for(final var talent : talents) {
			tempTiers.add(talent.getRequiredPoints());
		}
		//Sort them
		Collections.sort(tempTiers);
		//i.e: 0, 5, 10, 15, 20
		//Remove the duplicates
		final Set<Integer> tiers = new LinkedHashSet<>(tempTiers);
		final List<Set<Talent>> tree = new ArrayList<>();
		var i = 0;

		//For every tier, starting from the smallest going to the biggest
		for(final var requiredPoints : tiers) {
			//final copy of i to allow stream usage
			final var finalI = i;
			//Add a new set in the list
			tree.add(new HashSet<>());
			//And place in that set all the talents having that tier (= the required amount of points)
			talents.stream().filter(t -> t.getRequiredPoints() == requiredPoints).forEach(t -> tree.get(finalI).add(t));
			i++;
		}

		return tree;
	}

	public int getWidth() {
		var width = 0;
		for(final var set : talents) {
			if(set.size() > width) {
				width = set.size();
			}
		}

		return width;
	}

	public int getLength() {
		return talents.size();
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Builder {

		private String displayName;

		private Material icon;

		//Unsorted talents
		private final Set<Talent> talents = new HashSet<>();

		public Builder displayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder icon(final Material icon) {
			this.icon = icon;
			return this;
		}

		public Builder addTalents(final Talent... talents) {
			this.talents.addAll(List.of(talents));
			return this;
		}

		public TalentTree build() {
			return new TalentTree(this);
		}

	}

	public static class YamlTalentTree extends YamlBuilder<Builder> {

		public YamlTalentTree(final ConfigurationSection section) {
			super(section);
		}

		@Override
		public Optional<Builder> getBuilderFromFile(final String id) {
			if(!configuration.isConfigurationSection(id)) {
				throw new IllegalArgumentException("Id " + id + " doesn't exist in this context.");
			}
			final var section = configuration.getConfigurationSection(id);
			final var builder = TalentTree.newBuilder();

			if(!containsFields(section, "display-name")) {
				return Optional.empty();
			}
			builder.displayName(section.getString("display-name"));

			try {
				builder.icon(Material.valueOf(section.getString("icon").toUpperCase()));
			} catch(final IllegalArgumentException e) {
				Formatter.error("Invalid icon for talent tree " + id + ".");
				return Optional.empty();
			}

			return Optional.of(builder);
		}

	}

}