package com.example.pokemonproject.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokemonproject.R;

public class JornadaFragment extends Fragment implements GameActivity.QueryChangeListener {

    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_jornada, container, false);
        return mView;
    }

    @Override
    public void onQueryChange(String query) {

    }
}
