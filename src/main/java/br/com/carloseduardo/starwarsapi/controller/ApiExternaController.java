package br.com.carloseduardo.starwarsapi.controller;

import br.com.carloseduardo.starwarsapi.dto.PlanetDto;
import br.com.carloseduardo.starwarsapi.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping({"/apiexterna"})
public class ApiExternaController {

    private final RestTemplate restTemplate;
    private static final String URL = "https://swapi.tech/api/";
    private final ApiService apiService;

    ApiExternaController(RestTemplate restTemplate, ApiService apiService) {
        this.restTemplate = restTemplate;
        this.apiService = apiService;
    }

    @GetMapping(path = {"/planets/{id}"})
    public ResponseEntity<String> findPlanetById(@PathVariable long id) {
        ResponseEntity<String> response = restTemplate.exchange(URL + "planets/" + id, HttpMethod.GET, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok().body(response.getBody());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = {"/planets/pagina/"})
    public ResponseEntity<String> findAll(@RequestParam(defaultValue = "1", required = false) Integer page) {
        ResponseEntity<String> response = restTemplate.exchange(URL + "planets/?page=" + page, HttpMethod.GET, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok().body(response.getBody());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = {"/planets"})
    public ResponseEntity<String> getAllPlanets() {
        List<PlanetDto> planetDtoList = apiService.buscarTodosPlanetasDaApiExterna();
        if (!planetDtoList.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return ResponseEntity.ok().body(objectMapper.writeValueAsString(planetDtoList));
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
