package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CraftStationMenu extends SimpleMenu {

	private static final Set<Integer> CRAFTING_SLOTS = Stream.of(3+9, 4+9, 5+9, 12+9, 13+9, 14+9, 21+9, 22+9, 23+9)
			.collect(Collectors.toCollection(HashSet::new));

	private static final int RESULT_SLOT = 25;

	private static final int REAGENTS_SLOT = 49;

	private final SimpleButton craftButton;

	protected CraftStationMenu() {
		this.craftButton = new SimpleButton(getCraftIcon()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				final var inventory = player.getOpenInventory().getTopInventory();
				final var content = inventory.getContents();

				final var pattern = new ItemStack[] {content[3+9], content[4+9], content[5+9], content[12+9],
						content[13+9], content[14+9], content[21+9], content[22+9], content[23+9]};
				final var craftItem = CraftManager.getItemFromPattern(pattern);

				//Check if patterns match any item, check if player can craft it, check if the result slot is empty
				if(craftItem.isPresent() && craftItem.get().canCraft(player) && content[RESULT_SLOT] == null) {
					final var reagents = content[REAGENTS_SLOT];

					//Check if the craft needs reagents
					if(craftItem.get().getReagent() != null) {

						if(craftItem.get().getReagent().equals(reagents)) {
							inventory.setItem(RESULT_SLOT, craftItem.get().getResult());
							//Clear the crafting grid + bottles slot
							inventory.setItem(REAGENTS_SLOT, null);
							CRAFTING_SLOTS.forEach(s -> inventory.setItem(s, null));
							player.getWorld().playSound(player.getLocation(), getCraftSound(), 1, 1);

						} else {
							Formatter.tell(player, "&cYou need &9" + craftItem.get().getReagent().getAmount() +
									" " + craftItem.get().getReagent().getItemMeta().getDisplayName() + " &cto create this item!");
						}

					} else {
						inventory.setItem(RESULT_SLOT, craftItem.get().getResult());
						//Clear the crafting grid
						CRAFTING_SLOTS.forEach(s -> inventory.setItem(s, null));
						player.getWorld().playSound(player.getLocation(), getCraftSound(), 1, 1);
					}
				}

				return false;
			}
		};
		setTitle("&8" + getTitle());
		setSize(6*9);
	}

	protected abstract SimpleItem getCraftIcon();

	protected abstract String getTitle();

	protected Sound getCraftSound() {
		return Sound.ENTITY_PLAYER_LEVELUP;
	}

	@Override
	protected List<SimpleButton> registerLastButtons() {
		return new ArrayList<>(Collections.singletonList(craftButton));
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 19) {
			return craftButton.getIcon();
		} else if(slot == RESULT_SLOT || CRAFTING_SLOTS.contains(slot) || slot == REAGENTS_SLOT) {
			return null;
		} else {
			//Fill the rest
			return SimpleItem.newBuilder()
					.material(Material.GRAY_STAINED_GLASS_PANE)
					.displayName("&f")
					.localizedName("CANCEL")
					.build()
					.getItemStack();
		}
	}

	@Override
	public void onClose(final Player player) {
		final var inventory = player.getOpenInventory().getTopInventory().getContents();

		//Drop the remaining items from the menu when closed
		CRAFTING_SLOTS.stream()
				.filter(s -> inventory[s] != null)
				.forEach(s -> player.getWorld().dropItemNaturally(player.getLocation(), inventory[s]));
		if(inventory[RESULT_SLOT] != null) {
			player.getWorld().dropItemNaturally(player.getLocation(), inventory[RESULT_SLOT]);
		}
		if(inventory[REAGENTS_SLOT] != null) {
			player.getWorld().dropItemNaturally(player.getLocation(), inventory[REAGENTS_SLOT]);
		}
	}

}
