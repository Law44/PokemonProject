package com.example.pokemonproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.pokemonproject.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseFirestore db;
    boolean user = true;
    int games;
    String lastgame;
    ArrayList<String> listGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();




        findViewById(R.id.sign_in).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    void comeIn(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
             db.collection("Users")
                    .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    games = Integer.parseInt(document.get("games").toString());
                                    lastgame = document.get("lastGame").toString();
                                    listGames = (ArrayList<String>) document.get("listGames");
                                }
                                if (!task.getResult().isEmpty()) {
                                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                    intent.putExtra("games", games);
                                    intent.putExtra("lastGame", lastgame);
                                    intent.putExtra("listGames", listGames);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                                    intent.putExtra("games", 0);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                    });
        }
    }

    void signIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                comeIn();
            }
        }
    }
}

