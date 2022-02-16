package com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu;

import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.craft_entity.craft_recipe.ForgeCraftRecipe;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicForgeMenu extends DynamicCraftMenu {

	private static final int RESULT_SLOT = 4+9;

	private final SimpleButton heatButton;

	private final SimpleButton coolButton;

	public DynamicForgeMenu() {
		super(RESULT_SLOT);
		this.heatButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.LAVA_BUCKET)
				.displayName("&cHeat Up")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				getRecipeRunnable().heatUp();
				setCancelReinstantiation(true);
				//Update the menu only if the crafting process isn't finished yet
				return !finished;
			}
		};
		this.coolButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.WATER_BUCKET)
				.displayName("&bCool Down")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				getRecipeRunnable().coolDown();
				setCancelReinstantiation(true);
				return !finished;
			}
		};
		setSize(5*9);
		setTitle("Smelting");
	}

	private ForgeCraftRecipe.ForgeCraftRunnable getRecipeRunnable() {
		return getRecipeRunnable(ForgeCraftRecipe.ForgeCraftRunnable.class);
	}

	@Override
	public List<SimpleButton> registerLastButtons() {
		return new ArrayList<>(Arrays.asList(heatButton, coolButton));
	}

	@Override
	protected ItemStack getSlotItem(int slot) {
		//Do not display the heat bar if the process failed
		if(!failed) {
			for (var i = 0; i < (getRecipeRunnable().getHeat()); i++) {
				if(slot == i + 4 * 9) {
					return SimpleItem.newBuilder()
							.material(Material.RED_STAINED_GLASS_PANE)
							.displayName("&f")
							.localizedName("CANCEL_MENU")
							.build()
							.getItemStack();
				}
			}
		}
		if(slot == RESULT_SLOT && finished) {
			//Set finished back to false avoiding item duplication
			finished = false;
			return result;
		}
		if(slot == RESULT_SLOT && failed) {
			return SimpleItem.newBuilder()
					.material(Material.RED_STAINED_GLASS_PANE)
					.displayName("&cFailure")
					.localizedName("CANCEL_MENU")
					.build()
					.getItemStack();
		}
		if(slot == 3*9 + 3) {
			return coolButton.getIcon();
		}
		else if(slot == 3*9 + 5) {
			return heatButton.getIcon();
		}
		else {
			return SimpleItem.newBuilder()
					.material(Material.GRAY_STAINED_GLASS_PANE)
					.displayName("&f")
					.localizedName("CANCEL_MENU")
					.build()
					.getItemStack();
		}
	}

	@Override
	public void onClose(final Player player) {
		final var inventory = player.getOpenInventory().getTopInventory().getContents();

		if(inventory[RESULT_SLOT] != null && inventory[RESULT_SLOT].hasItemMeta()
				&& !inventory[RESULT_SLOT].getItemMeta().getLocalizedName().equals("CANCEL_MENU")) {
			player.getWorld().dropItemNaturally(player.getLocation(), inventory[RESULT_SLOT]);
		}
	}

}
