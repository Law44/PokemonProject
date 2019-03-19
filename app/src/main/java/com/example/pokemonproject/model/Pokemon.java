package com.example.pokemonproject.model;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokemon {

    public int id;
    public String name;
    public Sprites sprites;
    public List<Stats> stats;

    public Pokemon(int id, String name, Sprites sprites, List<Stats> stats) {
        this.id = id;
        this.name = name;
        this.sprites = sprites;
        this.stats = stats;
    }
}


