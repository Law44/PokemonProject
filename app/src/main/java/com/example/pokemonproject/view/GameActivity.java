package com.example.pokemonproject.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.api.PokemonApi;
import com.example.pokemonproject.api.PokemonModule;
import com.example.pokemonproject.model.Movement;
import com.example.pokemonproject.model.MovementFirebase;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Stats;
import com.example.pokemonproject.model.Team;
import com.example.pokemonproject.model.UserGame;
import com.example.pokemonproject.model.Username;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int numbergames;
    String id;
    String user;
    Partida partida;
    String nameGame;
    ArrayList<String> listGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        numbergames = getIntent().getIntExtra("games", 0);
        id = getIntent().getStringExtra("lastGame");
        listGames = getIntent().getStringArrayListExtra("listGames");

        if (id != null) {
            if (!id.equals("")) {
                db.collection("Partidas")
                        .document(id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    partida = document.toObject(Partida.class);
                                    nameGame = partida.getName();
                                    NavigationView navigationView = findViewById(R.id.nav_view);
                                    Menu nav_Menu = navigationView.getMenu();
                                    nav_Menu.findItem(R.id.gamesList).setTitle(nameGame);
                                }
                            }
                        });
            }
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);

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
                        findViewById(R.id.action_search).setVisibility(View.INVISIBLE);
                        tab.setIcon(R.drawable.icons8_home_48_red);
                        title.setText(R.string.inicioCaps);
                        break;
                    case 1:
                        makeIconsWhite(tabLayout);
                        findViewById(R.id.action_search).setVisibility(View.INVISIBLE);
                        tab.setIcon(R.drawable.icons8_leaderboard_filled_50_red);
                        title.setText(R.string.clasificacionCaps);
                        break;
                    case 2:
                        makeIconsWhite(tabLayout);
                        findViewById(R.id.action_search).setVisibility(View.INVISIBLE);
                        tab.setIcon(R.drawable.icons8_pokeball_48_red);
                        title.setText(R.string.mercadoCaps);
                        break;
                    case 3:
                        makeIconsWhite(tabLayout);
                        findViewById(R.id.action_search).setVisibility(View.INVISIBLE);
                        tab.setIcon(R.drawable.icons8_explosion_filled_50_red);
                        title.setText(R.string.combateCaps);
                        break;
                    case 4:
                        makeIconsWhite(tabLayout);
                        findViewById(R.id.action_search).setVisibility(View.VISIBLE);
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
        Menu nav_Menu = navigationView.getMenu();




        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("tlaw4444@gmail.com") ||
                FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("ymoralesa96@elpuig.xeill.net")){
            nav_Menu.findItem(R.id.createDatabase).setVisible(true);
        }
        else {
            nav_Menu.findItem(R.id.createDatabase).setVisible(false);
        }


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
                                user = document.get("username").toString();
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
                    searchView.clearFocus();
                    Button iconSearch =  findViewById(R.id.action_search);
                    iconSearch.setVisibility(View.VISIBLE);
                    iconSearch.findFocus();
                } else {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        }else{
            if (drawer.isDrawerOpen(GravityCompat.START) ) {
                drawer.closeDrawer(GravityCompat.START);

            } else {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {



        switch (item.getItemId()) {

            case R.id.gamesList:{
                new ModalSelectGame(GameActivity.this, id);
                break;
            }

            case R.id.invite: {
                sendInvitation();
                break;
            }
            case R.id.create: {
                startActivity(new Intent(GameActivity.this, NewLeagueActivity.class));
                break;
            }
            case R.id.join: {
                startActivity(new Intent(GameActivity.this, JoinLeagueActivity.class));
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

            case R.id.createDatabase: {
                startActivity(new Intent(GameActivity.this, ServerActivity.class));
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void sendInvitation() {
        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            String text = "Utiliza este codigo para unirte a la liga " + nameGame + " creada por " + user + ":\n" + id;

            PackageInfo info= pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);

            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_game,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



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
                        HomeFragment homeFragment = new HomeFragment(GameActivity.this, id, numbergames, id, listGames);
                        return homeFragment;
                    case 1:
                        CopaFragment copaFragment = new CopaFragment();
                        copaFragment.setIdLastPartida(id);
                        return copaFragment;
                    case 2:
                        MercadoFragment mercadoFragment = new MercadoFragment(GameActivity.this, id);
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

}
