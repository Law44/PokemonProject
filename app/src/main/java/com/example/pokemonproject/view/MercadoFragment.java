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
import com.example.pokemonproject.model.Pokemon;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.VERTICAL;

@SuppressLint("ValidFragment")
public class MercadoFragment extends Fragment {
    private Query query;
    private FirestorePagingOptions<Pokemon> options;
    private RecyclerView recyclerView;
    String lastgame;
    GameActivity context;

    @SuppressLint("ValidFragment")
    public MercadoFragment(GameActivity context, String id) {
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
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
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
                                ArrayList<Pokemon> listaPokemon = listaPujas.getLista();

                                PujasAdapter pujasAdapter = new PujasAdapter(context, lastgame, MercadoFragment.this);
                                pujasAdapter.setPokemonPujas(listaPokemon);

                                recyclerView.setAdapter(pujasAdapter);
                            }
                        }
                    }
                });


        return mView;
    }
}
