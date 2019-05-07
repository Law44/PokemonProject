package com.example.pokemonproject.model;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class UserGame {
    private Username user;
    private String teamName;
    private int points;
    private float money;
    private ArrayList<Pokemon> team;

    public UserGame(){

    }

    public UserGame(Username user, String teamName, int points, ArrayList<Pokemon> team, float money) {
        this.user = user;
        this.teamName = teamName;
        this.points = points;
        this.team = team;
        this.money = money;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<Pokemon> getTeam() {
        return team;
    }

    public void setTeam(ArrayList<Pokemon> team) {
        this.team = team;
    }

    public Username getUser() {
        return user;
    }

    public void setUser(Username user) {
        this.user = user;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
