package com.example.pokemonproject.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class ModalEnemyTeam {

    GameActivity context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> listGames;
    private String teamEnemyID;

    public ModalEnemyTeam(final GameActivity context, final String email, String idGame){

        this.context = context;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_selectalineation);
        dialog.setCanceledOnTouchOutside(true);

        final RecyclerView recyclerView =  dialog.findViewById(R.id.rvalineationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, VERTICAL);
        recyclerView.addItemDecoration(itemDecor);


        db.collection("Partidas").document(idGame).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Partida partida = task.getResult().toObject(Partida.class);

                    for (int j = 0; j < partida.getUsers().size(); j++) {
                        if (partida.getUsers().get(j).getUser().getEmail().equals(email)){
                            teamEnemyID = partida.getUsers().get(j).getTeamID();
                            break;
                        }
                    }

                    db.collection("Equipos").document(teamEnemyID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Team team = task.getResult().toObject(Team.class);

                                final EnemyTeamAdapter enemyTeamAdapter = new EnemyTeamAdapter(context);
                                enemyTeamAdapter.setGamesInfoArrayList(team.getEquipo());
                                recyclerView.setAdapter(enemyTeamAdapter);

                                dialog.show();
                            }
                        }
                    });
                }
            }
        });


    }
}
