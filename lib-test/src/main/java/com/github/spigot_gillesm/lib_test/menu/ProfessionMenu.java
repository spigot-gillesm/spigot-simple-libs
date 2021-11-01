package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.ListingMenu;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.PluginUtil;
import com.github.spigot_gillesm.lib_test.profession.Profession;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.lib_test.profession.WorkstationManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ProfessionMenu extends ListingMenu {

	@Override
	protected List<SimpleButton> generateButtons(Player viewer) {
		final List<SimpleButton> buttons = new ArrayList<>();

		for(final var profession : ProfessionManager.getProfessions()) {
			buttons.add(new SimpleButton(SimpleItem.newBuilder()
					.material(profession.getIcon())
					.displayName(profession.getDisplayName())
					.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
					.lore(generateLore(profession))
					.build()) {
				@Override
				public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
					if(PlayerManager.hasProfession(player)) {
						Formatter.tell(player, "&cYou already have a profession!");
					} else {
						PlayerManager.setPlayerProfession(player, profession);
						Formatter.tell(player, "&aYou're now " + profession.getDisplayName());
					}

					return false;
				}
			});
		}
		buttons.add(new SimpleButton(SimpleItem.newBuilder()
				.material(Material.WRITABLE_BOOK)
				.displayName("&4&lOublier")
				.addLore("", "&cShift &7+ &cClique Droit &7Pour", "&7oublier son métier")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player) && click == ClickType.SHIFT_RIGHT) {
					Formatter.tell(player, "&aYou're no longer " + PlayerManager.getProfession(player).get().getDisplayName());
					PlayerManager.removePlayerProfession(player);
				}

				return false;
			}
		});

		return buttons;
	}

	private List<String> generateLore(final Profession profession) {
		final List<String> description = new ArrayList<>(profession.getDescription());
		description.add("");
		description.add("&7Stations:");

		for(final var station : profession.getWorkstations()) {
			description.add(" &e" + PluginUtil.reformat(WorkstationManager.getId(station)));
		}

		return description;
	}

}
