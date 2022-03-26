package com.github.spigot_gillesm.epicworlds_utils.villager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TradeManager {

	private static final TradeManager INSTANCE = new TradeManager();

	private final Set<VillagerTrade> registeredTrades = new HashSet<>();

	private TradeManager() { }

	public void registerTrades(final List<VillagerTrade> villagerTrades) {
		villagerTrades.forEach(this::registerTrade);
	}

	public void registerTrade(final VillagerTrade villagerTrade) {
		registeredTrades.add(villagerTrade);
	}

	public void clearTrades() {
		registeredTrades.clear();
	}

	public int getCount() {
		return registeredTrades.size();
	}

	public static TradeManager getInstance() {
		return INSTANCE;
	}

}
