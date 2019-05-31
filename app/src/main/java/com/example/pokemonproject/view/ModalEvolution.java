package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.PiedrasEvoUser;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ModalEvolution {
    private Pokemon pokemon, pokemonEvolucionado;
    private String idGame;
    private Context context;
    private MercadoFragment fragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<UserGame> listUsers;
    int piedras = 0;

    public ModalEvolution(final Context context, final Pokemon model, final ArrayList<PiedrasEvoUser> piedrasEvoUsers, final String teamID, final int position, final ArrayList<Pokemon> pokemonsArrayList, final String alineationID, final String piedrasID) {
        this.context = context;
        this.pokemon = model;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_evolution);
        dialog.setCanceledOnTouchOutside(true);
        GlideApp.with(context).load(model.getSprites().front_default).into((ImageView) dialog.findViewById(R.id.imgPokemonModal));
        GlideApp.with(context).load(R.drawable.boxpokemonmodal).centerCrop().into((ImageView) dialog.findViewById(R.id.imgModalBoxFondo));


        if (model.getTypes().size()==2){
            int id = context.getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", context.getPackageName());
            GlideApp.with(context).load(id).into((ImageView) dialog.findViewById(R.id.imgTipo1Modal));
            int id2 = context.getResources().getIdentifier(model.getTypes().get(1).getType().getName(), "drawable", context.getPackageName());
            GlideApp.with(context).load(id2).into((ImageView) dialog.findViewById(R.id.imgTipo2Modal));
        }else {
            int id = context.getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", context.getPackageName());
            GlideApp.with(context).load(id).into((ImageView) dialog.findViewById(R.id.imgTipoUnicoModal));
        }
        Button btnClose = dialog.findViewById(R.id.btnCloseModal);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        TextView piedrasNecesarias = dialog.findViewById(R.id.piedrasNecesarias);

        TextView piedrasPosesion = dialog.findViewById(R.id.piedrasPosesion);

        Button btnEvolucionar = dialog.findViewById(R.id.btnEvolucionar);


        if (model.getPiedrasEvo().getCantidad() > 0) {
            piedrasNecesarias.setText(model.getPiedrasEvo().getCantidad() + " Piedras necesarias para evolucionar");
            for (int i = 0; i < piedrasEvoUsers.size(); i++) {
                if (piedrasEvoUsers.get(i).getId() == model.getPiedrasEvo().getId()){
                    piedras =  piedrasEvoUsers.get(i).getCantidad();
                    break;
                }
            }

            piedrasPosesion.setText("Tienes " + piedras + " piedras");

        }

        else {
            piedrasNecesarias.setText("Este pokemon no puede evolucionar");
            btnEvolucionar.setVisibility(View.INVISIBLE);
            btnEvolucionar.setEnabled(false);
        }

        btnEvolucionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (piedras >= pokemon.getPiedrasEvo().getCantidad()){
                    db.collection("ListaPokemon").whereEqualTo("id", pokemon.getIdEvo()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    pokemonEvolucionado = snapshot.toObject(Pokemon.class);
                                    pokemonEvolucionado.setMoves(model.getMoves());
                                }

                                db.collection("Equipos").document(teamID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            final Team team = task.getResult().toObject(Team.class);

                                            db.collection("Alineaciones").document(alineationID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        Alineation alineation = task.getResult().toObject(Alineation.class);

                                                        for (int i = 0; i < team.getEquipo().size(); i++) {
                                                            if (team.getEquipo().get(i).getId() == model.getId()){
                                                                team.getEquipo().set(i, pokemonEvolucionado);
                                                                pokemonsArrayList.set(position, pokemonEvolucionado);
                                                                db.collection("Equipos").document(teamID).update("equipo", team.getEquipo());
                                                                break;
                                                            }
                                                        }

                                                        for (int i = 0; i < alineation.getLista().size(); i++) {
                                                            if (alineation.getLista().get(i) != null) {
                                                                if (alineation.getLista().get(i).getId() == model.getId()) {
                                                                    alineation.getLista().set(i, pokemonEvolucionado);
                                                                    db.collection("Alineaciones").document(alineationID).update("lista", alineation.getLista());
                                                                    break;
                                                                }
                                                            }
                                                        }

                                                        for (int i = 0; i < piedrasEvoUsers.size(); i++) {
                                                            if (piedrasEvoUsers.get(i).getId() == model.getPiedrasEvo().getId()){
                                                                piedrasEvoUsers.get(i).setCantidad(piedrasEvoUsers.get(i).getCantidad()-pokemon.getPiedrasEvo().getCantidad());
                                                                if (piedrasEvoUsers.get(i).getCantidad() > 0) {
                                                                    db.collection("PiedrasUser").document(piedrasID).update("piedras", piedrasEvoUsers);
                                                                }
                                                                else if (piedrasEvoUsers.get(i).getCantidad() == 0){
                                                                    piedrasEvoUsers.remove(i);
                                                                    db.collection("PiedrasUser").document(piedrasID).update("piedras", piedrasEvoUsers);
                                                                }
                                                                break;
                                                            }
                                                        }

                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(context, "No tienes suficientes piedras para evolucionar", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();

    }
}
