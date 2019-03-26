package com.example.pokemonproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Username;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnSignUp;
    FirebaseFirestore db;

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
                    startActivity(new Intent(UserActivity.this, GameActivity.class));
                    Username username = new Username(etUsername.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), "true");
                    db.collection("Users").add(username);
                    finish();
                }
            }
        });


    }
}