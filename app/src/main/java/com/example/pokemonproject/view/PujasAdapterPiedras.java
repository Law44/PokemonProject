package com.example.pokemonproject.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.PiedrasEvoUser;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.Username;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PujasAdapterPiedras extends RecyclerView.Adapter<PujasAdapterPiedras.PokemonViewHolder> {

    private GameActivity context;
    ArrayList<PiedrasEvoUser> piedrasArrayList;
    String lastgame;
    String games;
    ArrayList<String> listGame;
    Username creator;
    MercadoFragment mercadoFragment;
    Team team;
    Map<Integer, Integer> totalPujas;
    ArrayList<Integer> pujas;
    int money;
    ArrayList<List<Moves>> movements;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PujasAdapterPiedras(GameActivity context, String lastgame, MercadoFragment mercadoFragment, Team team, Map<Integer, Integer> totalPujas, ArrayList<Integer> pujas, ArrayList<List<Moves>> movements){

        this.context = context;
        this.lastgame = lastgame;
        this.mercadoFragment = mercadoFragment;
        this.team = team;
        this.totalPujas = totalPujas;
        this.pujas = pujas;
        this.money = money;
        this.movements = movements;

    }

    @NonNull
    @Override
    public PujasAdapterPiedras.PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PokemonViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_piedra_mercado, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PujasAdapterPiedras.PokemonViewHolder viewHolder, final int i) {
        viewHolder.setPokemon(piedrasArrayList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return piedrasArrayList.size();
    }

    public void setPokemonPujas(ArrayList<PiedrasEvoUser> pokemonarray) {
        piedrasArrayList = pokemonarray;
    }

    class PokemonViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PokemonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }



        public void setPokemon(final PiedrasEvoUser model, final int position) {
            TextView tvName = view.findViewById(R.id.tvPokemonNameMercado);
            TextView tvId = view.findViewById(R.id.tvPokemonPokedexMercado);
            TextView tvCostePokemon = view.findViewById(R.id.tvPokemonCosteMercado);
            ImageView fondo = view.findViewById(R.id.imgFondo);
            ImageView button = view.findViewById(R.id.imgButtonPagar);


            tvName.setText(model.getName());
            tvId.setText(String.valueOf(model.getId()));
            tvCostePokemon.setText(String.valueOf(100));


            GlideApp.with(view)
                    .load(R.drawable.pokemondollar)
                    .into((ImageView) view.findViewById(R.id.imgPokedolar));

            if (Integer.parseInt(String.valueOf(pujas.get(position))) > 0){
                fondo.setBackgroundColor(view.getResources().getColor(R.color.colorBuy));
            }
            else {
                fondo.setBackgroundColor(view.getResources().getColor(R.color.searchWhite));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ModalComprarPiedra(view.getContext(), model, mercadoFragment, lastgame, position, team, totalPujas, pujas, view);
                }
            });

        }
    }


}
