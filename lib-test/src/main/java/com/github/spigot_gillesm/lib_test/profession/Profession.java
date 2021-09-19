package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.DynamicCraftItem;
import com.github.spigot_gillesm.lib_test.menu.CraftStationMenu;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import com.github.spigot_gillesm.rpg_stats_lib.StatManager;
import com.github.spigot_gillesm.rpg_stats_lib.Statistic;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Profession {

	@Getter
	private final ProfessionType type;

	private final Set<Workstation> workstations;

	private final double baseHealth;

	private final double baseResource;

	private Profession(final Builder builder) {
		this.type = builder.type;
		this.workstations = builder.workstations;
		this.baseHealth = builder.baseHealth;
		this.baseResource = builder.baseResource;
	}

	private void setPlayerStats(final Player player) {
		final var stats = StatManager.getHolder(player);
		stats.setStat(Statistic.HEALTH, baseHealth);
		stats.setStat(Statistic.MANA, baseResource);
	}

	public void join(final Player player) {
		DataManager.getData(player).setTagValue(PlayerTag.CLASS, type);
		setPlayerStats(player);
		PlayerManager.updatePlayerStats(player);
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
			if(workstation.material == block.getType()) {
				if(workstation.isFueled(block)) {
					//Check if the item is an item that needs a dyn craft menu + check if the menu is the right one
					craftItem.filter(item -> item instanceof DynamicCraftItem
									&& workstation.hasDynamicCraftMenu()
									&& item.getDynamicCraftMenu().equals(workstation.dynamicCraftMenu))
							//If there's no craft item involved or the craft item isn't a dyn craft -> open regular inventory
							.ifPresentOrElse(item -> {
										((DynamicCraftItem) item).reWork(player, itemStack);
										player.getInventory().removeItem(itemStack);
									},
									() -> PluginUtil.instantiateFromClass(workstation.craftStationMenu)
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
			if(workstation.material == material) {
				return true;
			}
		}

		return false;
	}

	public boolean isWorkstationRestricted(final Material material) {
		for(final var workstation : workstations) {
			if(workstation.material == material && workstation.restricted) {
				return true;
			}
		}
		return false;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private ProfessionType type;

		private final Set<Workstation> workstations = new HashSet<>();

		private double baseHealth = 20;

		private double baseResource = 20;

		private Builder() { }

		public Builder professionType(final ProfessionType professionType) {
			this.type = professionType;
			return this;
		}

		public Builder addWorkstations(final Workstation... workstations) {
			this.workstations.addAll(List.of(workstations));
			return this;
		}

		public Builder baseHealth(final double baseHealth) {
			this.baseHealth = baseHealth;
			return this;
		}

		public Builder baseResource(final double baseResource) {
			this.baseResource = baseResource;
			return this;
		}

		public Profession build() {
			if(type == null) {
				throw new IllegalArgumentException("Profession type cannot be null.");
			}

			return new Profession(this);
		}

	}

	public static class Workstation {

		private final Material material;

		private final boolean restricted;

		private final Material fuel;

		private final Class<? extends CraftStationMenu> craftStationMenu;

		private final Class<? extends DynamicCraftMenu> dynamicCraftMenu;

		private Workstation(final Builder builder) {
			this.material = builder.material;
			this.restricted = builder.restricted;
			this.fuel = builder.fuel;
			this.craftStationMenu = builder.craftStationMenu;
			this.dynamicCraftMenu = builder.dynamicCraftMenu;
		}

		public boolean hasDynamicCraftMenu() {
			return dynamicCraftMenu != null;
		}

		public boolean isFueled(final Block block) {
			if(fuel == null) {
				return true;
			} else {
				var isFueled = false;

				for(var x = -1; x <= 1; x++) {
					for(var z = -1; z <= 1; z++) {
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

		public static class Builder {

			private Material material;

			private boolean restricted = false;

			private Material fuel;

			private Class<? extends CraftStationMenu> craftStationMenu;

			private Class<? extends DynamicCraftMenu> dynamicCraftMenu;

			private Builder() { }

			public Workstation.Builder material(final Material material) {
				this.material = material;
				return this;
			}

			public Workstation.Builder restricted(final boolean restricted) {
				this.restricted = restricted;
				return this;
			}

			public Workstation.Builder fuel(final Material fuel) {
				this.fuel = fuel;
				return this;
			}

			public Workstation.Builder craftStationMenu(final Class<? extends CraftStationMenu> craftStationMenu) {
				this.craftStationMenu = craftStationMenu;
				return this;
			}

			public Workstation.Builder dynamicCraftMenu(final Class<? extends DynamicCraftMenu> dynamicCraftMenu) {
				this.dynamicCraftMenu = dynamicCraftMenu;
				return this;
			}

			public Workstation build() {
				if(material == null) {
					throw new IllegalArgumentException("Material cannot be null.");
				}
				if(craftStationMenu == null) {
					throw new IllegalArgumentException("Craft station menu cannot be null.");
				}

				return new Workstation(this);
			}

		}

	}

}
