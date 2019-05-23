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

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AlineationAdapter extends RecyclerView.Adapter<AlineationAdapter.AlineationViewHolder> {

    private GameActivity context;
    ArrayList<Pokemon> pokemonsArrayList;
    private String alineationID;
    private int alineationPos;
    int numbergames;
    String idLastGame;
    ArrayList<String> listGames;
    Dialog dialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AlineationAdapter(GameActivity context, String alineationID, int i, int numbergames, String idLastGame, ArrayList<String> listGames, Dialog dialog){
        this.context = context;
        this.alineationID = alineationID;
        this.alineationPos = i;
        this.numbergames = numbergames;
        this.idLastGame = idLastGame;
        this.listGames = listGames;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public AlineationAdapter.AlineationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlineationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alineation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlineationAdapter.AlineationViewHolder viewHolder, final int i) {
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

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Alineaciones").document(alineationID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Alineation alineation = task.getResult().toObject(Alineation.class);
                            alineation.getLista().set(alineationPos, pokemonsArrayList.get(i));
                            db.collection("Alineaciones").document(alineationID).update("lista", alineation.getLista());
                        }

                    }
                });
                if (alineationPos == 0) {
                    GlideApp.with(context)
                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                            .into((ImageView) context.findViewById(R.id.imgSilueta));
                }
                else if (alineationPos == 1) {
                    GlideApp.with(context)
                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                            .into((ImageView) context.findViewById(R.id.imgSilueta2));
                }
                else if (alineationPos == 2) {
                    GlideApp.with(context)
                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                            .into((ImageView) context.findViewById(R.id.imgSilueta3));
                }
                else if (alineationPos == 3) {
                    GlideApp.with(context)
                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                            .into((ImageView) context.findViewById(R.id.imgSilueta4));
                }
                else if (alineationPos == 4) {
                    GlideApp.with(context)
                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                            .into((ImageView) context.findViewById(R.id.imgSilueta5));
                }
                else if (alineationPos == 5) {
                    GlideApp.with(context)
                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                            .into((ImageView) context.findViewById(R.id.imgSilueta6));
                }
                dialog.dismiss();

            }
        });


    }

    @Override
    public int getItemCount() {
        return pokemonsArrayList.size();
    }

    public void setGamesInfoArrayList(ArrayList<Pokemon> pokemonsarray) {
        pokemonsArrayList = pokemonsarray;
    }

    class AlineationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvId;
        ImageView tipo1;
        ImageView tipo2;
        ImageView tipounico;

        AlineationViewHolder(View view){
            super(view);

            tvName = view.findViewById(R.id.tvPokemonName);
            tvId = view.findViewById(R.id.tvPokemonPokedex);
            tipo1 = view.findViewById(R.id.imgPokemonTipo1);
            tipo2 = view.findViewById(R.id.imgPokemonTipo2);
            tipounico = view.findViewById(R.id.imgPokemonTipoUnico);
        }

    }
}
