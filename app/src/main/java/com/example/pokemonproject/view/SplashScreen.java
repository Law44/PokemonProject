package com.example.pokemonproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    int games;
    String lastGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        if (firebaseUser!= null) {
            db.collection("Users")
                    .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    games = Integer.parseInt(document.get("games").toString());
                                    lastGame = document.get("lastGame").toString();
                                }
                            }
                        }
                    });
        }
        handler=new Handler();

        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (firebaseUser != null){
                        Intent intent = new Intent(SplashScreen.this, GameActivity.class);
                        intent.putExtra("games", games);
                        intent.putExtra("lastGame", lastGame);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }

                }
            },3000);

        GlideApp.with(this)
                .load(R.drawable.load)
                .into((ImageView) findViewById(R.id.loadGif));


    }
}
