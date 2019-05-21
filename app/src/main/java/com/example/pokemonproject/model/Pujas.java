package com.example.pokemonproject.model;

import java.util.ArrayList;

public class Pujas {

    private ArrayList<Integer> pujas;

    public Pujas(){
        this.pujas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pujas.add(0);
        }
    }

    public ArrayList<Integer> getPujas() {
        return pujas;
    }

    public void setPujas(ArrayList<Integer> pujas) {
        this.pujas = pujas;
    }
}
