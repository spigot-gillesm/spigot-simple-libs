package com.github.spigot_gillesm.rp_classes.talent.spell;

import com.github.spigot_gillesm.format_lib.Formatter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@UtilityClass
public class SpellManager {

	public double damageEntity(final LivingEntity target, final Player source, final Double damage, final MagicType magicType) {
		target.damage(damage, source);
		Formatter.tell(source, "&7You damaged &c" + target.getName() + " &7for " + magicType.getColorCode() +
				damage + "&7 damage.");
		return damage;
	}

}
