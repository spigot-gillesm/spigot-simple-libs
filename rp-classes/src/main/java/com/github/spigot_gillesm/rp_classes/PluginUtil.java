package com.github.spigot_gillesm.rp_classes;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PluginUtil {

	public Player getPlayer(@NotNull final String name) {
		return Bukkit.getServer().getPlayer(name);
	}

	public boolean isArmor(@NotNull final ItemStack itemStack) {
		final var material = itemStack.getType().name();

		return material.endsWith("HELMET")
				|| material.endsWith("CHESTPLATE")
				|| material.endsWith("LEGGINGS")
				|| material.endsWith("BOOTS");
	}

	public EquipmentSlot getMatchingArmorSlot(@NotNull final ItemStack itemStack) {
		final var material = itemStack.getType().name();

		if(material.contains("HELMET")) {
			return EquipmentSlot.HEAD;
		}
		else if(material.contains("CHESTPLATE")) {
			return EquipmentSlot.CHEST;
		}
		else if(material.contains("LEGGINGS")) {
			return EquipmentSlot.LEGS;
		}
		else if(material.contains("BOOTS")) {
			return EquipmentSlot.FEET;
		}
		else {
			return EquipmentSlot.OFF_HAND;
		}
	}

}
