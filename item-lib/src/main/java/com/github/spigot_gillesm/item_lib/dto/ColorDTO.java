package com.github.spigot_gillesm.item_lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorDTO {

    @JsonProperty(value = "red", required = true)
    private int red;

    @JsonProperty(value = "green", required = true)
    private int green;

    @JsonProperty(value = "blue", required = true)
    private int blue;

}
