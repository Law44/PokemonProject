package com.example.pokemonproject.model;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokemon {

    private int id;
    private String name;
    private Sprites sprites;

    public Pokemon(int id, String name, Sprites sprites, List<Stats> stats) {
        this.id = id;
        this.name = name;
        this.sprites = sprites;
        this.stats = stats;
    }
    public Pokemon(){}


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    private List<Stats> stats;



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public List<Stats> getStats() {
        return stats;
    }



}


