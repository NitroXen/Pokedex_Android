package com.example.pruebaapp;

import android.os.AsyncTask;
import android.os.PatternMatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;

public class Utils {
    static HttpURLConnection http;
    static URL url;
    static String country;

    public static ArrayList<String> getTipos(int numPoke) {
        ArrayList<String> types = new ArrayList<>();
        country = Locale.getDefault().getLanguage();
        try {
            url = new URL("https://pokeapi.co/api/v2/pokemon/" + numPoke + "/");
            http = (HttpURLConnection) url.openConnection();
            String json = "";
            if (http.getResponseCode() == 200) {
                BufferedReader bufferIn = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String linea;
                while ((linea = bufferIn.readLine()) != null) {
                    json += linea;
                }
                bufferIn.close();

                JSONObject obj = new JSONObject(json);

                JSONArray arr1 = obj.getJSONArray("types");

                for (int i = 0; i < arr1.length(); i++) {
                    JSONObject objType = arr1.getJSONObject(i);
                    String[] numType = objType.getJSONObject("type").getString("url").split("/");
                    if (country.equals("es")) {
                        for (int j = 0; j < TipoPokeES.values().length; j++) {
                            if (Integer.parseInt(numType[numType.length-1]) == j+1) {
                                types.add(TipoPokeES.values()[j].name().toLowerCase());
                            }
                        }
                    }else{
                        String type = objType.getJSONObject("type").getString("name");
                        types.add(type);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }finally {
            http.disconnect();
        }

        return types;

    }


    public static String getInfoPokemon(String data) {
        ArrayList<String> datos = new ArrayList<>();
        country = Locale.getDefault().getLanguage();
        try {
            url = new URL(data);

            http = (HttpURLConnection) url.openConnection();
            http.connect();


            String json = "";
            if (http.getResponseCode() == 200) {
                BufferedReader bufferIn = new BufferedReader(new InputStreamReader(http.getInputStream()));
                String linea;
                while ((linea = bufferIn.readLine()) != null) {
                    json += linea;
                }
                bufferIn.close();


                JSONObject obj = null;
                try {
                    obj = new JSONObject(json);


                    JSONArray arr = obj.getJSONArray("flavor_text_entries");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject o = arr.getJSONObject(i);
                        if (o.getJSONObject("language").getString("name").equals(country)) {
                            boolean flavor_text = datos.add(o.getString("flavor_text"));
                            if (flavor_text) {
                                System.out.println("oh oh");
                            }
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            http.disconnect();
        }

        return datos.get(new Random().nextInt(datos.size())).replace("\n"," ");
    }
}
