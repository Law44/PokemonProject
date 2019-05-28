package com.example.pokemonproject.model;

import java.util.ArrayList;

public class Equipo {

    private String username;
    private String useremail;
    private String imguser;
    private Alineation alineacion;

    public Equipo(String username, String useremail, Alineation alineacion, String imguser) {
        this.username = username;
        this.useremail = useremail;
        this.alineacion = alineacion;
        this.imguser = imguser;
    }

    public Equipo (){

    }

    public String getImguser() {
        return imguser;
    }

    public void setImguser(String imguser) {
        this.imguser = imguser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public Alineation getAlineacion() {
        return alineacion;
    }

    public void setAlineacion(Alineation alineacion) {
        this.alineacion = alineacion;
    }
}
