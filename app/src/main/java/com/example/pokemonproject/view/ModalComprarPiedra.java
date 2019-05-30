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
import com.example.pokemonproject.model.PujasPiedras;
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

    public ModalComprarPiedra(final Context context, final PiedrasEvoUser model, final MercadoFragment mercadoFragment, String idBuyGame, final int position, final Map<Integer, Integer> totalPujas, final ArrayList<Integer> pujaspropias, final View view) {
        this.context = context;
        this.piedrasEvoUser = model;
        this.fragment = mercadoFragment;
        this.idGame = idBuyGame;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_compra_piedra);
        GlideApp.with(context).load(R.drawable.boxpokemonmodal).centerCrop().into((ImageView) dialog.findViewById(R.id.imgModalBoxFondo));

        TextView tipoPiedra = dialog.findViewById(R.id.tipoPiedra);
        if (model.getCantidad() == 1) {
            tipoPiedra.setText(model.getCantidad() + " Piedra " + model.getName());
        }
        else if (model.getCantidad() > 1){
            tipoPiedra.setText(model.getCantidad() + " Piedras " + model.getName());
        }

        Button btnClose = dialog.findViewById(R.id.btnCloseModal);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText etCoste = dialog.findViewById(R.id.etCostePiedra);
        etCoste.setText(String.valueOf(model.getPrecio() * model.getCantidad()));
        if (Integer.parseInt(String.valueOf(pujaspropias.get(position))) > 0) {
            etCoste.setText(String.valueOf(pujaspropias.get(position)));
        }

        TextView tvpujasActivas = dialog.findViewById(R.id.pujasActivas);
        if (Integer.parseInt(String.valueOf(totalPujas.get(position))) > 0){
            if (Integer.parseInt(String.valueOf(totalPujas.get(position))) == 1){
                tvpujasActivas.setText(totalPujas.get(position) + " puja");
            }
            else {
                tvpujasActivas.setText(totalPujas.get(position) + " pujas");
            }
        }

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
                if (Integer.parseInt(etCoste.getText().toString()) > model.getPrecio()){
                    etCoste.setText(String.valueOf(Integer.parseInt(etCoste.getText().toString())-10));
                }
                else {
                    Toast.makeText(context, "La puja no puede ser mas baja que el precio original", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });

        Button btnPujarModal = dialog.findViewById(R.id.btnPujarModal);
        btnPujarModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (Integer.parseInt(etCoste.getText().toString()) >= model.getPrecio()) {
                    if (Integer.parseInt(etCoste.getText().toString()) > MercadoFragment.saldofuturo) {
                        Toast.makeText(context, "No puedes pujar mas de tu saldo actual o tu futuro saldo", Toast.LENGTH_LONG).show();
                    }
                    else {
                        db.collection("Partidas")
                                .document(idGame)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                        Partida partida = documentSnapshot.toObject(Partida.class);
                                        listUsers = partida.getUsers();
                                        for (int j = 0; j < listUsers.size(); j++) {
                                            if (listUsers.get(j).getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                final String pujasPiedras = listUsers.get(j).getPujasPiedras();
                                                db.collection("PujasPiedras")
                                                        .document(pujasPiedras)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Integer.parseInt(String.valueOf(pujaspropias.get(position))) == 0) {
                                                                    String kk = "" + totalPujas.get(position);
                                                                    Integer integer = Integer.valueOf(kk);
                                                                    integer += 1;
                                                                    totalPujas.put(position, integer);

                                                                }
                                                                DocumentSnapshot documentSnapshot1 = task.getResult();
                                                                PujasPiedras pujas = documentSnapshot1.toObject(PujasPiedras.class);
                                                                final ArrayList<Integer> pujastemp = pujas.getPujas();
                                                                pujastemp.set(position, Integer.parseInt(etCoste.getText().toString()));
                                                                pujaspropias.set(position, Integer.parseInt(etCoste.getText().toString()));
                                                                view.findViewById(R.id.imgFondo).setBackgroundColor(view.getResources().getColor(R.color.colorBuy));



                                                                MercadoFragment.saldofuturo-= Integer.parseInt(etCoste.getText().toString());
                                                                TextView futuro = mercadoFragment.getView().findViewById(R.id.tvMoneyFuturaMercado);
                                                                futuro.setText(String.valueOf(MercadoFragment.saldofuturo));
                                                                db.collection("PujasPiedras").document(pujasPiedras).update("pujas", pujastemp);
                                                                dialog.dismiss();



                                                            }
                                                        });

                                            }
                                        }

                                    }
                                });
                    }
                }
                else {
                    Toast.makeText(context, "La puja no puede ser mas baja que el precio original", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();

    }
}
