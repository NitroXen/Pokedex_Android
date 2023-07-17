package com.example.pruebaapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoPokemon {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("species")
    @Expose
    private Species s;

    public InfoPokemon(String name, int weight, int height, Species s) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.s = s;

    }

    public String getName() {
        return name;
    }


    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public Species getS() {
        return s;
    }
}
class Species {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;


    public Species(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}