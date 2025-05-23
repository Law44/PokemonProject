package com.example.pokemonproject.view;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.MovementFirebase;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.PiedrasUser;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public  class HomeFragment extends Fragment implements GameActivity.QueryChangeListener {

    private GameActivity gameActivity;
    private String teamID, alineationID, piedrasID;
    private String idGame;
    private Team team;
    private Alineation alineation;
    private int numbergames;

    private ArrayList<String> listGames;
    private ArrayList<List<Moves>> movimientos;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View mView;



    public HomeFragment() {
    }

    public void setInfo(GameActivity gameActivity, String id, int numbergames, ArrayList<String> listGames){
        this.gameActivity = gameActivity;
        this.idGame = id;
        this.numbergames = numbergames;
        this.listGames = listGames;
        this.movimientos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);

// To dismiss the dialog

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
                                piedrasID = listUsers.get(j).getObjetosID();
                            }
                        }

                        db.collection("Equipos")
                                .document(teamID)
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot1, @Nullable FirebaseFirestoreException e) {
                                        if (e != null){

                                            return;
                                        }
                                        team = documentSnapshot1.toObject(Team.class);

                                        for (int i = 0; i < team.getEquipo().size(); i++) {
                                            final int finalI = i;
                                            for (int j = 0; j < team.getEquipo().get(i).getMoves().size(); j++) {
                                                final int finalJ = j;
                                                db.collection("Movimientos").whereEqualTo("id",team.getEquipo().get(i).getMoves().get(j).move.id).get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            for (QueryDocumentSnapshot snapshot: task.getResult()){
                                                                MovementFirebase movementFirebase = snapshot.toObject(MovementFirebase.class);

                                                                switch (finalJ){
                                                                    case 0:
                                                                        team.getEquipo().get(finalI).getMoves().get(finalJ).move.name = movementFirebase.name;
                                                                        break;
                                                                    case 1:
                                                                        team.getEquipo().get(finalI).getMoves().get(finalJ).move.name = movementFirebase.name;
                                                                        break;
                                                                    case 2:
                                                                        team.getEquipo().get(finalI).getMoves().get(finalJ).move.name = movementFirebase.name;
                                                                        break;
                                                                    case 3:
                                                                        team.getEquipo().get(finalI).getMoves().get(finalJ).move.name = movementFirebase.name;
                                                                        break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                            movimientos.add(team.getEquipo().get(finalI).getMoves());
                                        }

                                        db.collection("PiedrasUser").document(piedrasID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    PiedrasUser piedrasUser = task.getResult().toObject(PiedrasUser.class);

                                                    RecyclerView recyclerView =  mView.findViewById(R.id.rvteamList);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                    DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
                                                    recyclerView.addItemDecoration(itemDecor);

                                                    TeamAdapter teamAdapter = new TeamAdapter(gameActivity, piedrasUser.getPiedras(), teamID, alineationID, piedrasID);
                                                    teamAdapter.setGamesInfoArrayList(team.getEquipo());

                                                    recyclerView.setAdapter(teamAdapter);
                                                }
                                            }
                                        });



                                    }
                                });

                        db.collection("Alineaciones").document(alineationID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    if (e != null){
                                        return;
                                    }
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
                        });
                    }
                });



        mView.findViewById(R.id.imgCampoBatalla).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 0, numbergames, idGame, listGames, movimientos);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 1, numbergames, idGame, listGames, movimientos);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 2, numbergames, idGame, listGames, movimientos);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 3, numbergames, idGame, listGames, movimientos);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 4, numbergames, idGame, listGames, movimientos);
                }

            }
        });

        mView.findViewById(R.id.imgCampoBatalla6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (team != null) {
                    new ModalSelectTeam(gameActivity, team.getEquipo(), alineationID, 5, numbergames, idGame, listGames, movimientos);
                }

            }
        });

        return mView;
    }

    @Override
    public void onQueryChange(String query) {

    }
}
