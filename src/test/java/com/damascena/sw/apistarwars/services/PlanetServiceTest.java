package com.damascena.sw.apistarwars.services;

import static com.damascena.sw.apistarwars.common.PlanetConstants.INVALID_PLANET;
import static com.damascena.sw.apistarwars.common.PlanetConstants.PLANET;

import com.damascena.sw.apistarwars.domains.Planet;
import com.damascena.sw.apistarwars.domains.utils.QueryBuilder;
import com.damascena.sw.apistarwars.repositories.PlanetRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    public void createPlanet_WithValidDate_ReturnsPlanet() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        Planet sutPlanet = planetService.create(PLANET);

        assertThat(sutPlanet).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidDate_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sutPlanet = planetService.get(1L);

        assertThat(sutPlanet).isNotEmpty();
        assertThat(sutPlanet.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty() {
        when(planetRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Planet> sutPlanet = planetService.get(1L);

        assertThat(sutPlanet).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet() {
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet> sutPlanet = planetService.getByName(PLANET.getName());

        assertThat(sutPlanet).isNotEmpty();
        assertThat(sutPlanet.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty() {
        final String name = "Unexisting Name";
        when(planetRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<Planet> sutPlanet = planetService.getByName(name);

        assertThat(sutPlanet.isEmpty());
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        List<Planet> planets = new ArrayList<>() {
            {
                add(PLANET);
            }
        };

        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> sutPlanets = planetService.list(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sutPlanets).isNotEmpty();
        assertThat(sutPlanets).hasSize(1);
        assertThat(sutPlanets.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sutPlanets = planetService.list(PLANET.getClimate(), PLANET.getTerrain());

        assertThat(sutPlanets).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_DoesNotThrowAnyException() {
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() {
        doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

        assertThatThrownBy(() -> planetService.remove(99L)).isInstanceOf(RuntimeException.class);
    }
}