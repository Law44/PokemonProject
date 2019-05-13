package com.example.pokemonproject.model;

public class Movement {
    public int id;
    public String name;
    public Name names;
    public String power;
    public int pp;
    public int accuracy;
    public int priority;
    public Type type;

    public Movement(int id, String name, Name names, String power, int pp, int accuracy, int priority, Type type) {
        this.id = id;
        this.name = name;
        this.names = names;
        this.power = power;
        this.pp = pp;
        this.accuracy = accuracy;
        this.priority = priority;
        this.type = type;
    }
}
