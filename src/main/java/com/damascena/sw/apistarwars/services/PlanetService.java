package com.damascena.sw.apistarwars.services;

import com.damascena.sw.apistarwars.domains.Planet;
import com.damascena.sw.apistarwars.repositories.PlanetRepository;

import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }
}