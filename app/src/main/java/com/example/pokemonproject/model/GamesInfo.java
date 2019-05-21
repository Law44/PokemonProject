package com.example.pokemonproject.model;

public class GamesInfo {
    private String id;
    private String nameLeague;
    private String teamName;
    private float money;
    private int points;

    public GamesInfo(String id, String nameLeague, String teamName, float money, int points) {
        this.id = id;
        this.nameLeague = nameLeague;
        this.teamName = teamName;
        this.money = money;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameLeague() {
        return nameLeague;
    }

    public void setNameLeague(String nameLeague) {
        this.nameLeague = nameLeague;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
