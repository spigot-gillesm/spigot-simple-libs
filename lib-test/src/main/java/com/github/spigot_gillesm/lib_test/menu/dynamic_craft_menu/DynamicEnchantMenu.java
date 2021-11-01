package com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu;

import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.EnchantCraftRecipe;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicEnchantMenu extends DynamicCraftMenu {

	private static final int SELECTED_COLOR_SLOT = 49;

	private static final int RESULT_SLOT = 4+9;

	@Getter
	private final Map<Integer, SimpleButton> colorButtons = new HashMap<>();

	public DynamicEnchantMenu() {
		setSize(6*9);
		setTitle("Enchanting Table");
	}

	private EnchantCraftRecipe.EnchantCraftRunnable getRecipeRunnable() {
		return getRecipeRunnable(EnchantCraftRecipe.EnchantCraftRunnable.class);
	}

	@Override
	public List<SimpleButton> registerLastButtons() {
		final List<SimpleButton> buttons = new ArrayList<>();

		//Use colors and put in map
		for(final var set : getRecipeRunnable().getColors().entrySet()) {
			final var slot = set.getKey();
			final var color = set.getValue();

			final var button = new SimpleButton(SimpleItem.newBuilder()
					.material(color)
					.displayName("&f")
					.localizedName(slot + " CANCEL_MENU")
					.build()) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					getRecipeRunnable().clickedOn(slot);
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					return !finished;
				}
			};
			buttons.add(button);
			colorButtons.put(slot, button);
		}

		return buttons;
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		//If finished -> display the result
		if(slot == RESULT_SLOT && finished) {
			return result;
		}
		//If failed -> display the fail icon
		else if(slot == RESULT_SLOT && failed) {
			return SimpleItem.newBuilder()
					.material(Material.RED_STAINED_GLASS_PANE)
					.displayName("&cFailure")
					.localizedName("CANCEL_MENU")
					.build()
					.getItemStack();
		}
		//Display colors if not finished nor failed
		else if(slot == SELECTED_COLOR_SLOT) {
			return SimpleItem.newBuilder()
					.material(getRecipeRunnable().getColorToFind())
					.displayName("&f")
					.localizedName("CANCEL_MENU")
					.build()
					.getItemStack();
		}
		else if (!(finished || failed)){
			for(final var set : colorButtons.entrySet()) {
				if(slot == set.getKey()) {
					return set.getValue().getIcon();
				}
			}
		}
		//Fill the rest
		return SimpleItem.newBuilder()
				.material(Material.GRAY_STAINED_GLASS_PANE)
				.displayName("&f")
				.localizedName("CANCEL_MENU")
				.build()
				.getItemStack();
	}

}
