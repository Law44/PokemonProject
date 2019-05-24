package com.example.pokemonproject.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public  class HomeFragment extends Fragment {

    private GameActivity gameActivity;
    private String teamID, alineationID;
    private String idGame;
    private Team team;
    private Alineation alineation;
    private int numbergames;
    private String idLastGame;
    private ArrayList<String> listGames;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View mView;


    public HomeFragment(){

    }

    @SuppressLint("ValidFragment")
    public HomeFragment(GameActivity gameActivity, String id, int numbergames, String idLastGame, ArrayList<String> listGames) {
        this.gameActivity = gameActivity;
        this.idGame = id;
        this.numbergames = numbergames;
        this.idLastGame = idLastGame;
        this.listGames = listGames;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .circleCrop()
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla2));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla3));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla4));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla5));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla6));

        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta2));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta3));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta4));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta5));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta6));



        db.collection("Partidas")
                .document(idGame)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Partida partida = documentSnapshot.toObject(Partida.class);
                        ArrayList<UserGame> listUsers = partida.getUsers();
                        for (int j = 0; j < listUsers.size(); j++) {
                            if (listUsers.get(j).getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                teamID = listUsers.get(j).getTeamID();
                                alineationID = listUsers.get(j).getAlineationID();
                            }
                        }

                        db.collection("Equipos")
                                .document(teamID)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                        team = documentSnapshot1.toObject(Team.class);

                                        RecyclerView recyclerView =  mView.findViewById(R.id.rvteamList);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
                                        recyclerView.addItemDecoration(itemDecor);

                                        TeamAdapter teamAdapter = new TeamAdapter(gameActivity);
                                        teamAdapter.setGamesInfoArrayList(team.getEquipo());

                                        recyclerView.setAdapter(teamAdapter);

                                    }
                                });

                        db.collection("Alineaciones").document(alineationID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    alineation = documentSnapshot.toObject(Alineation.class);
                                    if (alineation.getLista() != null) {
                                        if (alineation.getLista().get(0) != null) {
                                            GlideApp.with(gameActivity)
                                                    .load(alineation.getLista().get(0).getSprites().front_default)
                                                    .into((ImageView) mView.findViewById(R.id.imgSilueta));
                                        }
                                        if (alineation.getLista().get(1) != null) {
                                            GlideApp.with(gameActivity)
                                                    .load(alineation.getLista().get(1).getSprites().front_default)
                                                    .into((ImageView) mView.findViewById(R.id.imgSilueta2));
                                        }
                                        if (alineation.getLista().get(2) != null) {
                                            GlideApp.with(gameActivity)
                                                    .load(alineation.getLista().get(2).getSprites().front_default)
                                                    .into((ImageView) mView.findViewById(R.id.imgSilueta3));
                                        }
                                        if (alineation.getLista().get(3) != null) {
                                            GlideApp.with(gameActivity)
                                                    .load(alineation.getLista().get(3).getSprites().front_default)
                                                    .into((ImageView) mView.findViewById(R.id.imgSilueta4));
                                        }
                                        if (alineation.getLista().get(4) != null) {
                                            GlideApp.with(gameActivity)
                                                    .load(alineation.getLista().get(4).getSprites().front_default)
                                                    .into((ImageView) mView.findViewById(R.id.imgSilueta5));
                                        }
                                        if (alineation.getLista().get(5) != null) {
                                            GlideApp.with(gameActivity)
                                                    .load(alineation.getLista().get(5).getSprites().front_default)
                                                    .into((ImageView) mView.findViewById(R.id.imgSilueta6));
                                        }
                                    }
                                }
                            }
                        });
                    }
                });



        mView.findViewById(R.id.imgCampoBatalla).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 0, numbergames, idLastGame, listGames);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 1, numbergames, idLastGame, listGames);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 2, numbergames, idLastGame, listGames);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 3, numbergames, idLastGame, listGames);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 4, numbergames, idLastGame, listGames);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 5, numbergames, idLastGame, listGames);
                }

            }
        });

        return mView;
    }
}
