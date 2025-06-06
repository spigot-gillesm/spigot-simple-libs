package com.github.spigot_gillesm.gui_lib;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.item_lib.ItemUtil;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SimpleMenu {

	public static final String METADATA_KEY = "SIMPLE_MENU";

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

	protected SimpleMenu(SimpleMenu parentMenu) {
		this.parentMenu = parentMenu;
		generateParentButton();
	}

	/**
	 * Default empty constructor.
	 */
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
				public boolean action(Player player, final ClickType click, ItemStack draggedItem) {
					parentMenu.display(player);
					return false;
				}

			};
			registerButton(returnButton);
		}
	}

	public void display(@NotNull Player player) {
		this.viewer = player;
		final var inventory = Bukkit.getServer().createInventory(player, size, Formatter.colorize(title));
		inventory.setContents(getContent());

		player.openInventory(inventory);
		player.setMetadata(METADATA_KEY, new FixedMetadataValue(GuiLib.getInstance(), this));
	}

	protected void registerButtons(@NotNull SimpleButton... simpleButtons) {
		List.of(simpleButtons).forEach(this::registerButton);
	}

	protected void registerButton(@NotNull SimpleButton simpleButton) {
		//Do not duplicate buttons
		if(!registeredButtons.contains(simpleButton)) {
			//Before registering the button, write a unique id in the button icon's data
			ItemUtil.setPersistentString(simpleButton.getIcon(), SimpleButton.KEY_ID, String.valueOf(registeredButtons.size()));
			registeredButtons.add(simpleButton);
		}
	}

	protected abstract ItemStack getSlotItem(int slot);

	/**
	 * onClose is run whenever this menu instance is closed by a player. Defaults to no action.
	 *
	 * @param player the player who closed the inventory
	 */
	public void onClose(Player player) {
		//default to no effect. Must be overridden
	}

	private Optional<ItemStack> getLowerItem(final int slot) {
		if(returnButton != null && slot == size - 9) {
			return Optional.of(returnButton.getIcon());
		}

		return Optional.empty();
	}

	private ItemStack getItemAt(final int slot) {
		return getLowerItem(slot).orElse(getSlotItem(slot));
	}

	protected ItemStack[] getContent() {
		final ItemStack[] items = new ItemStack[size];

		for(var i = 0; i < size; i++) {
			items[i] = getItemAt(i);
		}

		return items;
	}

	public Optional<SimpleButton> getButton(@NotNull ItemStack item) {
		//Try and retrieve an id from the item
		return ItemUtil.getPersistentString(item, SimpleButton.KEY_ID)
				//If one is found, check that it matches one of the registered buttons id
				.flatMap(
						s -> registeredButtons.stream()
								//Retrieve the registered buttons icon's id and compare it to the given item's id
								.filter(button -> ItemUtil.getPersistentString(button.getIcon(), SimpleButton.KEY_ID)
										.map(buttonId -> buttonId.equals(s))
										.orElse(false))
								.findFirst()
				);
    }

	public static Optional<SimpleMenu> getMenu(@NotNull Player player) {
		if(player.hasMetadata(METADATA_KEY)) {
			final Object obj = player.getMetadata(METADATA_KEY).get(0).value();

			if(obj instanceof SimpleMenu simpleMenu) {
				return Optional.of(simpleMenu);
			}
		}

		return Optional.empty();
	}

}
