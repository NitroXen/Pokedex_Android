package com.example.pruebaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ListaPokemonAdapter extends RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder>{

    ArrayList<Pokemon> dataSet;
    Context context;


    public ListaPokemonAdapter(Context context){
        this.context = context;
        dataSet = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_pokemon,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon p = dataSet.get(position);
        holder.tvNombre.setText(p.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+p.getNum()+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPokemon);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PokemonActivity.class);
                System.out.println(p.getNum());
                i.putExtra("NUM", p.getNum());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void getDatos(ArrayList<Pokemon> datos){
        dataSet.addAll(datos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivPokemon;
        TextView tvNombre;
        LinearLayout ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPokemon = itemView.findViewById(R.id.ivPokemon);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            ll = itemView.findViewById(R.id.ll);
        }


    }



}
