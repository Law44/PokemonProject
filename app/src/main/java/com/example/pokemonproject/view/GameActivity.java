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

    PokemonApi pokemonApi = PokemonModule.getAPI();
    final Executor executor = Executors.newFixedThreadPool(2);
    final Executor executor2 = Executors.newFixedThreadPool(2);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int numbergames;
    String id;
    String user;
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
                                    nameGame = document.toObject(Partida.class).getName();
                                    NavigationView navigationView = findViewById(R.id.nav_view);
                                    Menu nav_Menu = navigationView.getMenu();
                                    nav_Menu.findItem(R.id.gamesList).setTitle(nameGame);                                }
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
                Button iconSearch =  findViewById(R.id.action_search);
                iconSearch.setVisibility(View.VISIBLE);

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
                new ModalSelectGame(GameActivity.this);
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
                readApi();
                readApiMovements();
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
                                for (int j = 0; j < response.body().getMoves().size();j++){
                                    String idMove ="";
                                    for (int z = 31;;z++){
                                        if (response.body().getMoves().get(j).move.url.charAt(z) != '/'){
                                            idMove += response.body().getMoves().get(j).move.url.charAt(z);
                                        }else {
                                            break;
                                        }
                                    }
                                    response.body().getMoves().get(j).move.id = Integer.parseInt(idMove);
                                }
                                String namePokemon = response.body().getName().substring(0, 1).toUpperCase() + response.body().getName().substring(1).toLowerCase();
                                Pokemon pokemon = new Pokemon(response.body().getId(), namePokemon, response.body().getSprites(), response.body().getStats(), response.body().getTypes(), response.body().getMoves());

                                addPriceEvo(pokemon);
                                db.collection("ListaPokemon")
                                        .add(pokemon);
                            }
                        }

                        private void addPriceEvo(Pokemon pokemon) {
                            switch (pokemon.getId()){
                                case 1:
                                    pokemon.setIdEvo(2);
                                    pokemon.setPrice(350);
                                    break;
                                case 2:
                                    pokemon.setIdEvo(3);
                                    pokemon.setPrice(1000);
                                    break;
                                case 3:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4000);
                                    break;
                                case 4:
                                    pokemon.setIdEvo(5);
                                    pokemon.setPrice(350);
                                    break;
                                case 5:
                                    pokemon.setIdEvo(6);
                                    pokemon.setPrice(1000);
                                    break;
                                case 6:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4000);
                                    break;
                                case 7:
                                    pokemon.setIdEvo(8);
                                    pokemon.setPrice(350);
                                    break;
                                case 8:
                                    pokemon.setIdEvo(9);
                                    pokemon.setPrice(1000);
                                    break;
                                case 9:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4000);
                                    break;
                                case 10:
                                    pokemon.setIdEvo(10);
                                    pokemon.setPrice(150);
                                    break;
                                case 11:
                                    pokemon.setIdEvo(12);
                                    pokemon.setPrice(300);
                                    break;
                                case 12:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1750);
                                    break;
                                case 13:
                                    pokemon.setIdEvo(14);
                                    pokemon.setPrice(150);
                                    break;
                                case 14:
                                    pokemon.setIdEvo(15);
                                    pokemon.setPrice(300);
                                    break;
                                case 15:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1750);
                                    break;
                                case 16:
                                    pokemon.setIdEvo(17);
                                    pokemon.setPrice(250);
                                    break;
                                case 17:
                                    pokemon.setIdEvo(18);
                                    pokemon.setPrice(900);
                                    break;
                                case 18:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2800);
                                    break;
                                case 19:
                                    pokemon.setIdEvo(20);
                                    pokemon.setPrice(90);
                                    break;
                                case 20:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(600);
                                    break;
                                case 21:
                                    pokemon.setIdEvo(22);
                                    pokemon.setPrice(200);
                                    break;
                                case 22:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 23:
                                    pokemon.setIdEvo(24);
                                    pokemon.setPrice(400);
                                    break;
                                case 24:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1850);
                                    break;
                                case 25:
                                    pokemon.setIdEvo(26);
                                    pokemon.setPrice(750);
                                    break;
                                case 26:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    break;
                                case 27:
                                    pokemon.setIdEvo(28);
                                    pokemon.setPrice(500);
                                    break;
                                case 28:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 29:
                                    pokemon.setIdEvo(30);
                                    pokemon.setPrice(400);
                                    break;
                                case 30:
                                    pokemon.setIdEvo(31);
                                    pokemon.setPrice(975);
                                    break;
                                case 31:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3250);
                                    break;
                                case 32:
                                    pokemon.setIdEvo(33);
                                    pokemon.setPrice(400);
                                    break;
                                case 33:
                                    pokemon.setIdEvo(34);
                                    pokemon.setPrice(975);
                                    break;
                                case 34:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3250);
                                    break;
                                case 35:
                                    pokemon.setIdEvo(36);
                                    pokemon.setPrice(850);
                                    break;
                                case 36:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 37:
                                    pokemon.setIdEvo(38);
                                    pokemon.setPrice(600);
                                    break;
                                case 38:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2600);
                                    break;
                                case 39:
                                    pokemon.setIdEvo(40);
                                    pokemon.setPrice(850);
                                    break;
                                case 40:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 41:
                                    pokemon.setIdEvo(42);
                                    pokemon.setPrice(165);
                                    break;
                                case 42:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2225);
                                    break;
                                case 43:
                                    pokemon.setIdEvo(44);
                                    pokemon.setPrice(160);
                                    break;
                                case 44:
                                    pokemon.setIdEvo(45);
                                    pokemon.setPrice(900);
                                    break;
                                case 45:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    break;
                                case 46:
                                    pokemon.setIdEvo(47);
                                    pokemon.setPrice(250);
                                    break;
                                case 47:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1450);
                                    break;
                                case 48:
                                    pokemon.setIdEvo(49);
                                    pokemon.setPrice(500);
                                    break;
                                case 49:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    break;
                                case 50:
                                    pokemon.setIdEvo(51);
                                    pokemon.setPrice(700);
                                    break;
                                case 51:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    break;
                                case 52:
                                    pokemon.setIdEvo(53);
                                    pokemon.setPrice(650);
                                    break;
                                case 53:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1300);
                                    break;
                                case 54:
                                    pokemon.setIdEvo(55);
                                    pokemon.setPrice(500);
                                    break;
                                case 55:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2300);
                                    break;
                                case 56:
                                    pokemon.setIdEvo(57);
                                    pokemon.setPrice(800);
                                    break;
                                case 57:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    break;
                                case 58:
                                    pokemon.setIdEvo(59);
                                    pokemon.setPrice(1000);
                                    break;
                                case 59:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3000);
                                    break;
                                case 60:
                                    pokemon.setIdEvo(61);
                                    pokemon.setPrice(450);
                                    break;
                                case 61:
                                    pokemon.setIdEvo(62);
                                    pokemon.setPrice(1000);
                                    break;
                                case 62:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2750);
                                    break;
                                case 63:
                                    pokemon.setIdEvo(64);
                                    pokemon.setPrice(400);
                                    break;
                                case 64:
                                    pokemon.setIdEvo(65);
                                    pokemon.setPrice(1600);
                                    break;
                                case 65:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(5000);
                                    break;
                                case 66:
                                    pokemon.setIdEvo(67);
                                    pokemon.setPrice(450);
                                    break;
                                case 67:
                                    pokemon.setIdEvo(68);
                                    pokemon.setPrice(1400);
                                    break;
                                case 68:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4250);
                                    break;
                                case 69:
                                    pokemon.setIdEvo(70);
                                    pokemon.setPrice(300);
                                    break;
                                case 70:
                                    pokemon.setIdEvo(71);
                                    pokemon.setPrice(975);
                                    break;
                                case 71:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2150);
                                    break;
                                case 72:
                                    pokemon.setIdEvo(73);
                                    pokemon.setPrice(500);
                                    break;
                                case 73:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    break;
                                case 74:
                                    pokemon.setIdEvo(75);
                                    pokemon.setPrice(500);
                                    break;
                                case 75:
                                    pokemon.setIdEvo(76);
                                    pokemon.setPrice(1500);
                                    break;
                                case 76:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4375);
                                    break;
                                case 77:
                                    pokemon.setIdEvo(78);
                                    pokemon.setPrice(950);
                                    break;
                                case 78:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2450);
                                    break;
                                case 79:
                                    pokemon.setIdEvo(80);
                                    pokemon.setPrice(300);
                                    break;
                                case 80:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 81:
                                    pokemon.setIdEvo(82);
                                    pokemon.setPrice(500);
                                    break;
                                case 82:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2200);
                                    break;
                                case 83:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1000);
                                    break;
                                case 84:
                                    pokemon.setIdEvo(85);
                                    pokemon.setPrice(750);
                                    break;
                                case 85:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    break;
                                case 86:
                                    pokemon.setIdEvo(86);
                                    pokemon.setPrice(900);
                                    break;
                                case 87:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    break;
                                case 88:
                                    pokemon.setIdEvo(89);
                                    pokemon.setPrice(700);
                                    break;
                                case 89:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1850);
                                    break;
                                case 90:
                                    pokemon.setIdEvo(91);
                                    pokemon.setPrice(650);
                                    break;
                                case 91:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1900);
                                    break;
                                case 92:
                                    pokemon.setIdEvo(93);
                                    pokemon.setPrice(500);
                                    break;
                                case 93:
                                    pokemon.setIdEvo(94);
                                    pokemon.setPrice(1500);
                                    break;
                                case 94:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4500);
                                    break;
                                case 95:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1500);
                                    break;
                                case 96:
                                    pokemon.setIdEvo(97);
                                    pokemon.setPrice(600);
                                    break;
                                case 97:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    break;
                                case 98:
                                    pokemon.setIdEvo(99);
                                    pokemon.setPrice(550);
                                    break;
                                case 99:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 100:
                                    pokemon.setIdEvo(101);
                                    pokemon.setPrice(800);
                                    break;
                                case 101:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1600);
                                    break;
                                case 102:
                                    pokemon.setIdEvo(103);
                                    pokemon.setPrice(1450);
                                    break;
                                case 104:
                                    pokemon.setIdEvo(105);
                                    pokemon.setPrice(500);
                                    break;
                                case 105:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1700);
                                    break;
                                case 106:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    break;
                                case 107:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1750);
                                    break;
                                case 108:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1000);
                                    break;
                                case 109:
                                    pokemon.setIdEvo(110);
                                    pokemon.setPrice(600);
                                    break;
                                case 110:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    break;
                                case 111:
                                    pokemon.setIdEvo(112);
                                    pokemon.setPrice(1000);
                                    break;
                                case 112:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3500);
                                    break;
                                case 113:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 114:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1250);
                                    break;
                                case 115:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2300);
                                    break;
                                case 116:
                                    pokemon.setIdEvo(117);
                                    pokemon.setPrice(800);
                                    break;
                                case 117:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1650);
                                    break;
                                case 118:
                                    pokemon.setIdEvo(119);
                                    pokemon.setPrice(700);
                                    break;
                                case 119:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1450);
                                    break;
                                case 120:
                                    pokemon.setIdEvo(121);
                                    pokemon.setPrice(1150);
                                    break;
                                case 121:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    break;
                                case 122:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1700);
                                    break;
                                case 123:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1850);
                                    break;
                                case 124:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    break;
                                case 125:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2100);
                                    break;
                                case 126:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2100);
                                    break;
                                case 127:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1500);
                                    break;
                                case 128:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2150);
                                    break;
                                case 129:
                                    pokemon.setIdEvo(130);
                                    pokemon.setPrice(50);
                                    break;
                                case 130:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4750);
                                    break;
                                case 131:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2300);
                                    break;
                                case 132:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1250);
                                    break;
                                case 133:
                                    pokemon.setIdEvo(134135136);
                                    pokemon.setPrice(800);
                                    break;
                                case 134:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    break;
                                case 135:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    break;
                                case 136:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    break;
                                case 137:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1600);
                                    break;
                                case 138:
                                    pokemon.setIdEvo(139);
                                    pokemon.setPrice(950);
                                    break;
                                case 139:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    break;
                                case 140:
                                    pokemon.setIdEvo(141);
                                    pokemon.setPrice(950);
                                    break;
                                case 141:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    break;
                                case 142:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    break;
                                case 143:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4400);
                                    break;
                                case 144:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(10000);
                                    break;
                                case 145:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(10000);
                                    break;
                                case 146:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(10000);
                                    break;
                                case 147:
                                    pokemon.setIdEvo(148);
                                    pokemon.setPrice(1000);
                                    break;
                                case 148:
                                    pokemon.setIdEvo(149);
                                    pokemon.setPrice(3000);
                                    break;
                                case 149:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(7000);
                                    break;
                                case 150:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(12000);
                                    break;
                                case 151:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(8000);
                                    break;
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

    public void readApiMovements(){
        for (int i = 1; i < 729; i++) {
            pokemonApi.getMovement(i).enqueue(new Callback<Movement>() {
                @Override
                public void onResponse(Call<Movement> call, final Response<Movement> response) {
                    executor2.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (response.body() != null) {
                                if (response.body().power != null) {
                                    Movement movement = new Movement(response.body().id, response.body().name, response.body().names, response.body().power, response.body().pp, response.body().accuracy, response.body().priority, response.body().type);
                                    String nombre = "";
                                    for (int j = 0; j < movement.getNames().size(); j++) {
                                        if (movement.getNames().get(j).language.name.equals("es")){
                                            nombre = movement.getNames().get(j).name;
                                        }
                                    }
                                    MovementFirebase movementFirebase = new MovementFirebase(movement.getId(), movement.getAccuracy(), Integer.parseInt(movement.getPower()), movement.getPp(), movement.getPriority(), movement.getType().getName(), nombre);
                                    db.collection("Movimientos")
                                            .add(movementFirebase);
                                }

                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<Movement> call, Throwable t) {
                    Log.e("ERROR", t.getMessage());
                }
            });
        }
    }

}
