package com.example.pokemonproject.model;

import java.util.ArrayList;

public class PiedrasUser {

    private ArrayList<PiedrasEvoUser> piedras;

    public PiedrasUser() {
        this.piedras = new ArrayList<>();
    }

    public ArrayList<PiedrasEvoUser> getPiedras() {
        return piedras;
    }

    public void setPiedras(ArrayList<PiedrasEvoUser> piedras) {
        this.piedras = piedras;
    }
}
