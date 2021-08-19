package com.github.spigot_gillesm.gui_lib;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class SimpleButton {

	@Getter
	protected final ItemStack icon;

	@Setter
	@Getter
	private boolean cancelOpen = false;

	public SimpleButton(final ItemStack icon) {
		this.icon = icon;
	}

	public SimpleButton(final SimpleItem icon) {
		this(icon.getItemStack());
	}

	public abstract boolean action(Player player, ClickType click, ItemStack draggedItem);

}
