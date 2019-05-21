package com.example.pokemonproject.model;

import java.util.ArrayList;

public class Team {

    private ArrayList<Pokemon> equipo;

    public Team(){
        this.equipo = new ArrayList<>();
    }

    public ArrayList<Pokemon> getEquipo() {
        return equipo;
    }

    public void setEquipo(ArrayList<Pokemon> equipo) {
        this.equipo = equipo;
    }
}
