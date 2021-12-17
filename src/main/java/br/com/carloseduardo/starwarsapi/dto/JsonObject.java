package br.com.carloseduardo.starwarsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JsonObject {
    private String count;
    private String next;
    private String previous;
    private List<PlanetDto> results;
}
