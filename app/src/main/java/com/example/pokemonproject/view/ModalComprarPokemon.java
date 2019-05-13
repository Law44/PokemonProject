package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Pokemon;

class ModalComprarPokemon {
    private Pokemon pokemon;
    private MercadoFragment fragment;
    private Context context;
    public ModalComprarPokemon(Context context, Pokemon model, MercadoFragment mercadoFragment) {
        this.context = context;
        this.pokemon = model;
        this.fragment = mercadoFragment;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_inventario);
        GlideApp.with(context).load(model.getSprites().back_shiny).into((ImageView) dialog.findViewById(R.id.imgPokemonModal));
        dialog.show();
    }
}
