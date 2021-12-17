package br.com.carloseduardo.starwarsapi.service;

import br.com.carloseduardo.starwarsapi.dto.JsonObject;
import br.com.carloseduardo.starwarsapi.dto.PlanetDto;
import br.com.carloseduardo.starwarsapi.model.Planet;
import br.com.carloseduardo.starwarsapi.repository.PlanetRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final PlanetRepository planetRepository;
    private static final String URL = "https://swapi.dev/api/";

    ApiService(RestTemplate restTemplate, PlanetRepository planetRepository) {
        this.restTemplate = restTemplate;
        this.planetRepository = planetRepository;
    }

    public List<PlanetDto> buscarTodosPlanetasDaApiExterna() {
        List<PlanetDto> planetDtoList = new ArrayList<>();
        boolean continuarBuscando = true;
        Integer page = 1;

        while (continuarBuscando) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(URL + "planets/?page=" + page, HttpMethod.GET, null, String.class);
                if (response.getBody() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JsonObject result = mapper.readValue(response.getBody(), JsonObject.class);

                    if (result != null) {
                        page++;
                        planetDtoList.addAll(result.getResults());
                        continuarBuscando = result.getNext() != null;
                    }
                } else {
                    continuarBuscando = false;
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                continuarBuscando = false;
            }
        }
        return planetDtoList;
    }

    public Planet adicionarNovoPlaneta(String nome, String clima, String terreno) {
        List<PlanetDto> planetDto = buscarTodosPlanetasDaApiExterna();

        Planet planet = new Planet();
        planet.setNome(nome);
        planet.setClima(clima);
        planet.setTerreno(terreno);

        if (planetDto != null && !planetDto.isEmpty()) {
            Optional<PlanetDto> planeta = planetDto.stream().filter(p -> nome.equals(p.getName())).findFirst();
            planeta.ifPresent(dto -> planet.setQuantidadeAparicoesEmFilmes(dto.getFilms().size()));
        } else {
            planet.setQuantidadeAparicoesEmFilmes(0);
        }
        return planetRepository.save(planet);
    }
}
