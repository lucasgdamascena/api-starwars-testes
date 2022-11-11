package com.damascena.sw.apistarwars;

import com.damascena.sw.apistarwars.domains.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.damascena.sw.apistarwars.common.PlanetConstants.PLANET;
import static com.damascena.sw.apistarwars.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/import_planets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/remove_planets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void createPlanet_returnsCreated() {
        ResponseEntity<Planet> planetResponseEntity = testRestTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(planetResponseEntity.getBody().getId()).isNotNull();
        assertThat(planetResponseEntity.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(planetResponseEntity.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(planetResponseEntity.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        ResponseEntity<Planet> planetResponseEntity = testRestTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planetResponseEntity.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanetByName_ReturnsPlanet() {
        ResponseEntity<Planet> planetResponseEntity =
                testRestTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planetResponseEntity.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        ResponseEntity<Planet[]> planetResponseEntity = testRestTemplate.getForEntity("/planets", Planet[].class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planetResponseEntity.getBody()).hasSize(3);
        assertThat(planetResponseEntity.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByClimate_ReturnsPlanets() {
        ResponseEntity<Planet[]> planetResponseEntity = testRestTemplate.getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planetResponseEntity.getBody()).hasSize(1);
        assertThat(planetResponseEntity.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets() {
        ResponseEntity<Planet[]> planetResponseEntity = testRestTemplate.getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(planetResponseEntity.getBody()).hasSize(1);
        assertThat(planetResponseEntity.getBody()[0]).isEqualTo(TATOOINE);
    }

    @Test
    public void removePlanet_ReturnsNoContent() {
        ResponseEntity<Void> planetResponseEntity =
                testRestTemplate.exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(planetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}