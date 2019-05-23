package com.example.pokemonproject.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PujasAdapter extends RecyclerView.Adapter<PujasAdapter.PokemonViewHolder> {

    private GameActivity context;
    ArrayList<Pokemon> pokemonArrayList;
    String lastgame;
    String games;
    ArrayList<String> listGame;
    Username creator;
    MercadoFragment mercadoFragment;
    Team team;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PujasAdapter(GameActivity context, String lastgame, MercadoFragment mercadoFragment, Team team){

        this.context = context;
        this.lastgame = lastgame;
        this.mercadoFragment = mercadoFragment;
        this.team = team;

    }

    @NonNull
    @Override
    public PujasAdapter.PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PokemonViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pokemon_mercado, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PujasAdapter.PokemonViewHolder viewHolder, final int i) {
        viewHolder.setPokemon(pokemonArrayList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return pokemonArrayList.size();
    }

    public void setPokemonPujas(ArrayList<Pokemon> pokemonarray) {
        pokemonArrayList = pokemonarray;
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PokemonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }



        public void setPokemon(final Pokemon model, final int position) {
            TextView tvName = view.findViewById(R.id.tvPokemonNameMercado);
            TextView tvId = view.findViewById(R.id.tvPokemonPokedexMercado);
            TextView tvCostePokemon = view.findViewById(R.id.tvPokemonCosteMercado);
            ImageView tipo1 = view.findViewById(R.id.imgPokemonTipo1Mercado);
            ImageView tipo2 = view.findViewById(R.id.imgPokemonTipo2Mercado);
            ImageView tipounico = view.findViewById(R.id.imgPokemonTipoUnicoMercado);
            ImageView button = view.findViewById(R.id.imgButtonPagar);


            tvName.setText(model.getName());
            tvId.setText(String.valueOf(model.getId()));
            tvCostePokemon.setText(String.valueOf(model.getPrice()));
            if (model.getTypes().size() == 2){
                int id = tipo1.getContext().getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", tipo1.getContext().getPackageName());
                GlideApp.with(context).load(id).into(tipo1);
                int id2 = tipo1.getContext().getResources().getIdentifier(model.getTypes().get(1).getType().getName(), "drawable", tipo2.getContext().getPackageName());
                GlideApp.with(context).load(id2).into(tipo2);

                GlideApp.with(context).load(0).into(tipounico);
            }else {
                int id = tipo1.getContext().getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", tipounico.getContext().getPackageName());
                GlideApp.with(context).load(id).into(tipounico);
                GlideApp.with(context).load(0).into(tipo1);
                GlideApp.with(context).load(0).into(tipo2);
            }

            GlideApp.with(view)
                    .load(model.getSprites().front_default)
                    .circleCrop()
                    .into((ImageView) view.findViewById(R.id.imgPokemonMercado));
            GlideApp.with(view)
                    .load(R.drawable.icons8_pokeball_80)
                    .circleCrop()
                    .into((ImageView)view.findViewById(R.id.imgButtonPagar));

            GlideApp.with(view)
                    .load(R.drawable.pokemondollar)
                    .into((ImageView) view.findViewById(R.id.imgPokedolar));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ModalComprarPokemon(view.getContext(), model, mercadoFragment, lastgame, position, team);
                }
            });

        }
    }


}
