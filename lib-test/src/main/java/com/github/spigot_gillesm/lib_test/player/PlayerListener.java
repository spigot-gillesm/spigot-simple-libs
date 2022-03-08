package com.github.spigot_gillesm.lib_test.player;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.gui_lib.SimpleMenuInteractEvent;
import com.github.spigot_gillesm.lib_test.BrewManager;
import com.github.spigot_gillesm.lib_test.RpProfessions;
import com.github.spigot_gillesm.lib_test.craft.CraftManager;
import com.github.spigot_gillesm.lib_test.event.CompleteCraftEntityEvent;
import com.github.spigot_gillesm.lib_test.event.InteractWithPatternEvent;
import com.github.spigot_gillesm.lib_test.pattern.RecipePatternManager;
import com.github.spigot_gillesm.lib_test.profession.Privilege;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
			/*if(block.getType().name().contains("BED")) {
				event.setCancelled(true);
			}*/

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

	@EventHandler
	private void onPlayerBreakBlock(final BlockBreakEvent event) {
		final var block = event.getBlock().getType();

		//If breaking wheat is a privilege, check if the player has that privilege
		if(block == Material.WHEAT && ProfessionManager.privilegeExists(Privilege.WHEAT) &&
				!PlayerManager.hasPrivilege(event.getPlayer(), Privilege.WHEAT)) {
			//If not -> prevent drops
			event.setDropItems(false);
		}
	}

	@EventHandler
	private void onEntityDeath(final EntityDeathEvent event) {
		final var entity = event.getEntity();
		final var killer = entity.getKiller();

		//If killing animals is a privilege, check if the player has that privilege
		if(entity instanceof Animals && killer != null && ProfessionManager.privilegeExists(Privilege.ANIMALS) &&
				!PlayerManager.hasPrivilege(killer, Privilege.ANIMALS)) {
			//If not -> prevent drops
			event.getDrops().clear();
		}
		if(entity instanceof Monster && killer != null && ProfessionManager.privilegeExists(Privilege.MONSTERS) &&
				!PlayerManager.hasPrivilege(killer, Privilege.MONSTERS)) {
			//If not -> prevent drops
			event.getDrops().clear();
		}
	}

	@EventHandler
	private void onPrepareItemCraft(final PrepareItemCraftEvent event) {
		final var players = event.getViewers();
		final var recipe = event.getRecipe();

		if(recipe != null) {
			final var material = recipe.getResult().getType();
			final var isArmor = material.name().contains("HELMET") || material.name().contains("CHESTPLATE")
					|| material.name().contains("LEGGINGS") || material.name().contains("BOOTS");
			final var isWeapon = (material.name().contains("SWORD") || material.name().contains("AXE"))
					&& !material.name().contains("WOOD");
			var preventCraft = false;

			for (final var player : players) {
				if(isArmor && ProfessionManager.privilegeExists(Privilege.ARMORS) &&
						!PlayerManager.hasPrivilege((Player) player, Privilege.ARMORS)) {
					preventCraft = true;
				}
				if(isWeapon && ProfessionManager.privilegeExists(Privilege.WEAPONS) &&
						!PlayerManager.hasPrivilege((Player) player, Privilege.WEAPONS)) {
					preventCraft = true;
				}
			}
			if(preventCraft) {
				event.getInventory().setResult(null);
				players.forEach(p -> ((Player) p).updateInventory());
			}
		}
	}


	@EventHandler
	private void onPlayerInteract(final PlayerInteractEvent event) {
		final var player = event.getPlayer();

		if(event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if(event.getAction().name().contains("RIGHT")) {
			final var heldItem = player.getInventory().getItemInMainHand();

			if(heldItem == null || heldItem.getType() == Material.AIR) {
				return;
			}
			final var blacksmithItem = CraftManager.getAnvilCraftRecipe(heldItem);
			final var block = player.getTargetBlock(null, 3);

			if(block == null) {
				return;
			}

			if(blacksmithItem.isPresent() && block.getType() == Material.WATER_CAULDRON) {
				final var hardenedItem = blacksmithItem.get().coolDown(player, heldItem);

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

		if(item == null || item.getType() == Material.AIR) {
			return;
		}

		if(item.hasItemMeta() && item.getItemMeta().getLocalizedName().contains("CANCEL_USE")) {
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

	@EventHandler
	private void onCompleteCraftEntityEvent(final CompleteCraftEntityEvent event) {
		final var player = event.getPlayer();
		final var profession = PlayerManager.getProfession(player);

		if(PlayerManager.updateProfessionLevel(event.getPlayer(), event.getCraftEntity())) {
			final var level = PlayerManager.getProfessionLevel(player);

			Formatter.tell(player, "&aYou're now a level &9" + PlayerManager.getProfessionLevel(player)
					+ " " + profession.get().getDisplayName());

			for(final var craftEntity : CraftManager.getItemsWithRequiredLevel(profession.get(), level)) {
				if(craftEntity.getItem().hasItemMeta() && craftEntity.getItem().getItemMeta().hasDisplayName()) {
					Formatter.tell(player, "&aYou've learned to craft "
							+ craftEntity.getItem().getItemMeta().getDisplayName() + "&a!");
				} else {
					Formatter.tell(player, "&aYou've learned to craft a new item!");
				}
			}
		}
	}

	@EventHandler
	private void onPlayerClickItem(final PlayerInteractEvent event) {
		if(event.getAction().name().contains("RIGHT")) {
			RecipePatternManager.getRecipePattern(event.getItem())
					.ifPresent(pattern -> Bukkit.getServer().getPluginManager()
							.callEvent(new InteractWithPatternEvent(event.getPlayer(), pattern))
					);
		}
	}

	@EventHandler
	private void onInteractWithPatternEvent(final InteractWithPatternEvent event) {
		final var pattern = event.getRecipePattern();
		final var player = event.getPlayer();

		if(pattern.teach(player) && pattern.isConsumedOnUse()) {
			Bukkit.getServer().getScheduler().runTaskLater(RpProfessions.getInstance(),
					() -> player.getInventory().setItemInMainHand(new ItemStack(Material.AIR)),
					2L);
		}
	}

}
