package com.example.pokemonproject.view;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Pokemon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AlineationAdapter extends RecyclerView.Adapter<AlineationAdapter.AlineationViewHolder> {

    private GameActivity context;
    ArrayList<Pokemon> pokemonsArrayList;
    private String alineationID;
    private int alineationPos;
    int numbergames;
    String idLastGame;
    ArrayList<String> listGames;
    ArrayList<List<Moves>> movements;
    Dialog dialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AlineationAdapter(GameActivity context, String alineationID, int i, int numbergames, String idLastGame, ArrayList<String> listGames, Dialog dialog, ArrayList<List<Moves>> movements){
        this.context = context;
        this.alineationID = alineationID;
        this.alineationPos = i;
        this.numbergames = numbergames;
        this.idLastGame = idLastGame;
        this.listGames = listGames;
        this.dialog = dialog;
        this.movements = movements;
    }

    @NonNull
    @Override
    public AlineationAdapter.AlineationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlineationViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alineation, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AlineationAdapter.AlineationViewHolder viewHolder, int position) {
        int i = position;
        viewHolder.tvName.setText(pokemonsArrayList.get(i).getName());
        viewHolder.tvId.setText(String.valueOf(pokemonsArrayList.get(i).getId()));

        if (movements.get(i).size() > 0) {
            viewHolder.move1.setText(movements.get(i).get(0).move.name);
        }
        if (movements.get(i).size() > 1) {
            viewHolder.move2.setText(movements.get(i).get(1).move.name);
        }
        if (movements.get(i).size() > 2) {
            viewHolder.move3.setText(movements.get(i).get(2).move.name);
        }
        if (movements.get(i).size() > 3) {
            viewHolder.move4.setText(movements.get(i).get(3).move.name);
        }



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
                        Alineation alineation = null;
                        boolean presente = false;

                        if (task.isSuccessful()) {
                            alineation = task.getResult().toObject(Alineation.class);


                            for (int j = 0; j < alineation.getLista().size(); j++) {
                                if (alineation.getLista().get(j) != null) {
                                    if (alineation.getLista().get(j).getId() == pokemonsArrayList.get(i).getId()) {
                                        presente = true;
                                    }
                                }
                            }
                            if (presente) {
                                Toast.makeText(context, "Ese pokemon ya esta en la alineación", Toast.LENGTH_LONG).show();

                            } else {
                                alineation.getLista().set(alineationPos, pokemonsArrayList.get(i));
                                db.collection("Alineaciones").document(alineationID).update("lista", alineation.getLista());

                                if (alineationPos == 0) {
                                    GlideApp.with(context)
                                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                                            .into((ImageView) context.findViewById(R.id.imgSilueta));
                                } else if (alineationPos == 1) {
                                    GlideApp.with(context)
                                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                                            .into((ImageView) context.findViewById(R.id.imgSilueta2));
                                } else if (alineationPos == 2) {
                                    GlideApp.with(context)
                                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                                            .into((ImageView) context.findViewById(R.id.imgSilueta3));
                                } else if (alineationPos == 3) {
                                    GlideApp.with(context)
                                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                                            .into((ImageView) context.findViewById(R.id.imgSilueta4));
                                } else if (alineationPos == 4) {
                                    GlideApp.with(context)
                                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                                            .into((ImageView) context.findViewById(R.id.imgSilueta5));
                                } else if (alineationPos == 5) {
                                    GlideApp.with(context)
                                            .load(pokemonsArrayList.get(i).getSprites().front_default)
                                            .into((ImageView) context.findViewById(R.id.imgSilueta6));
                                }
                                dialog.dismiss();
                            }
                        }

                    }
                });


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

    class AlineationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvId;
        ImageView tipo1;
        ImageView tipo2;
        ImageView tipounico;
        TextView move1;
        TextView move2;
        TextView move3;
        TextView move4;
        AlineationViewHolder(View view){
            super(view);

            tvName = view.findViewById(R.id.tvPokemonName);
            tvId = view.findViewById(R.id.tvPokemonPokedex);
            tipo1 = view.findViewById(R.id.imgPokemonTipo1);
            tipo2 = view.findViewById(R.id.imgPokemonTipo2);
            tipounico = view.findViewById(R.id.imgPokemonTipoUnico);
            move1 = view.findViewById(R.id.alineationMov1);
            move2 = view.findViewById(R.id.alineationMov2);
            move3 = view.findViewById(R.id.alineationMov3);
            move4 = view.findViewById(R.id.alineationMov4);

        }

    }
}
