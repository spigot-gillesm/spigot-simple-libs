package com.github.spigot_gillesm.lib_test.menu.dynamic_craft_menu;

import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.dynamic_craft.AnvilCraft;
import com.github.spigot_gillesm.lib_test.menu.DynamicCraftMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicAnvilMenu extends DynamicCraftMenu {

	private static final int RESULT_SLOT = 4+9;

	private final SimpleButton hitSpotButton;

	private final SimpleButton wrongSpotButton;

	public DynamicAnvilMenu() {
		this.hitSpotButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.BLACK_STAINED_GLASS_PANE)
				.displayName("&f")
				.localizedName("CANCEL")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				((AnvilCraft.AnvilCraftRunnable) craftRunnable).buildUp();
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
				return !finished;
			}
		};
		this.wrongSpotButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
				.displayName("&f")
				.localizedName("CANCEL")
				.build()) {
			@Override
			public boolean action(Player player, ClickType click, ItemStack draggedItem) {
				((AnvilCraft.AnvilCraftRunnable) craftRunnable).fail();
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
				return !finished;
			}
		};
		setSize(6*9);
		setTitle("Anvil");
	}

	@Override
	public List<SimpleButton> registerLastButtons() {
		return new ArrayList<>(Arrays.asList(hitSpotButton, wrongSpotButton));
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot >= 2*9 && slot < size - 9 && !(finished || failed)) {
			if(slot == ((AnvilCraft.AnvilCraftRunnable) craftRunnable).getNextHitTargetLocation() + 2*9) {
				return hitSpotButton.getIcon();
			} else {
				return wrongSpotButton.getIcon();
			}
		}
		else if(slot == RESULT_SLOT && finished) {
			return result;
		}
		else if(slot == RESULT_SLOT && failed) {
			return SimpleItem.newBuilder()
					.material(Material.RED_STAINED_GLASS_PANE)
					.displayName("&cFailure")
					.localizedName("CANCEL")
					.build()
					.getItemStack();
		}
		//If the craft is not failed nor finished -> display the progress bar
		else if(!(finished || failed)) {
			for (var i = 0; i < ((AnvilCraft.AnvilCraftRunnable) craftRunnable).getProportionalScore(); i++) {
				if(slot == i + 5 * 9) {
					return SimpleItem.newBuilder()
							.material(Material.LIME_STAINED_GLASS_PANE)
							.displayName("&cFailure")
							.localizedName("CANCEL")
							.build()
							.getItemStack();
				}
			}
		}
		//Fill the rest
		return SimpleItem.newBuilder()
				.material(Material.GRAY_STAINED_GLASS_PANE)
				.displayName("&f")
				.localizedName("CANCEL")
				.build()
				.getItemStack();
	}

}
