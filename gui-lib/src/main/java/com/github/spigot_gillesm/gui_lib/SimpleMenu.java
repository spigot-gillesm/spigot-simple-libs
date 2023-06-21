package com.github.spigot_gillesm.gui_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SimpleMenu {

	protected final List<SimpleButton> registeredButtons = new ArrayList<>();

	protected final SimpleMenu parentMenu;

	protected SimpleButton returnButton;

	@Setter(AccessLevel.PROTECTED)
	protected int size = 3*9;

	@Setter(AccessLevel.PROTECTED)
	protected String title = "&8Menu";

	@Setter(AccessLevel.PROTECTED)
	@Getter(AccessLevel.PUBLIC)
	protected boolean cancelActions = false;

	@Getter(AccessLevel.PROTECTED)
	protected Player viewer;

	protected SimpleMenu(final SimpleMenu parentMenu) {
		this.parentMenu = parentMenu;
		generateParentButton();
	}

	protected SimpleMenu() {
		this(null);
	}

	private void generateParentButton() {
		if(parentMenu != null) {
			this.returnButton = new SimpleButton(SimpleItem.newBuilder()
					.material(Material.BOOK)
					.displayName("&9Return")
					.build()
					.make()
			) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					parentMenu.display(player);
					return false;
				}
			};
			registerButton(returnButton);
		}
	}

	public void display(@NotNull final Player player) {
		this.viewer = player;
		final var inventory = Bukkit.getServer().createInventory(player, size, Formatter.colorize(title));
		inventory.setContents(getContent());

		player.openInventory(inventory);
		player.setMetadata("SIMPLE_MENU", new FixedMetadataValue(GuiLib.getInstance(), this));
	}

	protected void registerButtons(final SimpleButton... simpleButtons) {
		List.of(simpleButtons).forEach(this::registerButton);
	}

	protected void registerButton(final SimpleButton simpleButton) {
		registeredButtons.add(simpleButton);
	}

	protected abstract ItemStack getSlotItem(final int slot);

	public void onClose(final Player player) {
		//default to no effect. Must be overridden
	}

	private Optional<ItemStack> getLowerItem(final int slot) {
		if(returnButton != null && slot == size - 9) {
			return Optional.of(returnButton.getIcon());
		}

		return Optional.empty();
	}

	@Nullable
	private ItemStack getItemAt(final int slot) {
		return getLowerItem(slot).orElse(getSlotItem(slot));
	}

	protected ItemStack[] getContent() {
		final var items = new ItemStack[size];

		for(var i = 0; i < size; i++) {
			items[i] = getItemAt(i);
		}

		return items;
	}

	public Optional<SimpleButton> getButton(@NotNull final ItemStack item) {
		return registeredButtons.stream().filter(button -> button.getIcon().isSimilar(item)).findFirst();
	}

	public static Optional<SimpleMenu> getMenu(@NotNull final Player player) {
		if(player.hasMetadata("SIMPLE_MENU")) {
			final var obj = player.getMetadata("SIMPLE_MENU").get(0).value();

			if(obj instanceof SimpleMenu) {
				return Optional.of((SimpleMenu) obj);
			}
		}
		return Optional.empty();
	}

}
