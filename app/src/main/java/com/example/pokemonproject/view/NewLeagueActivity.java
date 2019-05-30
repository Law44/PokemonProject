package com.example.pokemonproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.ListaPujas;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.PujasPiedras;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewLeagueActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etGameName, etTeamName;

    Username creator;
    String idUser, teamID, pujasID, alineationID, pujasPiedrasID;
    String games;
    String lastGame;
    ArrayList<String> listGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_league);

        etGameName = findViewById(R.id.etGameName);
        etTeamName = findViewById(R.id.etTeamName);

        String[] opcionesSpinner = new String[] {
                "2500", "5000", "7500", "10000", "15000" };
        final Spinner spinner = findViewById(R.id.etInitialMoney);
        ArrayAdapter<String> spinneroptions = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, opcionesSpinner);
        spinneroptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneroptions);

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

        findViewById(R.id.crear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etGameName.getText().toString()) || TextUtils.isEmpty(etTeamName.getText().toString())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(NewLeagueActivity.this).create();
                    alertDialog.setMessage("No puede haber campos vacios");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    games = String.valueOf(Integer.parseInt(games) + 1);

                ArrayList<UserGame> usergames = new ArrayList<>();
                teamID = db.collection("Equipos").document().getId();
                pujasID = db.collection("Pujas").document().getId();
                alineationID = db.collection("Alineaciones").document().getId();
                pujasPiedrasID = db.collection("PujasPiedras").document().getId();
                PujasPiedras pujasPiedras = new PujasPiedras();
                db.collection("PujasPiedras").document(pujasPiedrasID).set(pujasPiedras);
                Alineation alineation = new Alineation();
                db.collection("Alineaciones").document(alineationID).set(alineation);
                Pujas pujas = new Pujas();
                db.collection("Pujas").document(pujasID).set(pujas);
                usergames.add(new UserGame(creator, etTeamName.getText().toString(), 0, teamID, Integer.parseInt(spinner.getSelectedItem().toString()), pujasID, alineationID, pujasPiedrasID));
                Team equipo = new Team();
                db.collection("Equipos").document(teamID).set(equipo);
                String id = db.collection("Partidas").document().getId();
                lastGame = id;
                listGame.add(lastGame);


                db.collection("Users")
                        .document(idUser)
                        .update("games", games, "lastGame", lastGame, "listGames", listGame);

                creator.setGames(games);
                creator.setListGames(listGame);

                Partida partida = new Partida(id, etGameName.getText().toString(), Integer.parseInt(String.valueOf(spinner.getSelectedItem().toString())), usergames);
                db.collection("Partidas").document(id).set(partida);

                finish();
                Intent intent = new Intent(NewLeagueActivity.this, GameActivity.class);
                intent.putExtra("games", Integer.parseInt(games));
                intent.putExtra("lastGame", lastGame);
                intent.putExtra("listGames", listGame);
                startActivity(intent);
            }

            }
        });

    }
}
