package com.example.pokemonproject.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Combate;
import com.example.pokemonproject.model.Partida;
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

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class JornadaFragment extends Fragment implements GameActivity.QueryChangeListener {
    private String idPartida ="";
    List<Combate> combateList;
    View mView;
    FirebaseFirestore db;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_jornada, container, false);
        db = FirebaseFirestore.getInstance();
        combateList = new ArrayList<>();

        db.collection("Partidas").document(idPartida).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                if (e != null){
                    return;
                }



                    Partida partida = documentSnapshot.toObject(Partida.class);

                    for (int i =0;i<partida.getUsers().size(); i++) {
                        if (partida.getUsers().get(i).getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            if (partida.getUsers().get(i).getCombatesID().size() > 0) {
                                final String combateId = partida.getUsers().get(i).getCombatesID().get(partida.getUsers().get(i).getCombatesID().size() - 1);
                                db.collection("Combates").document(combateId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                                        Combate combateSacarJornada = documentSnapshot.toObject(Combate.class);
                                        if (combateSacarJornada==null)return;
                                            final int jornadaActual = combateSacarJornada.getJornada();
                                            db.collection("Combates").whereEqualTo("idGame", idPartida).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                                                    if (e != null){
                                                        return;
                                                    }

                                                    combateList = new ArrayList<>();

                                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                            Combate combate = snapshot.toObject(Combate.class);
                                                            if (combate.getJornada() == jornadaActual) {
                                                                combateList.add(combate);
                                                            }
                                                        }
                                                        Log.e("Combates", "" + combateList.size());
                                                        CombatesAdapter combatesAdapter = new CombatesAdapter(combateList);
                                                        recyclerView = mView.findViewById(R.id.rvCombates);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                        DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
                                                        recyclerView.addItemDecoration(itemDecor);
                                                        recyclerView.setAdapter(combatesAdapter);


                                                }
                                            });

                                    }
                                });

                            }
                        }
                    }
            }
        });


        return mView;
    }

    public void setIdLastPartida(String id) {
        this.idPartida = id;
    }

    @Override
    public void onQueryChange(String query) {

    }
}
