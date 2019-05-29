package com.example.pokemonproject.view;

import android.annotation.SuppressLint;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.ListaPujas;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.Team;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class MercadoFragment extends Fragment {
    private Query query;
    private FirestorePagingOptions<Pokemon> options;
    private RecyclerView recyclerView;
    String lastgame, teamID, pujasID;
    GameActivity context;
    Team team;
    ArrayList<Pokemon> listaPokemon;
    ArrayList<Integer> pujas;
    Map<Integer, Integer> totalPujas;
    int money;
    public static int saldofuturo;

    public MercadoFragment() {
    }

    public void setInfo(GameActivity context, String id){
        this.totalPujas = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            totalPujas.put(i,0);
        }
        lastgame = id;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_mercado, container, false);


        recyclerView = mView.findViewById(R.id.rvMercadoPokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        query = rootRef.collection("Mercado").whereEqualTo("id", lastgame);
        rootRef.collection("Mercado")
                .document(lastgame)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().toObject(ListaPujas.class) != null) {
                                ListaPujas listaPujas = task.getResult().toObject(ListaPujas.class);
                                listaPokemon = listaPujas.getLista();

                                rootRef.collection("Partidas").document(lastgame).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            Partida partida = task.getResult().toObject(Partida.class);
                                            for (int i = 0; i < partida.getUsers().size(); i++) {
                                                if (partida.getUsers().get(i).getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                                    teamID = partida.getUsers().get(i).getTeamID();
                                                    pujasID = partida.getUsers().get(i).getPujasID();
                                                    money = partida.getUsers().get(i).getMoney();
                                                    saldofuturo = money;
                                                }
                                            }

                                            rootRef.collection("Pujas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @SuppressLint("NewApi")
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if (document.getId().equals(pujasID)){
                                                                pujas = (ArrayList<Integer>) document.get("pujas");
                                                                for (int i = 0; i < pujas.size(); i++) {
                                                                    saldofuturo -= Integer.parseInt(String.valueOf(pujas.get(i)));
                                                                }
                                                            }
                                                            ArrayList<Integer> pujastemp = (ArrayList<Integer>) document.get("pujas");
                                                            for (int i = 0; i < totalPujas.size(); i++) {
                                                                totalPujas.put(i, 0);

                                                            }

                                                            for (int i = 0; i < pujastemp.size(); i++) {
                                                                if (Integer.parseInt(String.valueOf(pujastemp.get(i))) > 0){
                                                                    totalPujas.put(i, Integer.parseInt(String.valueOf(totalPujas.get(i)))+1);
                                                                }
                                                            }
                                                        }
                                                        rootRef.collection("Equipos").document(teamID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    team = task.getResult().toObject(Team.class);

                                                                    PujasAdapter pujasAdapter = new PujasAdapter(context, lastgame, MercadoFragment.this, team, totalPujas, pujas);
                                                                    pujasAdapter.setPokemonPujas(listaPokemon);

                                                                    recyclerView.setAdapter(pujasAdapter);
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
                        }
                    }
                });

        return mView;
    }
}
