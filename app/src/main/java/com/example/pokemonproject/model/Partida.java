package com.example.pokemonproject.model;

import java.util.ArrayList;

public class Partida {

    private String id;
    private String name;
    private float initialMoney;
    private ArrayList<UserGame> users;


    public Partida(){

    }

    public Partida(String id, String name, float initialMoney, ArrayList<UserGame> users){
        this.id = id;
        this.name = name;
        this.initialMoney = initialMoney;
        this.users = users;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(float initialMoney) {
        this.initialMoney = initialMoney;
    }

    public ArrayList<UserGame> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserGame> users) {
        this.users = users;
    }
}
