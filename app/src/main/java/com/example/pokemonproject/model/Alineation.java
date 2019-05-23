package com.example.pokemonproject.model;

import java.util.ArrayList;

public class Alineation {
    private ArrayList<Pokemon> lista;

    public Alineation(){
        lista = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lista.add(null);
        }
    }

    public ArrayList<Pokemon> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Pokemon> lista) {
        this.lista = lista;
    }
}
