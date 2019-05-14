package com.example.pokemonproject.model;

import com.firebase.ui.auth.data.model.User;

import java.util.ArrayList;

public class Username {
    private String username;
    private String email;
    private String games;
    private String lastGame;
    private ArrayList<String> listGames;

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

    public ArrayList<String> getListGames() {
        return listGames;
    }

    public void setListGames(ArrayList<String> listGames) {
        this.listGames = listGames;
    }

    public Username(){

    }

    public Username(String username, String email, String games, String lastGame, ArrayList<String> listGames) {
        this.username = username;
        this.email = email;
        this.games = games;
        this.lastGame = lastGame;
        this.listGames = listGames;
    }
}
