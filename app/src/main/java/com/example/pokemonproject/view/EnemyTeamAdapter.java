package com.example.pokemonproject.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Pokemon;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EnemyTeamAdapter extends RecyclerView.Adapter<PokemonViewHolder> {

    private GameActivity context;
    ArrayList<Pokemon> pokemonsArrayList;
    ArrayList<String> listGames;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EnemyTeamAdapter(GameActivity context){
        this.context = context;
    }


    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PokemonViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pokemon, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PokemonViewHolder viewHolder, final int i) {
        viewHolder.setPokemon(pokemonsArrayList.get(i));




    }

    @Override
    public int getItemCount() {
        return  (pokemonsArrayList != null ? pokemonsArrayList.size() : 0);
    }

    public void setGamesInfoArrayList(ArrayList<Pokemon> pokemonsarray) {
        pokemonsArrayList = pokemonsarray;
    }


}
