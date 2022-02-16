package com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.ItemUtil;
import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.craft.CraftEntity;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.DynamicCraft;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.RecipeRunnable;
import com.github.spigot_gillesm.lib_test.event.CompleteCraftEntityEvent;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicAnvilMenu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnvilCraftRecipe extends CraftRecipe implements DynamicCraft {

	//The amount of time the item must be hardened (= put in water)
	private final int hardeningAmount;

	private final int requiredHammering;

	private final int mistakeCost;

	//In ticks
	private final int timeLaps;

	protected AnvilCraftRecipe(@NotNull final Builder builder) {
		super(builder);

		this.hardeningAmount = builder.hardeningAmount;
		this.requiredHammering = builder.requiredHammering;
		this.mistakeCost = builder.mistakeCost;
		this.timeLaps = builder.timeLaps;
	}

	@Override
	public RecipeRunnable<AnvilCraftRecipe, DynamicAnvilMenu> start(final Player player,
																	final DynamicCraftMenu anvilMenu) {
		var runnable = new AnvilCraftRunnable(this, player, (DynamicAnvilMenu) anvilMenu);
		runnable.runTaskTimer(LibTest.getInstance(), 30, 1);
		return runnable;
	}

	private void callEvent(final Player player, final CraftEntity craftEntity) {
		final var event = new CompleteCraftEntityEvent(player, craftEntity);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	//Called when using the item in its cold state on the anvil
	@Override
	public boolean reWork(final Player player, final ItemStack itemStack) {
		//If the item is already finished (= the result from the super class), do nothing
		//If the item isn't cold, do nothing
		if(itemStack.isSimilar(super.getResult())) {
			return false;
		}
		if(ItemUtil.hasLineInLore(itemStack, "&bCold")) {
			Formatter.info("Cold!");
			final var menu = new DynamicAnvilMenu();
			menu.setRecipeRunnable(start(player, menu));
			final var hardenItem = harden(player, itemStack, false);
			menu.setResult(hardenItem);
			menu.display(player);

			return true;
		}

		return false;
	}

	//Called when using the item with a filled cauldron to cool the item down
	public ItemStack coolDown(final Player player, final ItemStack itemStack) {
		if(ItemUtil.hasLineInLore(itemStack, "&cHot")) {
			return harden(player, itemStack, true);
		} else {
			return itemStack;
		}
	}

	//Called to modify the hardening data of the item
	public ItemStack harden(final Player player, final ItemStack itemStack, final boolean isHot) {
		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return null;
		}
		final var currentHardeningAmount = getCurrentHardeningAmount(itemStack);

		if(currentHardeningAmount > 0 && "CANCEL_USE".equals(meta.getLocalizedName())) {
			//Check if the item is about to be hardened enough and is hot -> cold
			if(currentHardeningAmount >= hardeningAmount && isHot) {
				CraftManager.getCraftItem(itemStack).ifPresent(item -> callEvent(player, item));
				return super.getResult();
			} else {
				//Clone the data
				final var newItem = itemStack.clone();
				final var newMeta = newItem.getItemMeta();
				final var newLore = ItemUtil.getLore(itemStack);
				//Update the hardening data
				final var hardenData = "&cHardening: " + (currentHardeningAmount + 1) + "/" + hardeningAmount;

				if(isHot) {
					//Only increase harden amount if the item hot -> cold (= isHot)
					newLore.set(1, Formatter.colorize(hardenData));
					newLore.set(2, Formatter.colorize("&bCold"));
				} else {
					newLore.set(2, Formatter.colorize("&cHot"));
				}
				newMeta.setLore(newLore);
				newItem.setItemMeta(newMeta);

				return newItem;
			}
		}

		return null;
	}

	@Override
	public ItemStack getResult() {
		//Get the original result
		final var item = super.getResult().clone();
		final var meta = item.getItemMeta();

		if(meta == null) {
			return null;
		}
		final List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		//Set the hardening data
		lore.add(0, Formatter.colorize("&cHot"));
		lore.add(0, Formatter.colorize("&cHardening: 1/" + hardeningAmount));
		lore.add(0, "");
		meta.setLore(lore);
		meta.setLocalizedName("CANCEL_USE");
		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(final ItemStack itemStack) {
		final ItemMeta meta = itemStack.getItemMeta();

		if(meta == null) {
			return false;
		}
		if(getCurrentHardeningAmount(itemStack) > 0) {
			final List<String> newLore = ItemUtil.getLore(itemStack);
			//Remove the 3 first lines for further comparison
			newLore.remove(0); //Remove the empty line
			newLore.remove(0); //Remove the hardening amount
			newLore.remove(0); //Remove the hot/cold data

			//Copy the item and the lore
			final var newItem = itemStack.clone();
			final var newMeta = meta.clone();

			//Set the lore of the copied item to the updated lore
			//Check if the new lore is empty or if just a blank line is left
			if(newLore.isEmpty()) {
				newMeta.setLore(null);
			} else {
				newMeta.setLore(newLore);
			}
			newMeta.setLocalizedName(null);
			newItem.setItemMeta(newMeta);

			//Return whether the item without the hardening data is similar or not
			return newItem.isSimilar(super.getResult());
		} else {
			//If there are no hardening data attach to the item -> compare the items
			return getItem().isSimilar(itemStack);
		}
	}

	private int getCurrentHardeningAmount(final ItemStack itemStack) {
		final var hardeningData = ItemUtil.getStringFromLore(itemStack, "Hardening");

		if(!hardeningData.contains("/")) {
			return 0;
		}
		final var splitData = hardeningData.split("/");

		try {
			return Integer.parseInt(splitData[0]);
		} catch (NumberFormatException exception) {
			return 0;
		}
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder extends CraftRecipe.Builder {

		private int hardeningAmount = 1;

		private int requiredHammering = 10;

		private int mistakeCost = 3;

		//In ticks
		private int timeLaps = 30;

		public Builder hardeningAmount(final int hardeningAmount) {
			this.hardeningAmount = hardeningAmount;
			return this;
		}

		public Builder requiredHammering(final int requiredHammering) {
			this.requiredHammering = requiredHammering;
			return this;
		}

		public Builder mistakeCost(final int mistakeCost) {
			this.mistakeCost = mistakeCost;
			return this;
		}

		public Builder timeLaps(final int timeLaps) {
			this.timeLaps = timeLaps;
			return this;
		}

		@Override
		public AnvilCraftRecipe build() {
			super.build();
			if(hardeningAmount < 1) {
				throw new IllegalArgumentException("The hardening amount cannot be smaller than 1");
			}
			if(dynamicCraftMenu == null) {
				throw new IllegalArgumentException("Dynamic craft menu cannot be null for an instance of DynamicCraft.");
			}
			return new AnvilCraftRecipe(this);
		}

	}

	public static class AnvilCraftRunnable extends RecipeRunnable<AnvilCraftRecipe, DynamicAnvilMenu> {

		@Getter
		private int nextHitTargetLocation;

		private AnvilCraftRunnable(final AnvilCraftRecipe recipe, final Player player, final DynamicAnvilMenu anvilMenu) {
			super(player, anvilMenu, recipe);

			this.nextHitTargetLocation = new Random().nextInt(27);
		}

		@Override
		protected boolean execute() {
			//If the score is high enough -> success
			if(score >= getRecipe().requiredHammering) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				//Let the menu knows the craft is over
				getMenu().setFinished(true);
				getMenu().setCancelReinstantiation(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			//If the score falls below 0 -> failure
			if(score < 0) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				//Let the menu knows the craft failed
				getMenu().setFailed(true);
				getMenu().setCancelReinstantiation(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			//If no click has been made during the given time laps -> considered as fail
			if(globalTimer >= getRecipe().timeLaps) {
				fail();
			}

			return false;
		}

		public void buildUp() {
			//Increase the score
			score++;
			//Reset the global timer
			globalTimer = 0;
			//Reset the position of the next hit target
			setNextHitTarget();
		}

		public void fail() {
			score -= getRecipe().mistakeCost;
			globalTimer = 0;
			setNextHitTarget();
		}

		public void setNextHitTarget() {
			//Generate a position within 3 rows for the hit button to appear
			this.nextHitTargetLocation = new Random().nextInt(27);
			getMenu().setCancelReinstantiation(true);
			getMenu().display(player);
		}

		/**
		 *
		 * @return the score on a scale of 0 to 9 for display purpose
		 */
		public int getProportionalScore() {
			return (int) ((score * 1.0 / getRecipe().requiredHammering) * 9.0);
		}

	}

}
