package br.com.carloseduardo.starwarsapi.repository;

import br.com.carloseduardo.starwarsapi.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
    Optional<Planet> findPlanetByNome(String nome);
}
