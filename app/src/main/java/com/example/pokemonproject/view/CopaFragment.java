package com.example.pokemonproject.view;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.UserGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

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
        db.collection("Partidas").document(idGame).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null){

                }

                Partida partida = documentSnapshot.toObject(Partida.class);
                usersGames = partida.getUsers();
                int limite = usersGames.size()-1;
                for (int i = 0; i < limite;) {
                    if (usersGames.get(i).getPoints()<usersGames.get(i+1).getPoints()){
                        UserGame tmp = usersGames.get(i+1);
                        usersGames.set(i+1, usersGames.get(i));
                        usersGames.set(i,tmp);
                    }
                    i++;
                    if (i == limite){
                        i = 0;
                        limite--;
                    }
                }
                recyclerView = mView.findViewById(R.id.rvClasificacion);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                DividerItemDecoration itemDecor = new DividerItemDecoration(mView.getContext(), VERTICAL);
                recyclerView.addItemDecoration(itemDecor);
                adapter = new UserGameRecyclerAdapter(usersGames);
                recyclerView.setAdapter(adapter);
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
            GlideApp.with(holder.itemView.getContext()).load(userGame.getUser().getImgurl()).circleCrop().into(holder.userGameImg);

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
            private ImageView userGameImg;
            public UserGameViewHolder(@NonNull View itemView) {
                super(itemView);
                userGameName =itemView.findViewById(R.id.tvClasUserGameName);
                userGameTeam = itemView.findViewById(R.id.tvClasUserGameTeamName);
                userGamePoints = itemView.findViewById(R.id.tvClasUserGamePoints);
                userGamePosition = itemView.findViewById(R.id.tvPosicionUsergame);
                userGameImg = itemView.findViewById(R.id.imgUserClas);
            }
        }
    }
}
