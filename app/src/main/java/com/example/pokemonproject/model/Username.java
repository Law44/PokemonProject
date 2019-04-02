package com.example.pokemonproject.model;

public class Username {
    private String username;
    private String email;
    private String games;

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

    public Username(String username, String email, String games) {
        this.username = username;
        this.email = email;
        this.games = games;
    }
}
