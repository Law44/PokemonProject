package com.example.pokemonproject.model;

public class PiedrasEvoUser {
    private int id;
    private String name;
    private int cantidad;
    private int precio;

    public PiedrasEvoUser(int id, String name, int cantidad) {
        this.id = id;
        this.name = name;
        this.cantidad = cantidad;
        this.precio = 200;
    }

    public PiedrasEvoUser(){

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
