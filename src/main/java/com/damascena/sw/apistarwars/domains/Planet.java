package com.damascena.sw.apistarwars.domains;

import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;

@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String climate;
    private String terrain;

    @Deprecated
    public Planet() {
    }

    public Planet(String climate, String terrain) {
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClimate() {
        return climate;
    }

    public String getTerrain() {
        return terrain;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }
}