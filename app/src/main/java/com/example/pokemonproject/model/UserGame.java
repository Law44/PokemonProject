package com.example.pokemonproject.model;

import android.widget.EditText;

public class UserGame {
    private Username user;
    private String teamName;

    public UserGame(){

    }

    public UserGame(Username user, String teamName) {
        this.user = user;
        this.teamName = teamName;
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
