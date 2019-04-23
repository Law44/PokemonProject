package com.example.pokemonproject.view;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Pokemon;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class ListaFragment extends Fragment {
    private int limit = 15;
    private MaterialSearchView searchView;
    private Query query;
    private FirestorePagingOptions<Pokemon> options;
    private FirestorePagingAdapter<Pokemon, PokemonViewHolder> adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_pokemons, container, false);

        searchView =  mView.findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);

        Button search = mView.findViewById(R.id.action_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.showSearch();
            }
        });

        final RecyclerView recyclerView = mView.findViewById(R.id.rvListaPokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference productsRef = rootRef.collection("ListaPokemon");

        loadList(recyclerView, productsRef);


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    loadList(recyclerView,productsRef);
                }else {
                    query = productsRef.whereEqualTo("name", newText.toLowerCase()).limit(limit);
                    Log.e("name", newText);
                    final PagedList.Config config = new PagedList.Config.Builder()
                            .setEnablePlaceholders(true)
                            .setPrefetchDistance(15)
                            .setPageSize(15)
                            .build();
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
                            Log.e("pokemon", model.getName());
                        }
                    };
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });

        return mView;
    }

    private void loadList(RecyclerView recyclerView, CollectionReference productsRef) {
        query = productsRef.orderBy("id").limit(limit);
        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(15)
                .setPageSize(15)
                .build();
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
                Log.e("pokemon",model.getName());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public  class PokemonViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PokemonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setPokemon(Pokemon pokemon) {
            TextView textView = view.findViewById(R.id.tvPokemonNameList);
            textView.setText(pokemon.getName());

            textView = view.findViewById(R.id.tvPokemonIdList);
            textView.setText(String.valueOf(pokemon.getId()));

            GlideApp.with(view)
                    .load(pokemon.getSprites().front_default)
                    .circleCrop()
                    .into((ImageView) view.findViewById(R.id.imgPokemonImageList));
        }
    }

}
