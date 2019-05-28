package com.example.pokemonproject.model;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class UserGame {
    private Username user;
    private String teamName;
    private int points;
    private int money;
    private String teamID;
    private String pujasID;
    private String alineationID;
    private ArrayList<String> combatesID;

    public UserGame(){

    }

    public UserGame(Username user, String teamName, int points, String teamID, int money, String pujasID, String alineationID) {
        this.user = user;
        this.teamName = teamName;
        this.points = points;
        this.teamID = teamID;
        this.money = money;
        this.pujasID = pujasID;
        this.alineationID = alineationID;
        this.combatesID = new ArrayList<>();
    }

    public ArrayList<String> getCombatesID() {
        return combatesID;
    }

    public void setCombatesID(ArrayList<String> combatesID) {
        this.combatesID = combatesID;
    }

    public String getAlineationID() {
        return alineationID;
    }

    public void setAlineationID(String alineationID) {
        this.alineationID = alineationID;
    }

    public String getPujasID() {
        return pujasID;
    }

    public void setPujasID(String pujas) {
        this.pujasID = pujas;
    }

    public int getMoney() {
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

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
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
