package com.example.pokemonproject.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.GamesInfo;
import com.example.pokemonproject.model.Username;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.ArrayList;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder> {

    private GameActivity context;
    ArrayList<GamesInfo> gamesInfoArrayList;
    String games;
    ArrayList<String> listGame;
    Username creator;
    String lastgame;
    String userID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GamesAdapter(GameActivity context, String id){
        this.context = context;
        this.lastgame = id;
    }

    @NonNull
    @Override
    public GamesAdapter.GamesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GamesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_games, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GamesAdapter.GamesViewHolder viewHolder, final int i) {
        viewHolder.textViewNameLeague.setText(gamesInfoArrayList.get(i).getNameLeague());
        viewHolder.textViewTeamName.setText(gamesInfoArrayList.get(i).getTeamName());
        viewHolder.money.setText(String.valueOf((int)gamesInfoArrayList.get(i).getMoney()));
        viewHolder.points.setText(String.valueOf(gamesInfoArrayList.get(i).getPoints()) + " puntos");
        GlideApp.with(viewHolder.itemView.getContext()).load(R.drawable.pokemondollar).into(viewHolder.dollar);
        GlideApp.with(viewHolder.itemView.getContext()).load(R.drawable.fondotextbox).into(viewHolder.fondo);

        db.collection("Users")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                games = document.get("games").toString();
                                creator = document.toObject(Username.class);
                                listGame = creator.getListGames();
                                userID = document.getId();
                            }
                        }
                    }
                });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gamesInfoArrayList.get(i).getId().equals(lastgame)){
                    Toast.makeText(context, "Estas actualmente en esta partida", Toast.LENGTH_LONG).show();

                }
                else {
                    context.finish();
                    Intent intent = new Intent(v.getContext(), GameActivity.class);
                    db.collection("Users").document(userID).update("lastGame", gamesInfoArrayList.get(i).getId());
                    intent.putExtra("lastGame", gamesInfoArrayList.get(i).getId());
                    intent.putExtra("games", Integer.parseInt(games));
                    intent.putExtra("listGames", listGame);
                    v.getContext().startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return  (gamesInfoArrayList != null ? gamesInfoArrayList.size() : 0);
    }

    public void setGamesInfoArrayList(ArrayList<GamesInfo> gamesarray) {
        gamesInfoArrayList = gamesarray;
    }

    class GamesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNameLeague;
        TextView textViewTeamName;
        TextView money;
        TextView points;
        ImageView dollar;
        ImageView fondo;

        GamesViewHolder(View view){
            super(view);

           textViewNameLeague = view.findViewById(R.id.games_nameLeague);
           textViewTeamName = view.findViewById(R.id.games_teamName);
           money = view.findViewById(R.id.games_money);
           points = view.findViewById(R.id.games_points);
           dollar = view.findViewById(R.id.imgPokedolarPartida);
           fondo = view.findViewById(R.id.imgModalBoxFondo);
        }

    }
}
