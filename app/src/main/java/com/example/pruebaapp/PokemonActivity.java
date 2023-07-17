package com.example.pruebaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class PokemonActivity extends AppCompatActivity {

    private int num;
    Retrofit retro;
    TextView tvPokeName, tvPokeWeigth, tvPokeHeight, tvPokeDesc, tvPokeType;
    ImageView ivPokeImage;
    Button btnReturn;
    TextToSpeech tts;
    public interface PokeApiDatosService{

        @GET("pokemon/{num}")
        Call<InfoPokemon> getInfo(@Path("num") int num);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);


        tvPokeName = findViewById(R.id.tvPokeName);
        tvPokeWeigth = findViewById(R.id.tvPokeWeight);
        tvPokeHeight = findViewById(R.id.tvPokeHeight);
        ivPokeImage = findViewById(R.id.ivPokeImage);
        tvPokeType = findViewById(R.id.tvPokeType);
        tvPokeDesc = findViewById(R.id.tvPokeDesc);
        btnReturn = findViewById(R.id.btnReturn);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });

        Bundle b = getIntent().getExtras();
        if(b!= null){
            num = b.getInt("NUM");
        }


        Glide.with(this)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+num+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPokeImage);

        retro = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        PokeApiDatosService service = retro.create(PokeApiDatosService.class);

        Call<InfoPokemon> datos = service.getInfo(num);

        datos.enqueue(new Callback<InfoPokemon>() {
            @Override
            public void onResponse(Call<InfoPokemon> call, Response<InfoPokemon> response) {
                if(response.isSuccessful()){

                    InfoPokemon info = response.body();

                    String peso = getString(R.string.weight)+info.getWeight() +" KG";

                    String altura = getString(R.string.height)+info.getHeight()+" M";

                    ArrayList<String> tipos = Utils.getTipos(num);

                    String t = tipos.size()==2?tipos.get(0)+" "+getString(R.string.and)+" "+tipos.get(1):tipos.get(0);

                    String nameType = getString(R.string.type)+" "+ t;

                    String dato = Utils.getInfoPokemon(info.getS().getUrl());

                    String pokeDato =  getString(R.string.description)+ dato;

                    tvPokeName.setText(info.getName());

                    tvPokeWeigth.setText(peso);

                    tvPokeHeight.setText(altura);

                    tvPokeType.setText(nameType);

                    tvPokeDesc.setText(pokeDato);

                    voz(info.getName()+getString(R.string.descriptionVoz)+ t +".   "+dato);
                }
            }

            @Override
            public void onFailure(Call<InfoPokemon> call, Throwable t) {

            }
        });
        btnReturn.setOnClickListener(view -> retorno());


    }

    public void voz(String muestra){
        tts.speak(muestra,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    public void retorno(){
        finish();
    }
}