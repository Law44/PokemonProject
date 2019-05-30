package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ModalEvolution {
    private Pokemon pokemon;
    private String idGame;
    private Context context;
    private MercadoFragment fragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<UserGame> listUsers;

    public ModalEvolution(final Context context, final Pokemon model) {
        this.context = context;
        this.pokemon = model;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_evolution);
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

        TextView piedrasNecesarias = dialog.findViewById(R.id.piedrasNecesarias);

        TextView piedrasPosesion = dialog.findViewById(R.id.piedrasPosesion);

        Button btnEvolucionar = dialog.findViewById(R.id.btnEvolucionar);


        if (model.getPiedrasEvo().getCantidad() > 0) {
            piedrasNecesarias.setText(model.getPiedrasEvo().getCantidad() + " Piedras necesarias para evolucionar");
            piedrasPosesion.setText("Tienes " + 3 + " piedras");
        }
        else {
            piedrasNecesarias.setText("Este pokemon no puede evolucionar");
            btnEvolucionar.setVisibility(View.INVISIBLE);
            btnEvolucionar.setEnabled(false);
        }

        btnEvolucionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();

    }
}
