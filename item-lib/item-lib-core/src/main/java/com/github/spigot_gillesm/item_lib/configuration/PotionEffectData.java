package com.github.spigot_gillesm.item_lib.configuration;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.potion.PotionEffectType;

@Setter
@Getter
public class PotionEffectData {

    private PotionEffectType potionEffectType;

    private double duration;

    private int amplifier;

}
