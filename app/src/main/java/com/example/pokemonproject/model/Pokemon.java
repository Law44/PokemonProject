package com.example.pokemonproject.model;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokemon {

    private int id;
    private int life;
    private String name;
    private Sprites sprites;
    private int price;
    private int idEvo;
    private List<Stats> stats;
    private List<Types> types;
    private List<Moves> moves;



    public Pokemon(int id, String name, Sprites sprites, List<Stats> stats, List<Types> types, List<Moves> moves) {
        this.id = id;
        this.name = name;
        this.sprites = sprites;
        this.stats = stats;
        this.types = types;
        this.moves = moves;
        this.life = stats.get(5).base_stat;
    }
    public Pokemon(){}

    public List<Moves> getMoves() {
        return moves;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setMoves(List<Moves> moves) {
        this.moves = moves;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIdEvo() {
        return idEvo;
    }

    public void setIdEvo(int idEvo) {
        this.idEvo = idEvo;
    }

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

    public List<Types> getTypes() {
        return types;
    }

    public void setTypes(List<Types> types) {
        this.types = types;
    }
}


