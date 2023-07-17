package com.example.pruebaapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokemonRespuesta {


    @SerializedName("results")
    @Expose
    private ArrayList<Pokemon> datos;

    public PokemonRespuesta(ArrayList<Pokemon> datos){
        this.datos = datos;
    }

    public ArrayList<Pokemon> getDatos() {
        return datos;
    }

    public void setDatos(ArrayList<Pokemon> datos) {
        this.datos = datos;
    }
}
