package com.example.pruebaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    Retrofit rt;

    RecyclerView rvLista;
    private ListaPokemonAdapter adapter;

    private int offset;
    private boolean aptoCarga;
    private StrictMode.ThreadPolicy policy;
    private ConnectivityManager manager;

    public interface PokeapiServiceInterface{

        @GET("pokemon")
        Call<PokemonRespuesta> devuelta(@Query("limit") int limit,@Query("offset") int offset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean conectado = info != null && info.isConnectedOrConnecting();

        if(!conectado){
            Toast.makeText(this, getString(R.string.conection), Toast.LENGTH_LONG).show();
        }

        rvLista = findViewById(R.id.rvLista);
        adapter = new ListaPokemonAdapter(getApplicationContext());
        rvLista.setAdapter(adapter);
        rvLista.setHasFixedSize(true);



        GridLayoutManager grid = new GridLayoutManager(this,3);
        rvLista.setLayoutManager(grid);

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        rvLista.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0){
                    int visibleItemCount = grid.getChildCount();
                    int totalItemCount = grid.getItemCount();
                    int pastVisiblesItems = grid.findFirstVisibleItemPosition();

                    if(aptoCarga){
                        if((visibleItemCount+pastVisiblesItems)>=totalItemCount){
                            offset+=20;
                            aptoCarga = false;

                            cargarPokemons(offset);

                        }
                    }
                }
            }
        });

        rt = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        aptoCarga = true;
        offset = 0;
        cargarPokemons(offset);

    }


    public void cargarPokemons(int offset){

        PokeapiServiceInterface service = rt.create(PokeapiServiceInterface.class);
        Call<PokemonRespuesta> pokeCall = service.devuelta(20,offset);

        pokeCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoCarga = true;
                if(response.isSuccessful()){


                    ArrayList<Pokemon> poke = response.body().getDatos();

                    adapter.getDatos(poke);


                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoCarga = true;
            }
        });


    }
}