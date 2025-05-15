package com.example.pokemonproject.view;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Moves;
import com.example.pokemonproject.model.Pokemon;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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
