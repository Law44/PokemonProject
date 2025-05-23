package com.example.pokemonproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pokemonproject.R;

public class NoLeagueFragment extends Fragment implements GameActivity.QueryChangeListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View mView = inflater.inflate(R.layout.fragment_noleague, container, false);

        mView.findViewById(R.id.newLeague).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewLeagueActivity.class));

            }
        });

        mView.findViewById(R.id.joinLeague).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JoinLeagueActivity.class));
            }
        });

        return mView;

    }

    @Override
    public void onQueryChange(String query) {

    }
}
