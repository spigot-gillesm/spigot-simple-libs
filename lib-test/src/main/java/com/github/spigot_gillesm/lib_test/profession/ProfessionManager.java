package com.github.spigot_gillesm.lib_test.profession;

import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.util.*;

@UtilityClass
public class ProfessionManager {

	private final Map<String, Profession> loadedProfessions = new HashMap<>();

	/*public final Workstation FORGE = Workstation.newBuilder()
			.addMaterials(Material.BLAST_FURNACE)
			.restricted(true)
			.fuel(Material.LAVA)
			.menu(ForgeMenu.class)
			.dynamicMenu(DynamicForgeMenu.class)
			.build();

	public final Workstation ANVIL = Workstation.newBuilder()
			.addMaterials(Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL)
			.restricted(false)
			.menu(AnvilMenu.class)
			.dynamicMenu(DynamicAnvilMenu.class)
			.build();

	public final Workstation ENCHANTING_TABLE = Workstation.newBuilder()
			.addMaterials(Material.ENCHANTING_TABLE)
			.restricted(false)
			.menu(EnchantMenu.class)
			.dynamicMenu(DynamicEnchantMenu.class)
			.build();

	public final Workstation LECTERN = Workstation.newBuilder()
			.addMaterials(Material.LECTERN)
			.restricted(false)
			.menu(LecternMenu.class)
			.build();

	public final Workstation BREWING_STAND = Workstation.newBuilder()
			.addMaterials(Material.BREWING_STAND)
			.restricted(true)
			.menu(PotionMenu.class)
			.build();

	public final Profession BLACKSMITH = Profession.newBuilder()
			.professionType(ProfessionType.BLACKSMITH)
			.addWorkstations(FORGE, ANVIL)
			.build();
	public final Profession ENCHANTER = Profession.newBuilder()
			.professionType(ProfessionType.ENCHANTER)
			.addWorkstations(ENCHANTING_TABLE)
			.build();
	public final Profession ALCHEMIST = Profession.newBuilder()
			.professionType(ProfessionType.ALCHEMIST)
			.addWorkstations(BREWING_STAND)
			.build();
	public final Profession BARMAN = Profession.newBuilder()
			.professionType(ProfessionType.BARMAN)
			.addWorkstations(BREWING_STAND)
			.build();
	public final Profession JOURNALIST = Profession.newBuilder()
			.professionType(ProfessionType.JOURNALIST)
			.build();
	public final Profession FARMER = Profession.newBuilder()
			.professionType(ProfessionType.FARMER)
			.build();
	public final Profession PRIEST = Profession.newBuilder()
			.professionType(ProfessionType.PRIEST)
			.addWorkstations(LECTERN)
			.build();
	public final Profession DOCTOR = Profession.newBuilder()
			.professionType(ProfessionType.DOCTOR)
			.addWorkstations(BREWING_STAND)
			.build();
	public final Profession POLICEMAN = Profession.newBuilder()
			.professionType(ProfessionType.POLICEMAN)
			.build();*/

	public void loadProfessions() {
		loadedProfessions.clear();
		Formatter.info("Loading Professions...");
		final var configuration = FileUtils.getConfiguration("professions.yml");

		for(final String id : configuration.getKeys(false)) {
			if(configuration.isConfigurationSection(id)) {
				Formatter.info("Loading profession " + id + ".");
				Profession.YamlProfession.fromConfiguration(configuration).getProfessionFromFile(id)
						.ifPresentOrElse(w -> loadedProfessions.put(id.toUpperCase(), w),
								() -> Formatter.warning("Could not load profession " + id + "."));
			}
		}
		Formatter.info("Loaded " + loadedProfessions.size() + " profession(s).");
	}

	public List<Profession> getProfessions() {
		return new ArrayList<>(loadedProfessions.values());
	}

	public Optional<Profession> getProfession(final String id) {
		return loadedProfessions.containsKey(id.toUpperCase()) ? Optional.of(loadedProfessions.get(id.toUpperCase())) : Optional.empty();
	}

	public String getId(final Profession profession) {
		for(final var set : loadedProfessions.entrySet()) {
			if(set.getValue().equals(profession)) {
				return set.getKey();
			}
		}

		return "";
	}

	public static boolean privilegeExists(final Privilege privilege) {
		for(final var profession : getProfessions()) {
			if(profession.hasPrivilege(privilege)) {
				return true;
			}
		}

		return false;
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
