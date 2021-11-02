package com.github.spigot_gillesm.spell_lib.spell;

import com.github.spigot_gillesm.format_lib.Formatter;
import com.github.spigot_gillesm.spell_lib.Spell;
import com.github.spigot_gillesm.spell_lib.SpellCooldown;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;

public abstract class SimpleSpell implements Spell {

	@Setter(AccessLevel.PROTECTED)
	private double cooldown = 0;

	@Setter(AccessLevel.PROTECTED)
	private String message;

	@Setter(AccessLevel.PROTECTED)
	private String onCooldownMessage = "&cYou must wait another %t seconds.";

	@Getter
	private final SpellCooldown spellCooldown = new SpellCooldown();

	@Override
	public void cast(@NotNull final LivingEntity entity) {
		final var id = String.valueOf(entity.getEntityId());

		if(cooldown > 0 && spellCooldown.isOnCooldown(id)) {
			Formatter.tell(entity,
					onCooldownMessage.replace("%t",
					spellCooldown.remainingTime(id, cooldown).setScale(2, RoundingMode.HALF_UP).toString()));
		} else {
			if(!StringUtils.isBlank(message)) {
				Formatter.tell(entity, message);
			}
			run(entity);
			spellCooldown.start(id, cooldown);
		}
	}

}
