package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.*;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicForgeMenu;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ProfessionManager {

	public final Profession BLACKSMITH = Profession.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.addWorkstations(
					Profession.Workstation.newBuilder()
							.material(Material.BLAST_FURNACE)
							.restricted(true)
							.fuel(Material.LAVA)
							.craftStationMenu(ForgeMenu.class)
							.dynamicCraftMenu(DynamicForgeMenu.class)
							.build(),
					Profession.Workstation.newBuilder()
							.material(Material.ANVIL)
							.restricted(false)
							.craftStationMenu(AnvilMenu.class)
							.dynamicCraftMenu(DynamicAnvilMenu.class)
							.build(),
					Profession.Workstation.newBuilder()
							.material(Material.CHIPPED_ANVIL)
							.restricted(false)
							.craftStationMenu(AnvilMenu.class)
							.dynamicCraftMenu(DynamicAnvilMenu.class)
							.build(),
					Profession.Workstation.newBuilder()
							.material(Material.DAMAGED_ANVIL)
							.restricted(false)
							.craftStationMenu(AnvilMenu.class)
							.dynamicCraftMenu(DynamicAnvilMenu.class)
							.build()
			)
			.build();
	public final Profession ENCHANTER = Profession.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.addWorkstations(Profession.Workstation.newBuilder()
					.material(Material.ENCHANTING_TABLE)
					.restricted(false)
					.craftStationMenu(EnchantMenu.class)
					.build())
			.build();
	public final Profession ALCHEMIST = Profession.newBuilder()
			.professionType(ProfessionType.ALCHEMIST)
			.addWorkstations(Profession.Workstation.newBuilder()
					.material(Material.BREWING_STAND)
					.restricted(true)
					.craftStationMenu(PotionMenu.class)
					.build())
			.build();
	public final Profession BARMAN = Profession.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.addWorkstations(Profession.Workstation.newBuilder()
					.material(Material.BREWING_STAND)
					.restricted(true)
					.craftStationMenu(PotionMenu.class)
					.build())
			.build();
	public final Profession JOURNALIST = Profession.newBuilder()
			.professionType(ProfessionType.JOURNALIST)
			.build();
	public final Profession FARMER = Profession.newBuilder()
			.professionType(ProfessionType.FARMER)
			.build();
	public final Profession PRIEST = Profession.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.addWorkstations(Profession.Workstation.newBuilder()
					.material(Material.LECTERN)
					.restricted(false)
					.craftStationMenu(LecternMenu.class)
					.build())
			.build();
	public final Profession DOCTOR = Profession.newBuilder()
			.professionType(ProfessionType.DOCTOR)
			.addWorkstations(Profession.Workstation.newBuilder()
					.material(Material.BREWING_STAND)
					.restricted(true)
					.craftStationMenu(PotionMenu.class)
					.build())
			.build();
	public final Profession POLICEMAN = Profession.newBuilder()
			.professionType(ProfessionType.POLICEMAN)
			.build();

	public List<Profession> getProfessions() {
		final var fields = Arrays.stream(ProfessionManager.class.getDeclaredFields())
				.filter(f -> Profession.class.isAssignableFrom(f.getType()))
				.collect(Collectors.toList());
		final List<Profession> professions = new ArrayList<>();

		for(final var field : fields) {
			try {
				final var profession = (Profession) field.get(ProfessionManager.class);
				professions.add(profession);
			} catch (IllegalAccessException e) {
				Formatter.error("Error retrieving craft items from CraftManager");
			}
		}

		return professions;
	}

	public Optional<Profession> getProfession(final ProfessionType professionType) {
		return getProfessions().stream().filter(profession -> profession.getType() == professionType).findFirst();
	}

	public boolean canUseWorkstation(final Profession profession, final Material material) {
		//Check if the mat is a profession's workstation
		if(profession.hasWorkstation(material)) {
			return true;
		}
		//If not, check if the mat is a restricted workstation from another profession
		for(final var otherProfession : ProfessionManager.getProfessions()) {
			if(otherProfession.isWorkstationRestricted(material)) {
				return false;
			}
		}

		return true;
	}

}
