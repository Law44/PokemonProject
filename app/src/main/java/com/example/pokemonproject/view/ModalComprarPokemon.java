package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
        GlideApp.with(context).load(model.getSprites().front_default).into((ImageView) dialog.findViewById(R.id.imgPokemonModal));
        GlideApp.with(context).load(R.drawable.boxpokemonmodal).centerCrop().into((ImageView) dialog.findViewById(R.id.imgModalBoxFondo));

        if (model.getTypes().size()==2){
            int id = context.getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", context.getPackageName());
            GlideApp.with(context).load(id).into((ImageView) dialog.findViewById(R.id.imgTipo1Modal));
            int id2 = context.getResources().getIdentifier(model.getTypes().get(1).getType().getName(), "drawable", context.getPackageName());
            GlideApp.with(context).load(id2).into((ImageView) dialog.findViewById(R.id.imgTipo2Modal));
        }else {
            int id = context.getResources().getIdentifier(model.getTypes().get(0).getType().getName(), "drawable", context.getPackageName());
            GlideApp.with(context).load(id).into((ImageView) dialog.findViewById(R.id.imgTipoUnicoModal));
        }
        Button btnClose = dialog.findViewById(R.id.btnCloseModal);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText etCoste = dialog.findViewById(R.id.etCostePokemon);
        etCoste.setText(String.valueOf(model.getPrice()));
        final Button btnAdd = dialog.findViewById(R.id.btnAddModal);
        Button btnMin = dialog.findViewById(R.id.btnMinusModal);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCoste.setText(String.valueOf(Integer.parseInt(etCoste.getText().toString())+10));
            }
        });
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCoste.setText(String.valueOf(Integer.parseInt(etCoste.getText().toString())-10));
            }
        });
        btnAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });
        dialog.show();

    }
}
