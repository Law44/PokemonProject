package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.MovementFirebase;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
