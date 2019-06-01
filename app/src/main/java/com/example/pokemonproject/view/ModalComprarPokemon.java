package com.example.pokemonproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.MovementFirebase;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ModalComprarPokemon {
    private Pokemon pokemon;
    private String idGame;
    private Context context;
    private MercadoFragment fragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<UserGame> listUsers;

    public ModalComprarPokemon(final Context context, final Pokemon model, final MercadoFragment mercadoFragment, String idBuyGame, final int position, final Team team, final Map<Integer, Integer> totalPujas, final ArrayList<Integer> pujaspropias, final View view, ArrayList<List<Moves>> movements) {
        this.context = context;
        this.pokemon = model;
        this.fragment = mercadoFragment;
        this.idGame = idBuyGame;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_compra);
        dialog.setCanceledOnTouchOutside(true);
        GlideApp.with(context).load(model.getSprites().front_default).into((ImageView) dialog.findViewById(R.id.imgPokemonModal));
        GlideApp.with(context).load(R.drawable.boxpokemonmodal).centerCrop().into((ImageView) dialog.findViewById(R.id.imgModalBoxFondo));
        final TextView mov1 = dialog.findViewById(R.id.comprarMov1);
        final TextView mov2 = dialog.findViewById(R.id.comprarMov2);
        final TextView mov3 = dialog.findViewById(R.id.comprarMov3);
        final TextView mov4 = dialog.findViewById(R.id.comprarMov4);

        if (movements.get(position).size() > 0) {
            mov1.setText(movements.get(position).get(0).move.name);
        }
        if (movements.get(position).size() > 1) {
            mov2.setText(movements.get(position).get(1).move.name);
        }
        if (movements.get(position).size() > 2) {
            mov3.setText(movements.get(position).get(2).move.name);
        }
        if (movements.get(position).size() > 3) {
            mov4.setText(movements.get(position).get(3).move.name);
        }

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
                if (Integer.parseInt(etCoste.getText().toString()) > pokemon.getPrice()){
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
            public void onClick(View v) {
                if (Integer.parseInt(etCoste.getText().toString()) >= pokemon.getPrice()) {
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
                                            final String pujasID = listUsers.get(j).getPujasID();
                                            db.collection("Pujas")
                                                    .document(pujasID)
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
                                                            Pujas pujas = documentSnapshot1.toObject(Pujas.class);
                                                            final ArrayList<Integer> pujastemp = pujas.getPujas();
                                                            pujastemp.set(position, Integer.parseInt(etCoste.getText().toString()));
                                                            pujaspropias.set(position, Integer.parseInt(etCoste.getText().toString()));
                                                            view.findViewById(R.id.imgFondo).setBackgroundColor(view.getResources().getColor(R.color.colorBuy));


                                                            boolean presente = false;
                                                            for (int i = 0; i < team.getEquipo().size(); i++) {
                                                                if (team.getEquipo().get(i).getId() == model.getId()) {
                                                                    presente = true;
                                                                }
                                                            }
                                                            if (presente) {
                                                                Toast.makeText(context, "Ya tienes a este pokemon!", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                MercadoFragment.saldofuturo-= Integer.parseInt(etCoste.getText().toString());
                                                                TextView futuro = mercadoFragment.getView().findViewById(R.id.tvMoneyFuturaMercado);
                                                                futuro.setText(String.valueOf(MercadoFragment.saldofuturo));
                                                                db.collection("Pujas").document(pujasID).update("pujas", pujastemp);
                                                                dialog.dismiss();
                                                            }


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
