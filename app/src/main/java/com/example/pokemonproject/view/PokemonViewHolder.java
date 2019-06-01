package com.example.pokemonproject.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Pokemon;

public  class PokemonViewHolder extends RecyclerView.ViewHolder {
    private View view;

    PokemonViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    void setPokemon(Pokemon pokemon) {
        TextView tvName = view.findViewById(R.id.tvPokemonNameList);
        TextView tvId = view.findViewById(R.id.tvPokemonIdList);
        TextView tvHp = view.findViewById(R.id.tvPokemonHpList);
        TextView tvSpeed = view.findViewById(R.id.tvPokemonSpeedList);
        TextView tvAtk = view.findViewById(R.id.tvPokemonAtkList);
        TextView tvDef = view.findViewById(R.id.tvPokemonDefList);
        TextView tvAtkSp = view.findViewById(R.id.tvPokemonAtkSpList);
        TextView tvDefSp = view.findViewById(R.id.tvPokemonDefSpList);
        ImageView tipo1 = view.findViewById(R.id.imgPokemonTipo1List);
        ImageView tipo2 = view.findViewById(R.id.imgPokemonTipo2List);
        ImageView tipounico = view.findViewById(R.id.imgPokemonTipoUnicoList);



        tvName.setText(pokemon.getName());
        tvId.setText(String.valueOf(pokemon.getId()));
        tvHp.setText(String.valueOf(pokemon.getStats().get(5).base_stat));
        tvSpeed.setText(String.valueOf(pokemon.getStats().get(0).base_stat));
        tvAtk.setText(String.valueOf(pokemon.getStats().get(4).base_stat));
        tvDef.setText(String.valueOf(pokemon.getStats().get(3).base_stat));
        tvAtkSp.setText(String.valueOf(pokemon.getStats().get(2).base_stat));
        tvDefSp.setText(String.valueOf(pokemon.getStats().get(1).base_stat));

        if (pokemon.getTypes().size() == 2){
            int id = tipo1.getContext().getResources().getIdentifier(pokemon.getTypes().get(0).getType().getName(), "drawable", tipo1.getContext().getPackageName());
            GlideApp.with(view.getContext()).load(id).into(tipo1);
            int id2 = tipo1.getContext().getResources().getIdentifier(pokemon.getTypes().get(1).getType().getName(), "drawable", tipo2.getContext().getPackageName());
            GlideApp.with(view.getContext()).load(id2).into(tipo2);

            GlideApp.with(view.getContext()).load(0).into(tipounico);
        }else {
            int id = tipo1.getContext().getResources().getIdentifier(pokemon.getTypes().get(0).getType().getName(), "drawable", tipounico.getContext().getPackageName());
            GlideApp.with(view.getContext()).load(id).into(tipounico);
            GlideApp.with(view.getContext()).load(0).into(tipo1);
            GlideApp.with(view.getContext()).load(0).into(tipo2);
        }
        GlideApp.with(view)
                .load(pokemon.getSprites().front_default)
                .circleCrop()
                .into((ImageView) view.findViewById(R.id.imgPokemonImageList));
    }
}