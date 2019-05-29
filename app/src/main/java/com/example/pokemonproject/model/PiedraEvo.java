package com.example.pokemonproject.model;

public class PiedraEvo {
    private int id;
    private int cantidad;

    public PiedraEvo(int id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public PiedraEvo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
