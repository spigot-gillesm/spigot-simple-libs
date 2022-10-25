package com.github.spigot_gillesm.gui_lib;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleButton {

	@Setter
	@Getter
	protected ItemStack icon;

	@Setter
	@Getter
	private boolean cancelOpen = false;

	public SimpleButton() { }

	public SimpleButton(@NotNull final ItemStack icon) {
		this.icon = icon;
	}

	public SimpleButton(@NotNull final SimpleItem icon) {
		this(icon.getItemStack());
	}

	public abstract boolean action(Player player, ClickType click, ItemStack draggedItem);

}
