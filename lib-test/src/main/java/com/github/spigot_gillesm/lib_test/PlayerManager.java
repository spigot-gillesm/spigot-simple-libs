package com.github.spigot_gillesm.lib_test;

import com.github.spigot_gillesm.lib_test.profession.Profession;
import com.github.spigot_gillesm.lib_test.profession.ProfessionManager;
import com.github.spigot_gillesm.player_lib.DataManager;
import com.github.spigot_gillesm.player_lib.PlayerTag;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@UtilityClass
public class PlayerManager {

	public void setPlayerProfession(final Player player, final Profession profession) {
		DataManager.getData(player).setTagValue(PlayerTag.PROFESSION, ProfessionManager.getId(profession));
	}

	public void setPlayerProfession(final Player player, final String id) {
		ProfessionManager.getProfession(id).ifPresent(p -> DataManager.getData(player).setTagValue(PlayerTag.PROFESSION, id.toUpperCase()));
	}

	public void removePlayerProfession(final Player player) {
		DataManager.getData(player).removeValue(PlayerTag.PROFESSION);
	}

	public void decrementLevel(final Player player) {
		player.setLevel(player.getLevel() - 1);
	}
	public Optional<Profession> getProfession(final Player player) {
		final var id = DataManager.getData(player).getTagValue(PlayerTag.PROFESSION);
		return id != null ? ProfessionManager.getProfession(id.toString().toUpperCase()) : Optional.empty();
	}

	public boolean hasProfession(final Player player) {
		return getProfession(player).isPresent();
	}

	public boolean canEquip(@NotNull final Player player, @NotNull final ItemStack itemStack) {
		final var professions = ItemManager.getItemProfessions(itemStack);

		//If no professions is required for the item -> always equipable
		if(professions.isEmpty()) {
			return true;
		} else {
			//Check if the player has one of the listed profession (or no profession at all)
			return getProfession(player).filter(professions::contains).isPresent();
		}
	}

}
