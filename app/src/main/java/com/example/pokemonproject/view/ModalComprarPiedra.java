package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.example.pokemonproject.model.PiedrasEvoUser;
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

class ModalComprarPiedra {
    private PiedrasEvoUser piedrasEvoUser;
    private String idGame;
    private Context context;
    private MercadoFragment fragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<UserGame> listUsers;

    public ModalComprarPiedra(final Context context, final PiedrasEvoUser model, MercadoFragment mercadoFragment, String idBuyGame, final int position, final Map<Integer, Integer> totalPujas, final ArrayList<Integer> pujaspropias, final View view) {
        this.context = context;
        this.piedrasEvoUser = model;
        this.fragment = mercadoFragment;
        this.idGame = idBuyGame;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_compra);
        GlideApp.with(context).load(R.drawable.boxpokemonmodal).centerCrop().into((ImageView) dialog.findViewById(R.id.imgModalBoxFondo));



        Button btnClose = dialog.findViewById(R.id.btnCloseModal);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText etCoste = dialog.findViewById(R.id.etCostePokemon);
        etCoste.setText(String.valueOf(100));
        if (Integer.parseInt(String.valueOf(pujaspropias.get(position))) > 0) {
            etCoste.setText(String.valueOf(pujaspropias.get(position)));
        }

        dialog.show();

    }
}
