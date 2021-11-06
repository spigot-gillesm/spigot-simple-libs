package com.github.spigot_gillesm.rp_classes.talent.spell.active;

import com.github.spigot_gillesm.rp_classes.player.PlayerManager;
import com.github.spigot_gillesm.rp_classes.talent.spell.MagicType;
import com.github.spigot_gillesm.rp_classes.talent.spell.SpellManager;
import com.github.spigot_gillesm.spell_lib.spell.entity_spell_impl.NextHitSpell;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class HolyStrike extends NextHitSpell {

	public HolyStrike() {
		setCooldown(5);
	}

	private double getDamage(final Player player) {
		return PlayerManager.getSpellRank(player, getClass().getName());
	}

	@Override
	public void onHit(final LivingEntity source, final LivingEntity entity) {
		SpellManager.damageEntity(entity, (Player) source, getDamage((Player) source), MagicType.HOLY);
	}

}
