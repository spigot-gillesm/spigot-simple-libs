package com.github.spigot_gillesm.epicworlds_utils;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.lib_test.RpProfessionsAPI;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

public class DependencyManager {

	private static final DependencyManager INSTANCE = new DependencyManager();

	private RpProfessionsAPI rpProfessionsAPI;
	private boolean warningSent = false;

	private MMOItems mmoItems;

	private DependencyManager() {
		loadDependencies();
	}

	private <T> Optional<T> retrieveFromAPI(final Supplier<Optional<T>> supplier) {
		if(rpProfessionsAPI == null) {
			if(!warningSent) {
				Formatter.warning("RpProfessions is not loaded. Unable to use its API.");
				warningSent = true;
			}

			return Optional.empty();
		} else {
			return supplier.get();
		}
	}

	public Optional<ItemStack> getItem(final String id) {
		return retrieveFromAPI(() -> rpProfessionsAPI.getItem(id));
	}

	private void loadRPProfessions() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("RPProfessions")) {
			Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("RPProfessions");

			try {
				rpProfessionsAPI = (RpProfessionsAPI) plugin.getClass().getDeclaredMethod("getAPI").invoke(null);
			} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				e.printStackTrace();
			}
			Formatter.info("Hooked into RPProfessions.");
		} else {
			Formatter.info("RPProfessions not found.");
		}
	}

	private void loadMmoItems() {
		if(Bukkit.getServer().getPluginManager().getPlugin("MMOItems") != null) {
			mmoItems = MMOItems.plugin;
		}
	}

	private void loadDependencies() {
		Formatter.info("Loading dependencies...");
		loadRPProfessions();
		loadMmoItems();
		Formatter.info("Done!");
	}

	public static DependencyManager getInstance() {
		return INSTANCE;
	}

}
