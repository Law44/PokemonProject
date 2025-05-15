package com.example.pokemonproject.view;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Pokemon;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListaFragment extends Fragment implements GameActivity.QueryChangeListener {

    private FirestorePagingOptions<Pokemon> options;
    private RecyclerView recyclerView;
    private FirestorePagingAdapter<Pokemon, PokemonViewHolder> adapter;

    CollectionReference productsRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View mView = inflater.inflate(R.layout.fragment_pokemons, container, false);


        recyclerView = mView.findViewById(R.id.rvListaPokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        productsRef = rootRef.collection("ListaPokemon");

        recyclerView = loadList(recyclerView, productsRef);
        return mView;

    }
    public void onQueryChange(String newText) {
        if(newText.isEmpty()){
                    recyclerView = loadList(recyclerView, productsRef);
        }else {
            String namePokemon = newText.substring(0, 1).toUpperCase() + newText.substring(1).toLowerCase();
            Query query = productsRef.whereEqualTo("name",namePokemon );
            Log.e("name", newText);
            final PagingConfig config = new PagingConfig(151,151,true);

                    /*new PagingConfig.Builder()
                    .setEnablePlaceholders(true)
                    .setPrefetchDistance(151)
                    .setPageSize(151)
                    .build();*/
            options = new FirestorePagingOptions.Builder<Pokemon>()
                    .setLifecycleOwner(getViewLifecycleOwner())
                    .setQuery(query, config, Pokemon.class)
                    .build();

            adapter = new FirestorePagingAdapter<Pokemon, PokemonViewHolder>(options) {
                @NonNull
                @Override
                public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pokemon, viewGroup, false);
                    return new PokemonViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull PokemonViewHolder holder, int position, @NonNull Pokemon model) {
                    holder.setPokemon(model);
                }
            };
            recyclerView.setAdapter(adapter);
        }

    }


    private RecyclerView loadList(RecyclerView recyclerView, CollectionReference productsRef) {
        Query query = productsRef.orderBy("id");
        final PagingConfig config = new PagingConfig(151,151,true);
        options = new FirestorePagingOptions.Builder<Pokemon>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(query, config, Pokemon.class)
                .build();

        adapter = new FirestorePagingAdapter<Pokemon,PokemonViewHolder>(options) {
            @NonNull
            @Override
            public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pokemon, viewGroup, false);
                return new PokemonViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PokemonViewHolder holder, int position, @NonNull Pokemon model) {
                holder.setPokemon(model);
            }
        };
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }



}
