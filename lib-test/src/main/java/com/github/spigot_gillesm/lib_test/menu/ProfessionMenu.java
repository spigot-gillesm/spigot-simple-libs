package com.github.spigot_gillesm.lib_test.menu;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import com.github.spigot_gillesm.lib_test.PlayerManager;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ProfessionMenu extends SimpleMenu {

	private final SimpleButton bsButton;
	private final SimpleButton enchantButton;
	private final SimpleButton alchemistButton;
	private final SimpleButton journalistButton;
	private final SimpleButton doctorButton;
	private final SimpleButton barmanButton;
	private final SimpleButton farmerButton;
	private final SimpleButton policemanButton;
	private final SimpleButton priestButton;

	public ProfessionMenu() {
		this.bsButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.IRON_CHESTPLATE)
				.displayName("&e&lForgeron.ne")
				.lore("", "&fSpécialisé.e dans la fabrication", "&fd'armes et armures")
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &e&lForgeron.ne &a!");
					ProfessionManager.BLACKSMITH.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.enchantButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.ENCHANTED_BOOK)
				.displayName("&b&lProfesseur.e")
				.lore("", "&fSpécialisé.e dans les enchantements")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &b&lProfesseur.e &a!");
					ProfessionManager.ENCHANTER.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.alchemistButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.DRAGON_BREATH)
				.displayName("&5&lAlchimiste")
				.lore("", "&fSpécialisé.e dans la fabrication", "&fde potions")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &5&lAlchemiste &a!");
					ProfessionManager.ALCHEMIST.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.barmanButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.HONEY_BOTTLE)
				.displayName("&c&lBarman &f/ &c&lBarmaid")
				.lore("", "&fSpécialisé.e dans la fabrication", "&fde breuvages multiples", "&fet variés")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &c&lBarman / Barmaid &a!");
					ProfessionManager.BARMAN.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.journalistButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.PAPER)
				.displayName("&e&lJournaliste")
				.lore("", "&fSpécialisé.e dans la communication")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &e&lJournaliste &a!");
					ProfessionManager.JOURNALIST.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.doctorButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.ENCHANTED_GOLDEN_APPLE)
				.displayName("&b&lDocteur &f/ &b&lDoctoresse")
				.lore("", "&fSpécialisé.e dans les soins")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &b&lDocteur &a/ &b&lDoctoresse &a!");
					ProfessionManager.DOCTOR.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.farmerButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.IRON_HOE)
				.displayName("&a&lFermier.ère")
				.lore("", "&fSpécialisé.e dans l'agriculture")
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &a&lFermier.ère&a!");
					ProfessionManager.FARMER.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.policemanButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.IRON_HELMET)
				.displayName("&5&lGendarme")
				.lore("", "&fSpécialisé.e dans la protection")
				.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &5&lGendarme&a!");
					ProfessionManager.POLICEMAN.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		this.priestButton = new SimpleButton(SimpleItem.newBuilder()
				.material(Material.TOTEM_OF_UNDYING)
				.displayName("&d&lPrêtre.sse")
				.lore("", "&fAmen")
				.build()) {
			@Override
			public boolean action(final Player player, final ClickType click, final ItemStack draggedItem) {
				if(PlayerManager.hasProfession(player)) {
					Formatter.tell(player, "&cYou've already got a profession");
				} else {
					Formatter.tell(player, "&aVous êtes maintenant &d&lPrêtre.sse&a!");
					ProfessionManager.PRIEST.join(player);
					player.closeInventory();
				}
				return false;
			}
		};
		setSize(4*9);
		setTitle("&8Métiers");
	}

	@Override
	protected ItemStack getSlotItem(final int slot) {
		if(slot == 10) {
			return bsButton.getIcon();
		}
		else if(slot == 11) {
			return enchantButton.getIcon();
		}
		else if(slot == 12) {
			return alchemistButton.getIcon();
		}
		else if(slot == 13) {
			return barmanButton.getIcon();
		}
		else if(slot == 14) {
			return journalistButton.getIcon();
		}
		else if(slot == 15) {
			return doctorButton.getIcon();
		}
		else if (slot == 16) {
			return farmerButton.getIcon();
		}
		else if(slot == 19) {
			return policemanButton.getIcon();
		}
		else if(slot == 20) {
			return priestButton.getIcon();
		}
		else {
			//Fill the rest
			return SimpleItem.newBuilder()
					.material(Material.GRAY_STAINED_GLASS_PANE)
					.displayName("&f")
					.build()
					.getItemStack();
		}
	}

}
