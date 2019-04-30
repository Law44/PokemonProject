package com.example.pokemonproject.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.UserGame;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NewLeagueActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etGameName, etInitialMoney, etTeamName;

    Username creator;
    String idUser;
    String games;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_league);

        etGameName = findViewById(R.id.etGameName);
        etInitialMoney = findViewById(R.id.etInitialMoney);
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
                            }
                        }
                    }
                });

        findViewById(R.id.crear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                games = String.valueOf(Integer.parseInt(games)+1);

                db.collection("Users")
                        .document(idUser)
                        .update("games", games);

                creator.setGames(games);

                ArrayList<UserGame> usergames = new ArrayList<UserGame>();
                usergames.add(new UserGame(creator, etTeamName.getText().toString()));
                String id = db.collection("Partidas").document().getId();
                Partida partida = new Partida(id, etGameName.getText().toString(), Float.parseFloat(String.valueOf(etInitialMoney.getText().toString())), usergames);
                db.collection("Partidas").document(id).set(partida);
                finish();
            }
        });

    }
}
