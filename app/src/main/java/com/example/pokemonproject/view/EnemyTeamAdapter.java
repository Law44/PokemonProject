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

public class EnemyTeamAdapter extends RecyclerView.Adapter<EnemyTeamAdapter.EnemyViewHolder> {

    private GameActivity context;
    ArrayList<Pokemon> pokemonsArrayList;
    ArrayList<String> listGames;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public EnemyTeamAdapter(GameActivity context){
        this.context = context;
    }

    @NonNull
    @Override
    public EnemyTeamAdapter.EnemyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EnemyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_enemy_team, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final EnemyTeamAdapter.EnemyViewHolder viewHolder, final int i) {

        viewHolder.tvName.setText(pokemonsArrayList.get(i).getName());
        viewHolder.tvId.setText(String.valueOf(pokemonsArrayList.get(i).getId()));




        if (pokemonsArrayList.get(i).getTypes().size() == 2){
            int id =  viewHolder.tipo1.getContext().getResources().getIdentifier(pokemonsArrayList.get(i).getTypes().get(0).getType().getName(), "drawable",  viewHolder.tipo1.getContext().getPackageName());
            GlideApp.with(viewHolder.itemView.getContext()).load(id).into( viewHolder.tipo1);
            int id2 =  viewHolder.tipo1.getContext().getResources().getIdentifier(pokemonsArrayList.get(i).getTypes().get(1).getType().getName(), "drawable",  viewHolder.tipo2.getContext().getPackageName());
            GlideApp.with(viewHolder.itemView.getContext()).load(id2).into( viewHolder.tipo2);

            GlideApp.with(viewHolder.itemView.getContext()).load(0).into( viewHolder.tipounico);
        }else {
            int id =  viewHolder.tipo1.getContext().getResources().getIdentifier(pokemonsArrayList.get(i).getTypes().get(0).getType().getName(), "drawable",  viewHolder.tipounico.getContext().getPackageName());
            GlideApp.with(viewHolder.itemView.getContext()).load(id).into( viewHolder.tipounico);
            GlideApp.with(viewHolder.itemView.getContext()).load(0).into( viewHolder.tipo1);
            GlideApp.with(viewHolder.itemView.getContext()).load(0).into( viewHolder.tipo2);
        }
        GlideApp.with(viewHolder.itemView.getContext())
                .load(pokemonsArrayList.get(i).getSprites().front_default)
                .circleCrop()
                .into((ImageView) viewHolder.itemView.findViewById(R.id.imgPokemon));

    }

    @Override
    public int getItemCount() {
        return pokemonsArrayList.size();
    }

    public void setGamesInfoArrayList(ArrayList<Pokemon> pokemonsarray) {
        pokemonsArrayList = pokemonsarray;
    }

    class EnemyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvId;
        ImageView tipo1;
        ImageView tipo2;
        ImageView tipounico;

        EnemyViewHolder(View view){
            super(view);

            tvName = view.findViewById(R.id.tvPokemonName);
            tvId = view.findViewById(R.id.tvPokemonPokedex);
            tipo1 = view.findViewById(R.id.imgPokemonTipo1);
            tipo2 = view.findViewById(R.id.imgPokemonTipo2);
            tipounico = view.findViewById(R.id.imgPokemonTipoUnico);

        }

    }
}
