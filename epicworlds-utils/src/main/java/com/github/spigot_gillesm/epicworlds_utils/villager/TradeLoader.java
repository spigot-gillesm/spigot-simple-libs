package com.github.spigot_gillesm.epicworlds_utils.villager;

import com.github.spigot_gillesm.epicworlds_utils.DependencyManager;
import com.github.spigot_gillesm.file_utils.FileUtils;
import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.YamlItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class TradeLoader {

	private static final TradeLoader INSTANCE = new TradeLoader();

	private TradeLoader() { }

	public void loadTrades() {
		final var manager = TradeManager.getInstance();
		manager.clearTrades();

		//path: /items/*.yml
		Formatter.info("Loading trades...");
		final var file = FileUtils.getResource("trades.yml");

		if(file != null && !file.isDirectory()) {
			manager.registerTrades(loadTradesFromFile(file));
		}
		Formatter.info("Loaded " + manager.getCount() + " trade(s).");
	}

	private List<VillagerTrade> loadTradesFromFile(final @NotNull File file) {
		final var conf = FileUtils.getConfiguration(file);
		final List<VillagerTrade> trades = new ArrayList<>();

		for(final var key : conf.getKeys(false)) {
			if(conf.isConfigurationSection(key) && VillagerUtil.getInstance().isProfession(key)) {
				trades.addAll(loadTradesForProfession(conf.getConfigurationSection(key)));
			} else {
				Formatter.error("Invalid villager profession in " + file.getName() + ": " + key);
			}
		}

		return trades;
	}

	private List<VillagerTrade> loadTradesForProfession(final @NotNull ConfigurationSection configurationSection) {
		Formatter.info("conf: " + configurationSection);
		final var profession = Villager.Profession.valueOf(configurationSection.getName().toUpperCase());
		final List<VillagerTrade> trades = new ArrayList<>();

		for(final var key : configurationSection.getKeys(false)) {
			if(configurationSection.isConfigurationSection(key)) {
				loadTradeFromConfiguration(profession, configurationSection.getConfigurationSection(key)).ifPresent(trades::add);
			}
		}

		return trades;
	}

	private Optional<VillagerTrade> loadTradeFromConfiguration(final Villager.Profession profession,
													 final @NotNull ConfigurationSection configurationSection) {
		final var valid = new AtomicBoolean(true);
		final var name = configurationSection.getName();
		final var builder = VillagerTrade.newBuilder()
				.profession(profession)
				.level(configurationSection.getInt("villager-level", 1))
				.chance(configurationSection.getInt("chance", 1))
				.maxUse(configurationSection.getInt("max-use", -1));

		if(!configurationSection.isConfigurationSection("result") ||
			!configurationSection.isConfigurationSection("first-ingredient")) {
			Formatter.error(String.format("Trade %s must contain result and first-ingredient sections", name));
			return Optional.empty();
		}

		loadItemStackFromConfig(configurationSection.getConfigurationSection("result")).ifPresentOrElse(
				builder::result, () -> {
					Formatter.error(String.format("Invalid result for trade: %s", name));
					valid.set(false);
				});
		loadItemStackFromConfig(configurationSection.getConfigurationSection("first-ingredient"))
				.ifPresentOrElse(builder::result, () -> {
					Formatter.error(String.format("Invalid first ingredient for trade: %s", name));
					valid.set(false);
				});
		if(configurationSection.isConfigurationSection("second-ingredient")) {
			loadItemStackFromConfig(configurationSection.getConfigurationSection("second-ingredient"))
					.ifPresentOrElse(builder::result, () -> {
						Formatter.error(String.format("Invalid second ingredient item for trade: %s", name));
						valid.set(false);
					});
		}

		if(valid.get()) {
			return Optional.of(builder.build());
		} else {
			return Optional.empty();
		}
	}

	private Optional<ItemStack> loadItemStackFromConfig(final @NotNull ConfigurationSection configurationSection) {
		Formatter.info("conf in fromConfig: " + configurationSection);
		Formatter.info("name: " + configurationSection.getName());
		if(configurationSection.contains("item")) {
			if(configurationSection.contains("provider")) {
				//TODO: MI integration
			} else {
				Formatter.info("Loading internal item: " + configurationSection.getString("item"));
				final var rpItem = DependencyManager.getInstance().getItem(configurationSection.getString("item"));
				Formatter.info("found?: " + rpItem.isPresent());

				return rpItem.map(item -> {
					final var newItem = item.clone();
					newItem.setAmount(configurationSection.getInt("amount", 1));
					return newItem;
				}).or(Optional::empty);
			}
		} else {
			return Optional.of(YamlItem.fromConfiguration(configurationSection).getItemFromFile().make().getItemStack());
		}

		return Optional.empty();
	}

	public static TradeLoader getInstance() {
		return INSTANCE;
	}

}
