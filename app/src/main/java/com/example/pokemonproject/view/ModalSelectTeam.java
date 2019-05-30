package com.example.pokemonproject.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.view.AlineationAdapter;
import com.example.pokemonproject.view.GameActivity;
import com.example.pokemonproject.view.GamesAdapter;
import com.example.pokemonproject.view.TeamAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class ModalSelectTeam {

    GameActivity context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> listGames;
    ArrayList<GamesInfo> gamesInfos = new ArrayList<>();

    public ModalSelectTeam(final GameActivity context, ArrayList<Pokemon> team, String alineationID, int i, int numbergames, String idLastGame, ArrayList<String> listGames, ArrayList<List<Moves>> movements){

        this.context = context;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_modal_selectalineation);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView recyclerView =  dialog.findViewById(R.id.rvalineationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        AlineationAdapter alineationAdapter = new AlineationAdapter(context, alineationID, i, numbergames, idLastGame, listGames, dialog, movements);
        alineationAdapter.setGamesInfoArrayList(team);

        recyclerView.setAdapter(alineationAdapter);

        dialog.show();
    }
}
