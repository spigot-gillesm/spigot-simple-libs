package com.github.spigot_gillesm.item_lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PotionEffectDTO {

    //The potion effect type is the key in the map: Map<String, PotionEffectDataDTO>

    @JsonProperty(value = "duration", required = true)
    private double duration;

    @JsonProperty("amplifier")
    private int amplifier;

}
