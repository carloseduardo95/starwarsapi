package br.com.carloseduardo.starwarsapi.controller;

import br.com.carloseduardo.starwarsapi.model.Planet;
import br.com.carloseduardo.starwarsapi.repository.PlanetRepository;
import br.com.carloseduardo.starwarsapi.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/apilocal"})
public class ApiLocalController {

    private final PlanetRepository planetRepository;
    private final ApiService apiService;
    ObjectMapper objectMapper = new ObjectMapper();

    ApiLocalController(ApiService apiService, PlanetRepository planetRepository) {
        this.apiService = apiService;
        this.planetRepository = planetRepository;
    }

    @GetMapping(path = {"/planets/{id}"})
    public ResponseEntity<String> getPlanetbyId(@PathVariable long id) {
        try {
            Optional<Planet> planet = planetRepository.findById(id);
            if (planet.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(planet));
            }
        } catch (Exception e) {
            System.out.println("Erro "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = {"/planets/"})
    public ResponseEntity<String> getPlanetByNome(@RequestParam String nome) {
        try {
            Optional<Planet> planet = planetRepository.findPlanetByNome(nome);
            if (planet.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(planet));
            }
        } catch (Exception e) {
            System.out.println("Erro "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = {"/planets"})
    public ResponseEntity<String> getAllPlanets() {
        try {
            List<Planet> planetList = planetRepository.findAll();
            if (!planetList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(planetList));
            }
        } catch (Exception e) {
            System.out.println("Erro "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = {"/planets"})
    public ResponseEntity<String> addNewPlanet(@RequestParam String nome, @RequestParam String clima, @RequestParam String terreno) {
        List<String> erros = new ArrayList<>();

        if (nome == null || nome.equals("")) {
            erros.add("nome");
        }
        if (clima == null || clima.equals("")) {
            erros.add("clima");
        }
        if (terreno == null || terreno.equals("")) {
            erros.add("terreno");
        }

        if (!erros.isEmpty()) {
            return ResponseEntity.badRequest().body("Faltou informar: " + String.join(",", erros));
        }

        try {
            Planet planetaCriado = apiService.adicionarNovoPlaneta(nome, clima, terreno);
            return ResponseEntity.status(HttpStatus.CREATED).body(planetaCriado.toString());
        } catch (Exception e) {
            System.out.println("Erro "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(path = {"/planets/{id}"})
    public ResponseEntity <?> deletePlanet(@PathVariable long id) {
        return planetRepository.findById(id)
                .map(result -> {
                    planetRepository.deleteById(id);
                    return ResponseEntity.ok().body("Registro removido com sucesso");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro não encontrado no banco de dados"));
    }
}
