package br.com.carloseduardo.starwarsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlanetDto {
    private String name;

    @JsonProperty("rotation_period")
    private String rotationPeriod;

    @JsonProperty("orbital_period")
    private String orbitalPeriod;

    private String diameter;
    private String climate;
    private String gravity;
    private String terrain;

    @JsonProperty("surface_water")
    private String surfaceWater;

    private String population;
    private List<String> residents;
    private List<String> films;
    private Date created;
    private Date edited;
    private String url;
}
