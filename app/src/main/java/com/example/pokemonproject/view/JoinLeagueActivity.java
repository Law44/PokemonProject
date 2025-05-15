package com.example.pokemonproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.PiedrasUser;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.PujasPiedras;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class JoinLeagueActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText idGame, etTeamName;

    Username creator;
    Partida users;
    String idUser, teamID, pujasID, alineationID, pujasPiedrasID, piedrasID;
    String games;
    String lastGame;
    ArrayList<String> listGame;
    boolean usuarioPresente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_league);

        usuarioPresente = false;

        idGame = findViewById(R.id.idGame);
        etTeamName = findViewById(R.id.etTeamName);

        db.collection("Users")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idUser = document.getId();
                                games = document.get("games").toString();
                                creator = document.toObject(Username.class);
                                lastGame = document.get("lastGame").toString();
                                listGame = creator.getListGames();
                            }
                        }
                    }
                });


        findViewById(R.id.unir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(idGame.getText().toString()) || TextUtils.isEmpty(etTeamName.getText().toString())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(JoinLeagueActivity.this).create();
                    alertDialog.setMessage("No puede haber campos vacios");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {


                    db.collection("Partidas")
                            .document(idGame.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        users = document.toObject(Partida.class);
                                        for (int i = 0; i < users.getUsers().size(); i++) {
                                            if (users.getUsers().get(i).getUser().getEmail().equals(creator.getEmail())) {
                                                usuarioPresente = true;
                                            }
                                        }

                                        if (usuarioPresente) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(JoinLeagueActivity.this).create();
                                            alertDialog.setMessage("Ya estas en esta liga!");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                        else if (users.getUsers().size() == 8) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(JoinLeagueActivity.this).create();
                                            alertDialog.setMessage("Esta liga ya esta llena!");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                        else {
                                            games = String.valueOf(Integer.parseInt(games) + 1);
                                            lastGame = idGame.getText().toString();
                                            listGame.add(lastGame);

                                            db.collection("Users")
                                                    .document(idUser)
                                                    .update("games", games, "lastGame", lastGame, "listGames", listGame);

                                            creator.setGames(games);
                                            teamID = db.collection("Equipos").document().getId();
                                            pujasID = db.collection("Pujas").document().getId();
                                            alineationID = db.collection("Alineaciones").document().getId();
                                            pujasPiedrasID = db.collection("PujasPiedras").document().getId();
                                            piedrasID = db.collection("PiedrasUser").document().getId();
                                            PiedrasUser piedrasUser = new PiedrasUser();
                                            db.collection("PiedrasUser").document(piedrasID).set(piedrasUser);
                                            PujasPiedras pujasPiedras = new PujasPiedras();
                                            db.collection("PujasPiedras").document(pujasPiedrasID).set(pujasPiedras);
                                            Alineation alineation = new Alineation();
                                            db.collection("Alineaciones").document(alineationID).set(alineation);
                                            Pujas pujas = new Pujas();
                                            db.collection("Pujas").document(pujasID).set(pujas);
                                            UserGame userGame = new UserGame(creator, etTeamName.getText().toString(), 0, teamID, users.getInitialMoney(), pujasID, alineationID, pujasPiedrasID, piedrasID);
                                            Team equipo = new Team();
                                            db.collection("Equipos").document(teamID).set(equipo);

                                            users.getUsers().add(userGame);
                                            db.collection("Partidas")
                                                    .document(idGame.getText().toString())
                                                    .update("users", users.getUsers());

                                            finish();
                                            Intent intent = new Intent(JoinLeagueActivity.this, GameActivity.class);
                                            intent.putExtra("games", Integer.parseInt(games));
                                            intent.putExtra("lastGame", lastGame);
                                            intent.putExtra("listGames", listGame);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                }

            }
        });

    }
}
