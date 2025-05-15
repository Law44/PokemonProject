package com.example.pokemonproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Username;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnSignUp;
    FirebaseFirestore db;
    boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        db = FirebaseFirestore.getInstance();

        etUsername = findViewById(R.id.etUsername);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUsername.getText().toString())){
                    AlertDialog alertDialog = new AlertDialog.Builder(UserActivity.this).create();
                    alertDialog.setMessage("El nombre de usuario no puede estar vacio");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    ArrayList<String> listgame = new ArrayList<>();
                    Username username = new Username(etUsername.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), "0", "", listgame, FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                    db.collection("Users").add(username);
                    next = true;

                    Intent intent = new Intent(UserActivity.this, GameActivity.class);
                    intent.putExtra("lastGame", "");
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!next) {
            AuthUI.getInstance()
                    .signOut(UserActivity.this);
        }
    }
}
