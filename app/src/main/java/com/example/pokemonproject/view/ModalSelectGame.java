package com.example.pokemonproject.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ModalSelectGame {

    GameActivity context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> listGames;
    ArrayList<GamesInfo> gamesInfos = new ArrayList<>();

    public ModalSelectGame(final GameActivity context, final String id){

        this.context = context;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_selectgame);
        dialog.setCanceledOnTouchOutside(true);

        db.collection("Users")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listGames = (ArrayList<String>) document.get("listGames");
                            }
                        }
                        for (int i = 0; i < listGames.size(); i++) {

                            final int finalI = i;
                            db.collection("Partidas")
                                    .document(listGames.get(i))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            Partida partida = documentSnapshot.toObject(Partida.class);
                                            ArrayList<UserGame> listUsers = partida.getUsers();
                                            for (int j = 0; j < listUsers.size(); j++) {
                                                if (listUsers.get(j).getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                    String nameLeague = partida.getName();
                                                    GamesInfo games = new GamesInfo(listGames.get(finalI), nameLeague, listUsers.get(j).getTeamName(), listUsers.get(j).getMoney(), listUsers.get(j).getPoints());
                                                    gamesInfos.add(games);
                                                }
                                            }

                                            RecyclerView recyclerView =  dialog.findViewById(R.id.rvgamesList);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getOwnerActivity()));

                                            GamesAdapter gamesAdapter = new GamesAdapter(context, id);
                                            gamesAdapter.setGamesInfoArrayList(gamesInfos);

                                            recyclerView.setAdapter(gamesAdapter);


                                            dialog.show();
                                        }
                                    });
                        }

                    }
                });
    }
}
