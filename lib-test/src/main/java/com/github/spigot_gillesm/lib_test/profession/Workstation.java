package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.menu.MenuType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Workstation {

	@Getter
	private final Set<Material> materials;

	@Getter
	private final boolean restricted;

	@Getter
	private final Material fuel;

	@Getter
	private final Class<? extends CraftStationMenu> menu;

	@Getter
	private final Class<? extends DynamicCraftMenu> dynamicMenu;

	private Workstation(final Builder builder) {
		this.materials = builder.materials;
		this.restricted = builder.restricted;
		this.fuel = builder.fuel;
		this.menu = builder.menu;
		this.dynamicMenu = builder.dynamicMenu;
	}

	public boolean hasDynamicCraftMenu() {
		return dynamicMenu != null;
	}

	public boolean isFueled(final Block block) {
		if(fuel == null) {
			return true;
		} else {
			var isFueled = false;

			for (var x = -1; x <= 1; x++) {
				for (var z = -1; z <= 1; z++) {
					if(block.getLocation().add(x, 0, z).getBlock().getType() == fuel) {
						isFueled = true;
					}
				}
			}
			return isFueled;
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder {

		private Set<Material> materials = new HashSet<>();

		private boolean restricted = false;

		private Material fuel;

		private Class<? extends CraftStationMenu> menu;

		private Class<? extends DynamicCraftMenu> dynamicMenu;

		public Builder materials(final Set<Material> materials) {
			this.materials = materials;
			return this;
		}

		public Builder addMaterials(final Material... materials) {
			this.materials.addAll(List.of(materials));
			return this;
		}

		public Builder restricted(final boolean restricted) {
			this.restricted = restricted;
			return this;
		}

		public Builder fuel(final Material fuel) {
			this.fuel = fuel;
			return this;
		}

		public Builder menu(final Class<? extends CraftStationMenu> menu) {
			this.menu = menu;
			return this;
		}

		public Builder dynamicMenu(final Class<? extends DynamicCraftMenu> dynamicMenu) {
			this.dynamicMenu = dynamicMenu;
			return this;
		}

		public Workstation build() {
			if(materials.isEmpty()) {
				throw new IllegalArgumentException("Materials cannot be empty.");
			}
			if(menu == null) {
				throw new IllegalArgumentException("Craft station menu cannot be null.");
			}

			return new Workstation(this);
		}

	}
	
	public static class YamlWorkstation {

		public final ConfigurationSection configuration;

		private YamlWorkstation(final ConfigurationSection configuration) {
			this.configuration = configuration;
		}

		private YamlWorkstation(final File file) {
			this(FileUtils.getConfiguration(file));
		}

		private YamlWorkstation(final String path) {
			this(FileUtils.getResource(path));
		}

		public Optional<Builder> getBuilderFromFile(final String id) {
			if(!configuration.isConfigurationSection(id)) {
				throw new IllegalArgumentException("Id " + id + " doesn't exist in this context.");
			}
			final var section = configuration.getConfigurationSection(id);
			final var builder = Workstation.newBuilder();

			if(!section.contains("materials")) {
				Formatter.error("All workstations must contain materials.");
				return Optional.empty();
			}
			if(!section.contains("menu")) {
				Formatter.error("All workstations must contain menu.");
				return Optional.empty();
			}
			for(final var material : section.getStringList("materials")) {
				try {
					builder.addMaterials(Material.valueOf(material.toUpperCase()));
				} catch(final IllegalArgumentException e) {
					Formatter.error("Invalid material for workstation " + id + ": " + material + ".");
					return Optional.empty();
				}
			}
			final var menu = MenuType.getMenuType(section.getString("menu").toUpperCase());

			if(menu != null) {
				builder.menu(menu.getMenu());
				builder.dynamicMenu(menu.getDynamicMenu());
			} else {
				Formatter.error("Invalid menu for workstation " + id + ".");
				return Optional.empty();
			}
			if(section.contains("fuel")) {
				final var fuel = section.getString("fuel");
				try {
					builder.fuel(Material.valueOf(fuel.toUpperCase()));
				} catch(final IllegalArgumentException e) {
					Formatter.error("Invalid fuel for workstation " + id + ".");
					return Optional.empty();
				}
			}
			builder.restricted(section.getBoolean("restricted", false));

			return Optional.of(builder);
		}

		public Optional<Builder> getBuilderFromFile() {
			return getBuilderFromFile("");
		}

		public Optional<Workstation> getWorkstationFromFile(final String id) {
			return getBuilderFromFile(id).map(Builder::build);
		}

		public Optional<Workstation> getWorkstationFromFile() {
			return getWorkstationFromFile("");
		}

		public static YamlWorkstation fromConfiguration(final ConfigurationSection configuration) {
			return new YamlWorkstation(configuration);
		}

		public static YamlWorkstation fromFile(final File file) {
			return new YamlWorkstation(FileUtils.getConfiguration(file));
		}

		public static YamlWorkstation fromPath(final String path) {
			return new YamlWorkstation(FileUtils.getResource(path));
		}
		
	}

}