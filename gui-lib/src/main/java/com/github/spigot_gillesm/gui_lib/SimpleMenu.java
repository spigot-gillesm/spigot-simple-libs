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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class SimpleMenu {

	private final List<SimpleButton> registeredButtons = new ArrayList<>();

	private boolean buttonsRegistered = false;

	protected final SimpleMenu parentMenu;

	private SimpleButton returnButton;

	@Setter(AccessLevel.PROTECTED)
	protected int size = 3*9;

	@Setter(AccessLevel.PROTECTED)
	private String title = "&8Menu";

	@Setter
	private boolean cancelReinstantiation = false;

	@Setter(AccessLevel.PROTECTED)
	@Getter(AccessLevel.PROTECTED)
	private boolean cancelActions = false;

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
			) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					parentMenu.setCancelReinstantiation(true);
					parentMenu.display(player);
					return false;
				}
			};
		}
	}

	public void display(@NotNull final Player player) {
		this.viewer = player;

		if(buttonsRegistered && !cancelReinstantiation) {
			final SimpleMenu menu = reInstantiate();

			if(menu != null) {
				menu.display(player);
			}
		} else {
			registerButtons();
			final var inventory = Bukkit.getServer().createInventory(player, size, Formatter.colorize(title));
			inventory.setContents(getContent());

			player.openInventory(inventory);
			player.setMetadata("SIMPLE_MENU", new FixedMetadataValue(GuiLib.plugin, this));

			if(cancelReinstantiation) {
				cancelReinstantiation = false;
			}
		}
	}

	@Nullable
	protected SimpleMenu reInstantiate() {
		try {
			if(parentMenu != null) {
				for(final Constructor<?> constructor : getClass().getDeclaredConstructors()) {
					constructor.setAccessible(true);

					if(constructor.getParameterCount() == 1) {
						return (SimpleMenu) constructor.newInstance(parentMenu);
					} else {
						throw new IllegalArgumentException("Classes extending SimpleMenu cannot have constructor having more than one argument.");
					}
				}
			}
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
			Formatter.error("Error re instantiating " + getClass() + ". Make sure the constructor has no argument or only one extending SimpleMenu.");
		}

		return null;
	}

	private void registerButton(@NotNull final Field field) {
		field.setAccessible(true);

		if(SimpleButton.class.isAssignableFrom(field.getType())) {
			try {
				final SimpleButton button = (SimpleButton) field.get(this);
				if(button != null) {
					registeredButtons.add(button);
				}
 			} catch (IllegalAccessException e) {
				Formatter.error("Error registering button.");
			}
		}
	}

	protected List<SimpleButton> registerLastButtons() {
		return new ArrayList<>();
	}

	protected void registerButtons() {
		registeredButtons.clear();
		Arrays.stream(getClass().getDeclaredFields()).forEach(this::registerButton);
		registeredButtons.addAll(registerLastButtons());
		buttonsRegistered = true;
	}

	protected abstract ItemStack getSlotItem(final int slot);

	public void onClose(final Player player) {
		//default to no effect. Must be overridden
	}

	@Nullable
	private ItemStack getLowerItem(final int slot) {
		if(returnButton != null && slot == size - 9) {
			return returnButton.getIcon();
		}

		return null;
	}

	@Nullable
	private ItemStack getItemAt(final int slot) {
		return getLowerItem(slot) != null ? getLowerItem(slot) : getSlotItem(slot);
	}

	protected ItemStack[] getContent() {
		final var items = new ItemStack[size];

		for(var i = 0; i < size; i++) {
			items[i] = getItemAt(i);
		}

		return items;
	}

	public Optional<SimpleButton> getButton(@NotNull final ItemStack item) {
		if(returnButton != null && returnButton.getIcon().equals(item)) {
			return Optional.of(returnButton);
		} else {
			return registeredButtons.stream().filter(b -> b.getIcon().isSimilar(item)).findFirst();
		}
	}

	public static SimpleMenu getMenu(@NotNull final Player player) {
		if(player.hasMetadata("SIMPLE_MENU")) {
			final var obj = player.getMetadata("SIMPLE_MENU").get(0).value();

			if(obj instanceof SimpleMenu) {
				return (SimpleMenu) obj;
			}
		}
		return null;
	}

}
