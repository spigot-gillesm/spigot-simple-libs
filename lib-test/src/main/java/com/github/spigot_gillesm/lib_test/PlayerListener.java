package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.gui_lib.SimpleMenuInteractEvent;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerListener implements Listener {

	@EventHandler
	private void onPlayerClick(final PlayerInteractEvent event) {
		final var player = event.getPlayer();
		final var block = event.getClickedBlock();
		final var profession = PlayerManager.getProfession(player);

		//Do not proceed if a menu is opened
		if(SimpleMenu.getMenu(player) != null) {
			event.setCancelled(true);
			return;
		}
		if(block == null || event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			//Prevent the use of beds
			if(block.getType().name().contains("BED")) {
				event.setCancelled(true);
			}

			profession.ifPresent(p -> {
				//Check if the player is not supposed to use this block
				if(!ProfessionManager.canUseWorkstation(p, block.getType())) {
					event.setCancelled(true);
				}
				//Open any custom inventory if needed
				if(player.isSneaking()
						&& p.displayWorkstationMenu(player, block, player.getInventory().getItemInMainHand())) {
					event.setCancelled(true);
				}
			});
		}
	}

	/*@EventHandler
	private void onPlayerBreakBlock(final BlockBreakEvent event) {*/
		/*
		 * Prevent actions based on the profession
		 */
		/*final var player = event.getPlayer();
		final var block = event.getBlock();
		final var professionType = PlayerManager.getProfessionType(player);*/

		/*
		 * FARMER
		 */
		/*if(block.getType() == Material.WHEAT && professionType != ProfessionType.FARMER) {
			event.setDropItems(false);
		}*/
	//}

	/*@EventHandler
	private void onEntityDeath(final EntityDeathEvent event) {*/
		/*
		 * Prevent actions based on the profession
		 */
		/*final var entity = event.getEntity();
		final var killer = entity.getKiller();*/

		/*
		 * FARMER
		 */
		/*if(entity instanceof Animals && killer != null) {
			final var professionType = PlayerManager.getProfessionType(killer);
			if(professionType != ProfessionType.FARMER) {
				event.getDrops().clear();
			}
		}*/
	//}

	@EventHandler
	private void onPlayerInteract(final PlayerInteractEvent event) {
		final var player = event.getPlayer();

		if(event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if(event.getAction().name().contains("RIGHT")) {
			final var heldItem = player.getInventory().getItemInMainHand();
			final var blacksmithItem = CraftManager.getAnvilCraftRecipe(heldItem);

			if(heldItem == null || heldItem.getType() == Material.AIR) {
				return;
			}

			/*if(heldItem.isSimilar(CraftManager.EMPTY_EXPERIENCE_BOTTLE) && player.getLevel() >= 1) {
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
			}*/
			final var block = player.getTargetBlock(null, 3);

			if(blacksmithItem.isPresent() && block.getType() == Material.WATER_CAULDRON) {
				final var hardenedItem = blacksmithItem.get().coolDown(heldItem);

				if(hardenedItem != null) {
					player.getInventory().removeItem(heldItem);
					player.getInventory().setItemInMainHand(hardenedItem);
					player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				}
				block.setType(Material.CAULDRON);
			}
		}

	}

	@EventHandler
	private void onPlayerUseItem(final PlayerInteractEvent event) {
		final var player = event.getPlayer();
		final var item = event.getPlayer().getInventory().getItemInMainHand();

		if(item.hasItemMeta() && "CANCEL_USE".contains(item.getItemMeta().getLocalizedName())) {
			event.setCancelled(true);
		}
		if(!PlayerManager.canEquip(player, item)) {
			event.setCancelled(true);
			Formatter.tell(player, "&cYou cannot use that item");
		}
	}

	@EventHandler
	private void onEntityDamageEvent(final EntityDamageByEntityEvent event) {
		final var damager = event.getDamager();

		if(damager instanceof Player) {
			final var player = (Player) damager;
			final var item = player.getInventory().getItemInMainHand();

			if(item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
					&& item.getItemMeta().getLocalizedName().contains("CANCEL_USE")) {
				event.setCancelled(true);
			}
			if(!PlayerManager.canEquip(player, item)) {
				event.setCancelled(true);
				Formatter.tell(player, "&cYou cannot use that item");
			}
		}
	}

	@EventHandler
	private void onSimpleMenuInteract(final SimpleMenuInteractEvent event) {
		if(event.getClickedItem() == null) {
			return;
		}
		final var item = event.getClickedItem();

		if(item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
				&& item.getItemMeta().getLocalizedName().contains("CANCEL_MENU")) {
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

	@EventHandler(priority = EventPriority.HIGH)
	private void onArmorInventoryClick(final InventoryClickEvent event) {
		final var player = (Player) event.getWhoClicked();
		final var item = event.getCursor();
		final var slot = event.getSlot();

		if(item != null && slot >= 36 && slot <= 40 && !PlayerManager.canEquip(player, item)) {
			event.setCancelled(true);
			Formatter.tell(player, "&cYou cannot equip that item");
		}

	}

	@EventHandler
	private void onInventoryClosed(final InventoryCloseEvent event) {
		//Remove the tag when a menu is closed
		DataManager.getData((Player) event.getPlayer()).removeRawValue("BREWING_MENU");
	}

}
