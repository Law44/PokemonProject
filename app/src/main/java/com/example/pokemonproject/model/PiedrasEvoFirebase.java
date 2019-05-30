package com.example.pokemonproject.model;

public class PiedrasEvoFirebase {
    private int id;
    private String name;
    private String sprite;

    public PiedrasEvoFirebase(int id, String name, String sprite) {
        this.id = id;
        this.name = name;
        this.sprite = sprite;
    }

    public PiedrasEvoFirebase(){

    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
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
