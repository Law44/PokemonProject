package com.example.pokemonproject.model;

public class Combate {
    private Equipo equipo1;
    private Equipo equipo2;
    private int jornada;
    private String idGame;

    public Combate(Equipo equipo1, Equipo equipo2, int jornada, String idGame) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.jornada = jornada;
        this.idGame = idGame;
    }

    public Combate(){

    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public Equipo getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }
}
