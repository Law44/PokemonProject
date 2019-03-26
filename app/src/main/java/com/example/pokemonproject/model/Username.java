package com.example.pokemonproject.model;

public class Username {
    private String username;
    private String email;
    private String created;

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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Username(String username, String email, String created) {
        this.username = username;
        this.email = email;
        this.created = created;
    }
}
