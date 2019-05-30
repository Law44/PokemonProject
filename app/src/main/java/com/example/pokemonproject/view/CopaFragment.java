package com.example.pokemonproject.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.UserGame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class CopaFragment extends Fragment implements GameActivity.QueryChangeListener {
    private GameActivity gameActivity;
    private UserGameRecyclerAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idGame ="";
    ArrayList<UserGame> usersGames = new ArrayList<>();
    RecyclerView recyclerView;
    View mView;

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_copa, container, false);
        db.collection("Partidas").document(idGame).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Partida partida = documentSnapshot.toObject(Partida.class);
                    usersGames = partida.getUsers();
                    for (int i = 0; i < usersGames.size()-1; i++) {
                        if (usersGames.get(i).getPoints()<usersGames.get(i+1).getPoints()){
                            UserGame tmp = usersGames.get(i+1);
                            usersGames.set(i+1, usersGames.get(i));
                            usersGames.set(i,tmp);
                        }
                    }
                    recyclerView = mView.findViewById(R.id.rvClasificacion);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
                    recyclerView.addItemDecoration(itemDecor);
                    adapter = new UserGameRecyclerAdapter(usersGames);
                    recyclerView.setAdapter(adapter);

                }

            }
        });
        return mView;
    }

    public void setIdLastPartida(String id) {
        this.idGame = id;
    }

    @Override
    public void onQueryChange(String query) {

    }

    private class UserGameRecyclerAdapter extends RecyclerView.Adapter<UserGameRecyclerAdapter.UserGameViewHolder> {
        ArrayList<UserGame> list;

        UserGameRecyclerAdapter(ArrayList<UserGame> list){
            this.list = list;
        }
        @NonNull
        @Override
        public UserGameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemUsergame = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usergame, viewGroup,false);
            return new UserGameViewHolder(itemUsergame);
        }

        @Override
        public void onBindViewHolder(@NonNull UserGameViewHolder holder, int i) {
            final UserGame userGame = list.get(i);

            holder.userGameName.setText(userGame.getUser().getUsername());
            holder.userGameTeam.setText(userGame.getTeamName());
            holder.userGamePoints.setText(String.valueOf(userGame.getPoints()));
            holder.userGamePosition.setText(String.valueOf(i+1));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!userGame.getUser().getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        new ModalEnemyTeam(gameActivity, userGame.getUser().getEmail(), idGame);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return (list != null ? list.size() : 0);
        }

        class UserGameViewHolder extends RecyclerView.ViewHolder {
            private TextView userGameName;
            private TextView userGameTeam;
            private TextView userGamePoints;
            private TextView userGamePosition;
            public UserGameViewHolder(@NonNull View itemView) {
                super(itemView);
                userGameName =itemView.findViewById(R.id.tvClasUserGameName);
                userGameTeam = itemView.findViewById(R.id.tvClasUserGameTeamName);
                userGamePoints = itemView.findViewById(R.id.tvClasUserGamePoints);
                userGamePosition = itemView.findViewById(R.id.tvPosicionUsergame);
            }
        }
    }
}
