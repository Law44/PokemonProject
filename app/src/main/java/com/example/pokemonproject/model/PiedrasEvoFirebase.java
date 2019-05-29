package com.example.pokemonproject.model;

public class PiedrasEvoFirebase {
    private int id;
    private String name;

    public PiedrasEvoFirebase(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public PiedrasEvoFirebase(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
