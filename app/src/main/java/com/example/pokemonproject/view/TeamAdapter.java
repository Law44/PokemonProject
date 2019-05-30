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
import com.example.pokemonproject.model.PiedrasEvoUser;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private GameActivity context;
    ArrayList<Pokemon> pokemonsArrayList;
    ArrayList<PiedrasEvoUser> piedrasEvoUsers;
    String teamID, alineationID, piedrasID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TeamAdapter(GameActivity context, ArrayList<PiedrasEvoUser> piedrasEvoUsers, String teamID, String alineationID, String piedrasID){
        this.context = context;
        this.piedrasEvoUsers = piedrasEvoUsers;
        this.teamID = teamID;
        this.alineationID = alineationID;
        this.piedrasID = piedrasID;
    }

    @NonNull
    @Override
    public TeamAdapter.TeamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TeamViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pokemon, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.TeamViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(pokemonsArrayList.get(i).getName());
        viewHolder.tvId.setText(String.valueOf(pokemonsArrayList.get(i).getId()));
        viewHolder.tvHp.setText(String.valueOf(pokemonsArrayList.get(i).getStats().get(5).base_stat));
        viewHolder.tvSpeed.setText(String.valueOf(pokemonsArrayList.get(i).getStats().get(0).base_stat));
        viewHolder.tvAtk.setText(String.valueOf(pokemonsArrayList.get(i).getStats().get(4).base_stat));
        viewHolder.tvDef.setText(String.valueOf(pokemonsArrayList.get(i).getStats().get(3).base_stat));
        viewHolder.tvAtkSp.setText(String.valueOf(pokemonsArrayList.get(i).getStats().get(2).base_stat));
        viewHolder.tvDefSp.setText(String.valueOf(pokemonsArrayList.get(i).getStats().get(1).base_stat));

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
                .into((ImageView) viewHolder.itemView.findViewById(R.id.imgPokemonImageList));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ModalEvolution(context, pokemonsArrayList.get(i), piedrasEvoUsers, teamID, i, pokemonsArrayList, alineationID, piedrasID);
            }
        });


    }

    @Override
    public int getItemCount() {
        return  (pokemonsArrayList != null ? pokemonsArrayList.size() : 0);
    }

    public void setGamesInfoArrayList(ArrayList<Pokemon> pokemonsarray) {
        pokemonsArrayList = pokemonsarray;
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvId;
        TextView tvHp;
        TextView tvSpeed;
        TextView tvAtk;
        TextView tvDef;
        TextView tvAtkSp;
        TextView tvDefSp;
        ImageView tipo1;
        ImageView tipo2;
        ImageView tipounico;

        TeamViewHolder(View view){
            super(view);

            tvName = view.findViewById(R.id.tvPokemonNameList);
            tvId = view.findViewById(R.id.tvPokemonIdList);
            tvHp = view.findViewById(R.id.tvPokemonHpList);
            tvSpeed = view.findViewById(R.id.tvPokemonSpeedList);
            tvAtk = view.findViewById(R.id.tvPokemonAtkList);
            tvDef = view.findViewById(R.id.tvPokemonDefList);
            tvAtkSp = view.findViewById(R.id.tvPokemonAtkSpList);
            tvDefSp = view.findViewById(R.id.tvPokemonDefSpList);
            tipo1 = view.findViewById(R.id.imgPokemonTipo1List);
            tipo2 = view.findViewById(R.id.imgPokemonTipo2List);
            tipounico = view.findViewById(R.id.imgPokemonTipoUnicoList);
        }

    }
}
