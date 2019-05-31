package com.example.pokemonproject.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.PiedrasUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class InventarioActivity extends AppCompatActivity {

    String inventarioID;
    private RecyclerView recyclerViewObjetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Partidas").document(getIntent().getStringExtra("idGame")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    Partida partida = documentSnapshot.toObject(Partida.class);
                    for (int i = 0; i < partida.getUsers().size(); i++) {
                        if (partida.getUsers().get(i).getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            inventarioID = partida.getUsers().get(i).getObjetosID();
                            break;
                        }
                    }

                    db.collection("PiedrasUser").document(inventarioID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null){
                                return;
                            }

                            if (documentSnapshot.toObject(PiedrasUser.class) != null){
                                PiedrasUser piedrasUser = documentSnapshot.toObject(PiedrasUser.class);

                                recyclerViewObjetos = findViewById(R.id.rvInventario);
                                recyclerViewObjetos.setLayoutManager(new LinearLayoutManager(InventarioActivity.this));

                                InventarioAdapterPiedras inventarioAdapterPiedras = new InventarioAdapterPiedras();
                                inventarioAdapterPiedras.setPiedrasInventario(piedrasUser.getPiedras());
                                recyclerViewObjetos.setAdapter(inventarioAdapterPiedras);
                            }

                        }
                    });
                }
            }
        });
    }
}
