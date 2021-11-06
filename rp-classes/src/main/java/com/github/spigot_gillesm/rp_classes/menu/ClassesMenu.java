package com.github.spigot_gillesm.rp_classes.menu;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.rp_classes.player.PlayerManager;
import com.github.spigot_gillesm.rp_classes.rp_class.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClassesMenu extends ListingMenu {

	@Override
	protected List<SimpleButton> generateButtons(final Player viewer) {
		final List<SimpleButton> buttons = new ArrayList<>();

		for(final var rpClass : ClassManager.getClasses()) {
			buttons.add(new SimpleButton(SimpleItem.newBuilder()
					.material(rpClass.getIcon())
					.displayName(rpClass.getDisplayName())
					.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
					.lore(rpClass.getDescription())
					.build()) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					PlayerManager.setPlayerClass(player, rpClass);
					Formatter.tell(player, "&aYou're now a " + rpClass.getDisplayName() + "&a!");

					return false;
				}
			});
		}

		return buttons;
	}

}
