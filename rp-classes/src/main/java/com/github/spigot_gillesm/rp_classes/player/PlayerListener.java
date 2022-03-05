package com.github.spigot_gillesm.rp_classes.player;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.rp_classes.PluginUtil;
import com.github.spigot_gillesm.rp_classes.RpClasses;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	@EventHandler
	private void onPlayerUseItem(final PlayerInteractEvent event) {
		final var player = event.getPlayer();
		final var item = event.getPlayer().getInventory().getItemInMainHand();

		if(!PlayerManager.canEquip(player, item)) {
			//Since cancelling the event doesn't work when right-clicking air -> do it manually
			if(event.getAction() == Action.RIGHT_CLICK_AIR && PluginUtil.isArmor(item)) {
				Bukkit.getServer().getScheduler().runTaskLater(RpClasses.getInstance(),
						() -> {
							player.getWorld().dropItemNaturally(player.getLocation(), item);
							player.getInventory().setItem(PluginUtil.getMatchingArmorSlot(item), new ItemStack(Material.AIR));
						},
						2L);
			}
			event.setCancelled(true);
			Formatter.tell(player, "&cYou cannot use that item!");
		}
	}

	@EventHandler
	private void onEntityDamageEvent(final EntityDamageByEntityEvent event) {
		final var damager = event.getDamager();

		if(damager instanceof Player) {
			final var player = (Player) damager;
			final var item = player.getInventory().getItemInMainHand();

			if(!PlayerManager.canEquip(player, item)) {
				event.setCancelled(true);
				Formatter.tell(player, "&cYou cannot use that item");
			}
		}
	}

	@EventHandler
	private void onArmorInventoryClick(final InventoryClickEvent event) {
		final var player = (Player) event.getWhoClicked();
		final var item = event.getCursor();
		final var slot = event.getSlot();

		if(item != null && slot >= 36 && slot <= 40 && !PlayerManager.canEquip(player, item)) {
			event.setCancelled(true);
			Formatter.tell(player, "&cYou cannot equip that item!");
		}

	}

}
