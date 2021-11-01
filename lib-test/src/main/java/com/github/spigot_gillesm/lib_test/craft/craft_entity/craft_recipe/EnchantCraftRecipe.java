package com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe;

import com.github.spigot_gillesm.lib_test.LibTest;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.CraftRecipe;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.DynamicCraft;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.RecipeRunnable;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu.DynamicEnchantMenu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnchantCraftRecipe extends CraftRecipe implements DynamicCraft {

	private final int requiredAmount;

	private final int mistakeCost;

	//In ticks
	private final int timeLaps;

	private EnchantCraftRecipe(@NotNull final Builder builder) {
		super(builder);

		this.requiredAmount = builder.requiredAmount;
		this.mistakeCost = builder.mistakeCost;
		this.timeLaps = builder.timeLaps;
	}

	@Override
	public RecipeRunnable<EnchantCraftRecipe, DynamicEnchantMenu> start(final Player player,
																		final DynamicCraftMenu dynamicCraftMenu) {
		final var runnable = new EnchantCraftRunnable(this, player, (DynamicEnchantMenu) dynamicCraftMenu);
		runnable.runTaskTimer(LibTest.getInstance(), 0, 1);
		return runnable;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@NoArgsConstructor
	public static class Builder extends CraftRecipe.Builder {

		private int requiredAmount = 1;

		private int mistakeCost = 0;

		//In ticks
		private int timeLaps = 20 * 8;

		public Builder requiredHammering(final int requiredAmount) {
			this.requiredAmount = requiredAmount;
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
		public EnchantCraftRecipe build() {
			super.build();
			if(requiredAmount < 1) {
				throw new IllegalArgumentException("The required amount cannot be smaller than 1");
			}
			if(mistakeCost < 0) {
				throw new IllegalArgumentException("The mistake cost cannot be smaller than 1");
			}
			if(dynamicCraftMenu == null) {
				throw new IllegalArgumentException("Dynamic craft menu cannot be null for an instance of DynamicCraft.");
			}
			return new EnchantCraftRecipe(this);
		}

	}

	public static class EnchantCraftRunnable extends RecipeRunnable<EnchantCraftRecipe, DynamicEnchantMenu> {

		@Getter
		private Material colorToFind;

		@Getter
		private final Map<Integer, Material> colors = new HashMap<>();

		private final List<Material> availableColors = new ArrayList<>(
				Arrays.asList(Material.MAGENTA_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
						Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE)
		);

		private EnchantCraftRunnable(final EnchantCraftRecipe recipe, final Player player, final DynamicEnchantMenu menu) {
			super(player, menu, recipe);
			this.colorToFind = pickColor();
			generateColors();
		}

		private Material pickColor() {
			final var rand = new Random().nextInt(availableColors.size());
			return availableColors.get(rand);
		}

		private void generateColors() {
			//Generate colors as long as there are empty spots
			while(colors.size() < 45) {
				//Generate random location for a random color
				final var location = new Random().nextInt(45);
				var color = pickColor();

				//one chance out of 2 to change the color if the selected color is the one to find
				//to avoid an overload of that color and make the game too hard
				if(color == colorToFind && new Random().nextInt(2) == 0) {
					color = pickColor();
				}
				colors.putIfAbsent(location, color);
			}
		}

		private void reset() {
			this.colorToFind = pickColor();
			generateColors();
			globalTimer = 0;

			//Re-load the buttons in the menu to match them with the new colors
			getMenu().registerLastButtons();
			getMenu().setCancelReinstantiation(true);
			getMenu().display(player);
		}

		public void clickedOn(final int slot) {
			//If the clicked slot is a color to find
			if(colors.get(slot) == colorToFind) {
				//remove the color from the list
				colors.remove(slot);

				//re display the menu without the selected color
				getMenu().getColorButtons().remove(slot);
				getMenu().setCancelReinstantiation(true);
				getMenu().display(player);
			} else {
				//If not, decrease the score
				score -= getRecipe().mistakeCost;
				//And reset the board
				reset();
			}
		}

		@Override
		protected boolean execute() {
			//Check if the selected colors have all been removed from the board
			if(!colors.containsValue(colorToFind)) {
				score++;

				if(score >= getRecipe().requiredAmount) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

					getMenu().setFinished(true);
					getMenu().setCancelReinstantiation(true);
					getMenu().display(player);
					cancel();

					return true;
				} else {
					reset();
				}
			}
			//If the score falls below 0 -> failure
			if(score < 0) {
				//Let the menu knows the craft failed
				getMenu().setFailed(true);
				getMenu().setCancelReinstantiation(true);
				//And re displays it
				getMenu().display(player);
				cancel();

				return true;
			}
			if(globalTimer >= getRecipe().timeLaps) {
				reset();
			}

			return false;
		}
	}

}
