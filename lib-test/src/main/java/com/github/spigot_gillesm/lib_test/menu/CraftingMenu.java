package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.craft.CraftItem;
import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CraftingMenu extends SimpleMenu {

	@Setter(AccessLevel.PACKAGE)
	private CraftItem item;

	public CraftingMenu(final SimpleMenu parentMenu) {
		super(parentMenu);
		setSize(6*9);
		setTitle("&8Recipe");
		setCancelActions(true);
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 12) {
			return item.getPattern()[0];
		}
		else if(slot == 13) {
			return item.getPattern()[1];
		}
		else if(slot == 14) {
			return item.getPattern()[2];
		}
		else if(slot == 21) {
			return item.getPattern()[3];
		}
		else if(slot == 22) {
			return item.getPattern()[4];
		}
		else if(slot == 23) {
			return item.getPattern()[5];
		}
		else if(slot == 25) {
			return item.getResult();
		}
		else if(slot == 30) {
			return item.getPattern()[6];
		}
		else if(slot == 31) {
			return item.getPattern()[7];
		}
		else if(slot == 32) {
			return item.getPattern()[8];
		}
		else if(slot == 49) {
			return item.getReagent();
		}
		else {
			//Fill the rest
			return SimpleItem.newBuilder()
					.material(Material.GRAY_STAINED_GLASS_PANE)
					.displayName("&f")
					.localizedName("CANCEL")
					.build()
					.getItemStack();
		}
	}

}