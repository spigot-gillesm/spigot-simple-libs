package com.github.spigot_gillesm.gui_lib;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Setter
@Getter
public abstract class SimpleButton {

	public static final String KEY_ID = "gui_lib_button_id";

	protected ItemStack icon;

	private boolean cancelOpen = false;

	protected SimpleButton() { }

	protected SimpleButton(@NotNull ItemStack icon) {
		this.icon = icon;
	}

	protected SimpleButton(@NotNull SimpleItem icon) {
		this(icon.getItemStack());
	}

	public abstract boolean action(Player player, ClickType click, ItemStack draggedItem);

	@Override
	public final int hashCode() {
		return Objects.hash(icon, cancelOpen);
	}

	@Override
	public final boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(other == this) {
			return true;
		}
		if(!(other instanceof SimpleButton otherButton)) {
			return false;
		}

        return Objects.equals(otherButton.icon, this.icon) && otherButton.cancelOpen == this.cancelOpen;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("icon", icon)
				.add("cancelOpen", cancelOpen)
				.toString();
	}

	public static class DummyButton extends SimpleButton {

		public static final DummyButton FILLING_BUTTON = new DummyButton(SimpleItem.newBuilder()
				.material(Material.GRAY_STAINED_GLASS_PANE)
				.displayName("&f")
				.build()
				.make()
				.getItemStack());

		public DummyButton(@NotNull ItemStack icon) {
			super(icon);
		}

		@Override
		public boolean action(Player player, final ClickType clickType, ItemStack draggedItem) {
			return false;
		}

	}

}
