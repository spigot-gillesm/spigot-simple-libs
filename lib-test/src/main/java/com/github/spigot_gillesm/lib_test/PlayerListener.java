package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleMenuInteractEvent;
import com.github.spigot_gillesm.lib_test.brew.BrewManager;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.menu.craft_station_menu.*;
import com.github.spigot_gillesm.lib_test.profession.ProfessionType;
import com.github.spigot_gillesm.player_lib.DataManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	@EventHandler
	private void onPlayerClick(final PlayerInteractEvent event) {
		final var player = event.getPlayer();
		final var block = event.getClickedBlock();
		final var action = event.getAction();
		final var profession = PlayerManager.getProfession(player);

		if(block == null) {
			return;
		}
		if(action == Action.RIGHT_CLICK_BLOCK) {
			if(block.getType().name().contains("BED")) {
				event.setCancelled(true);
			}

			/*
			 * Brewing stand restrictions
			 */
			if(block.getType() == Material.BREWING_STAND) {
				if(profession != ProfessionType.ALCHEMIST && profession != ProfessionType.BARMAN
						&& profession != ProfessionType.DOCTOR) {
					event.setCancelled(true);
				}
			}
			/*
			 * Custom inventory interactions
			 */
			if(player.isSneaking()) {
				if(block.getType().name().contains("ANVIL") && profession == ProfessionType.BLACKSMITH) {
					event.setCancelled(true);
					new AnvilMenu().display(player);
				}
				else if(block.getType() == Material.BLAST_FURNACE && profession == ProfessionType.BLACKSMITH) {
					event.setCancelled(true);

					var isFueled = false;

					for(var x = -1; x <= 1; x++) {
						for(var z = -1; z <= 1; z++) {
							if(block.getLocation().add(x, 0, z).getBlock().getType() == Material.LAVA) {
								isFueled = true;
							}
						}
					}

					if(isFueled) {
						new ForgeMenu().display(player);
					} else {
						Formatter.tell(player, "&cThe forge must be near a source of lava in order to work.");
					}
				}
				else if(block.getType() == Material.ENCHANTING_TABLE && profession == ProfessionType.ENCHANTER) {
					event.setCancelled(true);
					new EnchantMenu().display(player);
				}
				else if(block.getType() == Material.BREWING_STAND &&
						(profession == ProfessionType.ALCHEMIST || profession == ProfessionType.BARMAN
								|| profession == ProfessionType.DOCTOR)) {
					event.setCancelled(true);
					new PotionMenu().display(player);
				}
				else if(block.getType() == Material.LECTERN && profession == ProfessionType.PRIEST) {
					event.setCancelled(true);
					new LecternMenu().display(player);
				}
			}
		}
	}

	@EventHandler
	private void onPlayerBreakBlock(final BlockBreakEvent event) {
		/*
		 * Prevent actions based on the profession
		 */
		final var player = event.getPlayer();
		final var block = event.getBlock();
		final var profession = PlayerManager.getProfession(player);

		/*
		 * FARMER
		 */
		if(block.getType() == Material.WHEAT && profession != ProfessionType.FARMER) {
			event.setDropItems(false);
		}
	}

	@EventHandler
	private void onEntityDeath(final EntityDeathEvent event) {
		/*
		 * Prevent actions based on the profession
		 */
		final var entity = event.getEntity();
		final var killer = entity.getKiller();

		/*
		 * FARMER
		 */
		if(entity instanceof Animals && killer != null) {
			final var profession = PlayerManager.getProfession(killer);
			if(profession != ProfessionType.FARMER) {
				event.getDrops().clear();
			}
		}
	}

	@EventHandler
	private void onPlayerInteract(final PlayerInteractEvent event) {
		final var player = event.getPlayer();

		if(event.getAction().name().contains("RIGHT")) {
			final var heldItem = player.getInventory().getItemInMainHand();

			if(heldItem.isSimilar(CraftManager.EMPTY_EXPERIENCE_BOTTLE) && player.getLevel() >= 1) {
				if(player.getInventory().firstEmpty() == -1) {
					Formatter.tell(player, "&cYour inventory is full.");
				} else {
					//Decrement the item or remove it if amount == 1
					if(ItemManager.decrementItemAmount(heldItem)) {
						player.getInventory().remove(heldItem);
					}
					PlayerManager.decrementLevel(player);
					player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE));
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				}
			}
		}

	}

	@EventHandler
	private void onSimpleMenuInteract(final SimpleMenuInteractEvent event) {
		if(event.getClickedItem() == null) {
			return;
		}
		final var item = event.getClickedItem();

		if("CANCEL".equals(item.getItemMeta().getLocalizedName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onInventoryClick(final InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		if(event.getClickedInventory() == null) {
			return;
		}
		if(event.getClickedInventory().getType() != InventoryType.BREWING) {
			return;
		}
		final var click = event.getClick();
		final var player = (Player) event.getWhoClicked();
		final var inventory = (BrewerInventory) event.getClickedInventory();
		final var clickedItem = event.getCurrentItem();
		final var draggedItem = event.getCursor();
		final var clickedSlot = event.getSlot();

		//Check if it's a demo menu
		if(DataManager.getData(player).getRawValue("BREWING_MENU") != null) {
			event.setCancelled(true);
			return;
		}

		//Do nothing if the player has nothing on its cursor/isn't dragging any item
		if(draggedItem == null || draggedItem.getType() == Material.AIR) {
			return;
		}
		if(clickedItem != null) {
			//Left click means swapping items
			if(click == ClickType.LEFT) {
				//Dragged and Clicked are swapped -> params must be swapped as well
				BrewManager.setBrewerInventoryItems(player, inventory, clickedSlot, clickedItem.clone(), draggedItem.clone());
			}
			//Right click means adding one item instead of the whole stack
			else if(click == ClickType.RIGHT) {
				final var newDraggedItem = draggedItem.clone();
				newDraggedItem.setAmount(newDraggedItem.getAmount() - 1);

				//If nothing was already present on the slot, set the item to the dragged item with amount = 1
				if(clickedItem.getType() == Material.AIR) {
					final var newClickedItem = newDraggedItem.clone();
					newClickedItem.setAmount(1);

					BrewManager.setBrewerInventoryItems(player, inventory, clickedSlot, newDraggedItem, newClickedItem);
				} else if (clickedItem.isSimilar(draggedItem)){
					//Else, decrement dragged item and increment clicked item
					final var newClickedItem = clickedItem.clone();
					newClickedItem.setAmount(newClickedItem.getAmount() + 1);

					BrewManager.setBrewerInventoryItems(player, inventory, clickedSlot, newDraggedItem, newClickedItem);
				} else {
					//If the items are not similar, just swap them like a left click
					BrewManager.setBrewerInventoryItems(player, inventory, clickedSlot, clickedItem.clone(), draggedItem.clone());
				}
			}
		}
	}

	@EventHandler
	private void onInventoryClosed(final InventoryCloseEvent event) {
		//Remove the tag when a menu is closed
		DataManager.getData((Player) event.getPlayer()).removeRawValue("BREWING_MENU");
	}

}
