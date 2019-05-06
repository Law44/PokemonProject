package com.example.pokemonproject.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.api.PokemonApi;
import com.example.pokemonproject.api.PokemonModule;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Stats;
import com.example.pokemonproject.model.Username;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    PokemonApi pokemonApi = PokemonModule.getAPI();
    final Executor executor = Executors.newFixedThreadPool(2);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int numbergames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        numbergames = getIntent().getIntExtra("games", 0);

        Log.e("ERROR", String.valueOf(numbergames));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final TextView title = findViewById(R.id.toolbar_title);
        title.setText("INCIO");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        makeIconsWhite(tabLayout);
                        tab.setIcon(R.drawable.icons8_home_48_red);
                        title.setText(R.string.inicioCaps);
                        break;
                    case 1:
                        makeIconsWhite(tabLayout);
                        tab.setIcon(R.drawable.icons8_leaderboard_filled_50_red);
                        title.setText(R.string.clasificacionCaps);
                        break;
                    case 2:
                        makeIconsWhite(tabLayout);
                        tab.setIcon(R.drawable.icons8_pokeball_48_red);
                        title.setText(R.string.mercadoCaps);
                        break;
                    case 3:
                        makeIconsWhite(tabLayout);
                        tab.setIcon(R.drawable.icons8_explosion_filled_50_red);
                        title.setText(R.string.combateCaps);
                        break;
                    case 4:
                        makeIconsWhite(tabLayout);
                        tab.setIcon(R.drawable.icons8_pokedex_filled_52_red);
                        title.setText(R.string.pokemonsCaps);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ImageView photo = header.findViewById(R.id.userPhoto);
        final TextView name = header.findViewById(R.id.userName);
        TextView email = header.findViewById(R.id.userEmail);

        GlideApp.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                .circleCrop()
                .into(photo);
        db.collection("Users")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name.setText(document.get("username").toString());
                                }
                        }
                    }
                });

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    private void makeIconsWhite(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_home_48);
        tabLayout.getTabAt(1).setIcon(R.drawable.icons8_leaderboard_52);
        tabLayout.getTabAt(2).setIcon(R.drawable.icons8_pokeball_48);
        tabLayout.getTabAt(3).setIcon(R.drawable.icons8_explosion_filled_50);
        tabLayout.getTabAt(4).setIcon(R.drawable.icons8_pokedex_52);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (findViewById(R.id.search_view)!= null) {
            MaterialSearchView searchView = findViewById(R.id.search_view);
            if (drawer.isDrawerOpen(GravityCompat.START) || searchView.isSearchOpen() ) {
                drawer.closeDrawer(GravityCompat.START);
                searchView.closeSearch();
                Button iconSearch =  findViewById(R.id.action_search);
                iconSearch.setVisibility(View.VISIBLE);

            } else {
                super.onBackPressed();
            }
        }else{
            if (drawer.isDrawerOpen(GravityCompat.START) ) {
                drawer.closeDrawer(GravityCompat.START);

            } else {
                super.onBackPressed();
            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_camera: {
                break;
            }
            case R.id.nav_gallery: {
                readApi();
                break;
            }
            case R.id.nav_manage: {
                break;
            }

            case R.id.sign_out: {

                AuthUI.getInstance()
                        .signOut(GameActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                break;

            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_game, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (numbergames == 0){
                NoLeagueFragment noLeagueFragment = new NoLeagueFragment();
                return noLeagueFragment;
            }
            else {
                switch (position) {
                    case 0:
                        HomeFragment homeFragment = new HomeFragment();
                        return homeFragment;
                    case 1:
                        CopaFragment copaFragment = new CopaFragment();
                        return copaFragment;
                    case 2:
                        MercadoFragment mercadoFragment = new MercadoFragment();
                        return mercadoFragment;
                    case 3:
                        JornadaFragment jornadaFragment = new JornadaFragment();
                        return jornadaFragment;
                    case 4:
                        ListaFragment listaFragment = new ListaFragment();
                        return listaFragment;
                }
                return new HomeFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }


    public void readApi(){

        for (int i = 1; i < 152; i++) {
            pokemonApi.getPokemon(i).enqueue(new Callback<Pokemon>() {
                @Override
                public void onResponse(Call<Pokemon> call, final Response<Pokemon> response) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if(response.body()!=null) {
                                for (int j = 0; j < response.body().getStats().size(); j++) {
                                    if (j == 5) {
                                        response.body().getStats().get(j).base_stat+= 60;
                                    }
                                    else {
                                        response.body().getStats().get(j).base_stat+= 5;
                                    }
                                }
                                Pokemon pokemon = new Pokemon(response.body().getId(), response.body().getName(), response.body().getSprites(), response.body().getStats(), response.body().getTypes());
                                db.collection("ListaPokemon")
                                        .add(pokemon);
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                }
            });
        }
    }

}
