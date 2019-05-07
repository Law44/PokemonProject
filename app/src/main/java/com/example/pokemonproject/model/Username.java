package com.example.pokemonproject.model;

import com.firebase.ui.auth.data.model.User;

public class Username {
    private String username;
    private String email;
    private String games;
    private String lastGame;

    public String getLastGame() {
        return lastGame;
    }

    public void setLastGame(String lastGame) {
        this.lastGame = lastGame;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public Username(){

    }

    public Username(String username, String email, String games, String lastGame) {
        this.username = username;
        this.email = email;
        this.games = games;
        this.lastGame = lastGame;
    }
}
