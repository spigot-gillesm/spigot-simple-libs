package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.DynamicCraft;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class Profession {

	@Getter
	private final Set<Workstation> workstations;

	@Getter
	private final String displayName;

	@Getter
	private final Material icon;

	@Getter
	private final List<String> description;

	private Profession(final Builder builder) {
		this.workstations = builder.workstations;
		this.displayName = builder.displayName;
		this.icon = builder.icon;
		this.description = builder.description;
	}

	/**
	 * Display the workstation's menu if one exists for the given block.
	 * 
	 * @param player the player
	 * @param block the block used as a workstation
	 * @param itemStack the item stack involved if one exists
	 *
	 * @return true if a menu has been displayed to the player
	 */
	public boolean displayWorkstationMenu(final Player player, final Block block, final ItemStack itemStack) {
		final var craftItem = CraftManager.getCraftItem(itemStack);

		for(final var workstation : workstations) {
			if(workstation.getMaterials().contains(block.getType())) {
				if(workstation.isFueled(block)) {
					//Check if the item is an item that needs a dyn craft menu + check if the menu is the right one
					craftItem.filter(item -> item instanceof DynamicCraft
									&& workstation.hasDynamicCraftMenu()
									&& item.getDynamicCraftMenu().equals(workstation.getDynamicMenu()))
							//If there's no craft item involved or the craft item isn't a dyn craft -> open regular inventory
							.ifPresentOrElse(item -> {
										//If the item has been reworked -> remove the item
										if(((DynamicCraft) item).reWork(player, itemStack)) {
											player.getInventory().removeItem(itemStack);
										}
									},
									() -> PluginUtil.instantiateFromClass(workstation.getMenu())
											.ifPresent(menu -> menu.display(player)));

					return true;
				} else {
					Formatter.tell(player, "&cThe workstation must be fueled in order to work");
				}
			}
		}

		return false;
	}

	public boolean hasWorkstation(final Material material) {
		for(final var workstation : workstations) {
			if(workstation.getMaterials().contains(material)) {
				return true;
			}
		}

		return false;
	}

	public boolean isWorkstationRestricted(final Material material) {
		for(final var workstation : workstations) {
			if(workstation.getMaterials().contains(material) && workstation.isRestricted()) {
				return true;
			}
		}
		return false;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private final Set<Workstation> workstations = new HashSet<>();

		private String displayName;

		private Material icon;

		private List<String> description = new ArrayList<>();

		public Builder addWorkstations(final Workstation... workstations) {
			this.workstations.addAll(List.of(workstations));
			return this;
		}

		public Builder displayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder icon(final Material icon) {
			this.icon = icon;
			return this;
		}

		public Builder description(final List<String> description) {
			this.description = description;
			return this;
		}

		public Builder addDescriptions(final String... descriptions) {
			this.description.addAll(List.of(descriptions));
			return this;
		}

		public Profession build() {
			return new Profession(this);
		}

	}

	public static class YamlProfession {

		public final ConfigurationSection configuration;

		private YamlProfession(final ConfigurationSection configuration) {
			this.configuration = configuration;
		}

		private YamlProfession(final File file) {
			this(FileUtils.getConfiguration(file));
		}

		private YamlProfession(final String path) {
			this(FileUtils.getResource(path));
		}

		public Optional<Builder> getBuilderFromFile(final String id) {
			if(!configuration.isConfigurationSection(id)) {
				throw new IllegalArgumentException("Id " + id + " doesn't exist in this context.");
			}
			final var section = configuration.getConfigurationSection(id);
			final var builder = Profession.newBuilder();

			if(!section.contains("workstations")) {
				Formatter.error("All professions must contain workstations.");
				return Optional.empty();
			}
			if(!section.contains("display-name")) {
				Formatter.error("All professions must contain display-name.");
				return Optional.empty();
			}
			if(!section.contains("icon")) {
				Formatter.error("All professions must contain icon.");
				return Optional.empty();
			}
			if(!section.contains("description")) {
				Formatter.error("All professions must contain description.");
				return Optional.empty();
			}

			for(final var stationName : section.getStringList("workstations")) {
				final var workstation = WorkstationManager.getWorkstation(stationName);

				if(workstation.isPresent()) {
					builder.addWorkstations(workstation.get());
				} else {
					Formatter.error("Unknown workstation for profession " + id + ".");
					return Optional.empty();
				}
			}
			builder.displayName(section.getString("display-name"));
			builder.description(section.getStringList("description"));

			try {
				builder.icon(Material.valueOf(section.getString("icon").toUpperCase()));
			} catch(final IllegalArgumentException e) {
				Formatter.error("Invalid icon for workstation " + id + ".");
				return Optional.empty();
			}

			return Optional.of(builder);
		}

		public Optional<Builder> getBuilderFromFile() {
			return getBuilderFromFile("");
		}

		public Optional<Profession> getProfessionFromFile(final String id) {
			return getBuilderFromFile(id).map(Profession.Builder::build);
		}

		public Optional<Profession> getProfessionFromFile() {
			return getProfessionFromFile("");
		}

		public static YamlProfession fromConfiguration(final ConfigurationSection configuration) {
			return new YamlProfession(configuration);
		}

		public static YamlProfession fromFile(final File file) {
			return new YamlProfession(FileUtils.getConfiguration(file));
		}

		public static YamlProfession fromPath(final String path) {
			return new YamlProfession(FileUtils.getResource(path));
		}

	}

}
