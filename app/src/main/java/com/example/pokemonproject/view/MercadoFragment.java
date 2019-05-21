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
import com.example.pokemonproject.model.Pokemon;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class MercadoFragment extends Fragment {
    private Query query;
    private FirestorePagingOptions<Pokemon> options;
    private RecyclerView recyclerView;
    private FirestorePagingAdapter<Pokemon, MercadoFragment.PokemonViewHolder> adapter;
    String lastgame;

    @SuppressLint("ValidFragment")
    public MercadoFragment(String id) {
        lastgame = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_mercado, container, false);


        recyclerView = mView.findViewById(R.id.rvMercadoPokemon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference productsRef = rootRef.collection("ListaPokemon");
        query = productsRef.orderBy("id", Query.Direction.DESCENDING).limit(10).endAt(10);
        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .setPageSize(10)
                .build();
        options = new FirestorePagingOptions.Builder<Pokemon>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(query, config, Pokemon.class)
                .build();

        adapter = new FirestorePagingAdapter<Pokemon, MercadoFragment.PokemonViewHolder>(options) {
            @NonNull
            @Override
            public MercadoFragment.PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pokemon_mercado, viewGroup, false);
                return new MercadoFragment.PokemonViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MercadoFragment.PokemonViewHolder holder, int position, @NonNull Pokemon model) {
                holder.setPokemon(model,position);

            }
        };
        recyclerView.setAdapter(adapter);


        return mView;
    }
    public  class PokemonViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PokemonViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }



        public void setPokemon(final Pokemon model, final int position) {
            TextView tvName = view.findViewById(R.id.tvPokemonNameMercado);
            TextView tvId = view.findViewById(R.id.tvPokemonPokedexMercado);
            TextView tvCostePokemon = view.findViewById(R.id.tvPokemonCosteMercado);
            ImageView tipo1 = view.findViewById(R.id.imgPokemonTipo1Mercado);
            ImageView tipo2 = view.findViewById(R.id.imgPokemonTipo2Mercado);
            ImageView tipounico = view.findViewById(R.id.imgPokemonTipoUnicoMercado);
            ImageView button = view.findViewById(R.id.imgButtonPagar);


            tvName.setText(model.getName());
            tvId.setText(String.valueOf(model.getId()));
            tvCostePokemon.setText(String.valueOf(model.getPrice()));
            if (model.getTypes().size() == 2){
                int id = tipo1.getContext().getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", tipo1.getContext().getPackageName());
                GlideApp.with(getActivity()).load(id).into(tipo1);
                int id2 = tipo1.getContext().getResources().getIdentifier(model.getTypes().get(1).getType().getName(), "drawable", tipo2.getContext().getPackageName());
                GlideApp.with(getActivity()).load(id2).into(tipo2);

                GlideApp.with(getActivity()).load(0).into(tipounico);
            }else {
                int id = tipo1.getContext().getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", tipounico.getContext().getPackageName());
                GlideApp.with(getActivity()).load(id).into(tipounico);
                GlideApp.with(getActivity()).load(0).into(tipo1);
                GlideApp.with(getActivity()).load(0).into(tipo2);
            }

            GlideApp.with(view)
                    .load(model.getSprites().front_default)
                    .circleCrop()
                    .into((ImageView) view.findViewById(R.id.imgPokemonMercado));
            GlideApp.with(view)
                    .load(R.drawable.icons8_pokeball_80)
                    .circleCrop()
                    .into((ImageView)view.findViewById(R.id.imgButtonPagar));

            GlideApp.with(view)
                    .load(R.drawable.pokemondollar)
                    .into((ImageView) view.findViewById(R.id.imgPokedolar));


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ModalComprarPokemon(view.getContext(), model, MercadoFragment.this, lastgame, position);
                }
            });
        }
    }
}
