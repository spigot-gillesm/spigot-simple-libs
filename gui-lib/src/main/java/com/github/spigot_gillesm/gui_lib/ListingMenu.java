package com.github.spigot_gillesm.gui_lib;

import com.github.spigot_gillesm.item_lib.SimpleItem;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ListingMenu extends SimpleMenu {

	//Represents the content of every pages of the menu
	private final List<List<SimpleButton>> menuContent;

	private int pageAmount = 1;
	@Setter(AccessLevel.PRIVATE)
	private int currentPage = 0;

	private SimpleButton nextPageButton;
	private SimpleButton previousPageButton;

	@Setter(AccessLevel.PROTECTED)
	private SimpleButton middleButton;

	protected ListingMenu(final SimpleMenu parentMenu) {
		super(parentMenu);
		setSize(5*9);
		generateNavButtons();
		this.menuContent = generateContent(generateButtons());
	}

	protected ListingMenu() {
		this(null);
	}

	protected abstract List<SimpleButton> generateButtons();

	/*@Override
	public void display(@NotNull final Player player) {
		this.viewer = player;
		final var inventory = Bukkit.getServer().createInventory(player, size, Formatter.colorize(title));
		inventory.setContents(getContent());

		player.openInventory(inventory);
		player.setMetadata("SIMPLE_MENU", new FixedMetadataValue(GuiLib.getInstance(), this));
	}*/

	/*@Override
	protected ItemStack[] getContent() {
		final ItemStack[] content = new ItemStack[size];

		for(var i = 0; i < size; i++) {
			content[i] = getSlotItem(i);
		}

        return content;
	}*/

	@Nullable
	@Override
	protected ItemStack getSlotItem(int slot) {
		//Check for the navigation buttons
		/*if(returnButton != null && slot == size - 9) {
			return returnButton.getIcon();
		}*/
		if(slot == size - 8 && currentPage > 0) {
			return previousPageButton.getIcon();
		}
		if(slot == size - 1 && currentPage < pageAmount - 1) {
			return nextPageButton.getIcon();
		}
		if(slot == size - 5 && middleButton != null) {
			return middleButton.getIcon();
		}
		//Check for an empty menu
		if(menuContent.isEmpty()) {
			return null;
		}
		final var currentContent = menuContent.get(currentPage);

		if(slot < currentContent.size()) {
			return currentContent.get(slot) != null ? currentContent.get(slot).getIcon() : null;
		}

		return null;
	}

	private List<List<SimpleButton>> generateContent(@NotNull final List<SimpleButton> buttons) {
		//i.e: 28 buttons will result in 2 pages. page 1 : 1 -> 27, page 2: 28
		this.pageAmount = (int) (Math.ceil((buttons.size() * 1.0) / (size - 9)));

		//Represents the pages
		final var content = new ArrayList<List<SimpleButton>>();
		//Represents the content of one page
		final var newPage = new ArrayList<SimpleButton>();

		//Fills the pages
		for(final SimpleButton button : buttons) {
			//If one page is full, put it in the content list
			if(newPage.size() >= size - 9) {
				//Copy the list into the content list
				content.add(new ArrayList<>(newPage));
				newPage.clear();
			}

			newPage.add(button);
            registerButton(button);
		}
		//Check for a non-full page to add
		if(!newPage.isEmpty()) {
			content.add(newPage);
		}

		return content;
	}

	private void generateNavButtons() {
		this.nextPageButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.ARROW)
				.displayName("&7Next Page")
				.build()
				.make()) {
			@Override
			public boolean action(Player player, ClickType click, ItemStack draggedItem) {
				SimpleMenu.getMenu(player).ifPresent(menu -> {
					if(!(menu instanceof ListingMenu)) {
						return;
					}
					final var listingMenu = (ListingMenu) menu;
					listingMenu.setCurrentPage(currentPage + 1);
					listingMenu.generateNavButtons();
					listingMenu.display(player);
				});

				return false;
			}
		};
		this.previousPageButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.ARROW)
				.displayName("&7Previous Page")
				.build()
				.make()) {
			@Override
			public boolean action(Player player, ClickType click, ItemStack draggedItem) {
				SimpleMenu.getMenu(player).ifPresent(menu -> {
					if(!(menu instanceof ListingMenu)) {
						return;
					}
					final var listingMenu = (ListingMenu) menu;
					listingMenu.setCurrentPage(currentPage - 1);
					listingMenu.generateNavButtons();
					listingMenu.display(player);
				});

				return false;
			}
		};
		registerButtons(previousPageButton, nextPageButton);
	}

}
