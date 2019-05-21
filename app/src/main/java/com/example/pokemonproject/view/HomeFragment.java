package com.example.pokemonproject.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public  class HomeFragment extends Fragment {

    private GameActivity gameActivity;
    private String teamID;
    private String idGame;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View mView;


    public HomeFragment(){

    }

    @SuppressLint("ValidFragment")
    public HomeFragment(GameActivity gameActivity, String id) {
        this.gameActivity = gameActivity;
        this.idGame = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView campoBatalla = mView.findViewById(R.id.imgCampoBatalla);
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .circleCrop()
                .into(campoBatalla);
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
                            }
                        }

                        db.collection("Equipos")
                                .document(teamID)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                        Team team = documentSnapshot1.toObject(Team.class);

                                        RecyclerView recyclerView =  mView.findViewById(R.id.rvteamList);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                                        TeamAdapter teamAdapter = new TeamAdapter(gameActivity);
                                        teamAdapter.setGamesInfoArrayList(team.getEquipo());
                                    }
                                });

                    }
                });




        return mView;
    }
}
