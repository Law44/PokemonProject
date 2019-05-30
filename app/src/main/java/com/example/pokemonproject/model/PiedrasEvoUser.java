package com.example.pokemonproject.model;

public class PiedrasEvoUser {
    private int id;
    private String name;
    private int cantidad;
    private int precio;
    private String sprite;


    public PiedrasEvoUser(int id, String name, int cantidad, String sprite) {
        this.id = id;
        this.name = name;
        this.cantidad = cantidad;
        this.precio = 200;
        this.sprite = sprite;
    }

    public PiedrasEvoUser(){

    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
