package com.example.pokemonproject.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pokemonproject.R;

public  class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView campoBatalla = mView.findViewById(R.id.imgCampoBatalla);
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .circleCrop()
                .into(campoBatalla);
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla2));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla3));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla4));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla5));
        GlideApp.with(this)
                .load(R.drawable.icons8_pokeball_80)
                .into((ImageView) mView.findViewById(R.id.imgCampoBatalla6));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta2));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta3));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta4));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta5));
        GlideApp.with(this)
                .load(R.drawable.magikarp_silhoutte)
                .into((ImageView) mView.findViewById(R.id.imgSilueta6));




        return mView;
    }
}
