package com.example.pokemonproject.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pokemonproject.R;
import com.example.pokemonproject.api.PokemonApi;
import com.example.pokemonproject.api.PokemonModule;
import com.example.pokemonproject.model.Alineation;
import com.example.pokemonproject.model.Combate;
import com.example.pokemonproject.model.Equipo;
import com.example.pokemonproject.model.ListaPujas;
import com.example.pokemonproject.model.Movement;
import com.example.pokemonproject.model.MovementFirebase;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerActivity extends AppCompatActivity {
    FirebaseFirestore db;
    List<Pokemon> pkemonList;
    List<MovementFirebase> movementList;
    TextView estado ;
    List<String> idPujas;
    PokemonApi pokemonApi;
    Alineation alineation1, alineation2;
    Partida partida;
    ArrayList<String> partidas;
    final Executor executor = Executors.newFixedThreadPool(2);
    final Executor executor2 = Executors.newFixedThreadPool(2);
    int jornada = 0;
    boolean calculojornada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        estado = findViewById(R.id.estado);
        db = FirebaseFirestore.getInstance();
        pkemonList = new ArrayList<>();
        movementList = new ArrayList<>();
        pokemonApi = PokemonModule.getAPI();

        Button btnCombates = findViewById(R.id.Combates);
        Button btnRefrescarMercado = findViewById(R.id.Mercado);
        Button btnPujas = findViewById(R.id.Pujas);
        Button btnPrepararCombate = findViewById(R.id.preparaCombate);
        Button btnListaPokemon = findViewById(R.id.listapokemon);
        Button btnListaMovimientos = findViewById(R.id.listamovimientos);
        Button btnLoadPokemon = findViewById(R.id.loadPokemon);
        Button btnLoadMovement = findViewById(R.id.loadMovement);

        btnLoadPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("ListaPokemon").orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot: task.getResult()) {
                                pkemonList.add(snapshot.toObject(Pokemon.class));
                            }
                        }
                    }
                });
            }
        });

        btnLoadMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Movimientos").orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                movementList.add(snapshot.toObject(MovementFirebase.class));
                            }
                            estado.setText("Ready");
                        }
                    }
                });
            }
        });



        btnPrepararCombate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                prepararCombates();
            }
        });


        btnCombates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                prepararCombates();
            }
        });

        btnRefrescarMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                refrescarMercado();
            }
        });
        btnPujas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                asignarPujas();
            }
        });

        btnListaPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setText("Wait");
                readApi();
            }
        });

        btnListaMovimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setText("Wait");
                readApiMovements();
            }
        });
    }

    private void asignarPujas() {

        estado.setText("Wait");
        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();
                    consultarPartidas(a);
                }

            }
        });

    }


    void consultarPartidas(Iterator<QueryDocumentSnapshot> a){
        if(!a.hasNext()){

            return;
        }
        Map<Integer,String> idjugadores = new HashMap<>();
        Map<Integer,Integer> pujas = new HashMap<>();

        QueryDocumentSnapshot queryDocumentSnapshot = a.next();
        idPujas = new ArrayList<>();
        for (int i = 0; i<10;i++){
            idjugadores.put(i,"");
            pujas.put(i,0);
        }


        Partida partida = queryDocumentSnapshot.toObject(Partida.class);

        consultarPujas(partida, 0,idjugadores,pujas);

//        subirDatos(partida,idjugadores,pujas);/*--------------------------------*/

        consultarPartidas(a);
    }

    private void subirDatos(final Partida partida, final int i, final Map<Integer, String> idjugadores, final Map<Integer, Integer> pujas) {
        if (i>=idjugadores.size()){
            refrescarMercado();
            estado.setText("Done");
            return;
        }
        Log.e("ListaPujas",String.valueOf(i)+"   "+String.valueOf(idjugadores.size()));
        if (pujas.get(i)>0){
            final String idPujaJugador = idjugadores.get(i);

            for (int j = 0; j < partida.getUsers().size(); j++) {
                if (partida.getUsers().get(j).getPujasID().equals(idjugadores.get(i))){
                    final String idTeamJugador = partida.getUsers().get(j).getTeamID();
                    final int pokemonPos = i;
                    final int posJugadorPartida = j;
                    db.collection("Equipos").document(idTeamJugador).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final Team team = task.getResult().toObject(Team.class);
                                db.collection("Mercado").document(partida.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            ListaPujas mercado = task.getResult().toObject(ListaPujas.class);
                                            team.getEquipo().add(mercado.getLista().get(pokemonPos));
                                            db.collection("Equipos").document(idTeamJugador).update("equipo", team.getEquipo());

                                            partida.getUsers().get(posJugadorPartida).setMoney(partida.getUsers().get(posJugadorPartida).getMoney()-pujas.get(i));
                                            db.collection("Partidas").document(partida.getId()).set(partida);


                                            subirDatos(partida,i+1,idjugadores,pujas);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

        }else subirDatos(partida,i+1,idjugadores,pujas);

    }

    void consultarPujas(final Partida partida, final int i, final Map<Integer, String> idjugadores, final Map<Integer, Integer> pujas){

        if(i>=partida.getUsers().size()) {
            subirDatos(partida,0,idjugadores,pujas);
            return;
        }

        idPujas.add(partida.getUsers().get(i).getTeamID());

        db.collection("Pujas").document(partida.getUsers().get(i).getPujasID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Pujas listaPujas = new Pujas();
                    listaPujas = task.getResult().toObject(Pujas.class);
                    for (int j =0;j<listaPujas.getPujas().size();j++){
                        if (pujas.get(j) < listaPujas.getPujas().get(j)){
                            idjugadores.put(j,task.getResult().getId());
                            pujas.put(j,listaPujas.getPujas().get(j));

                        }
                    }
                    consultarPujas(partida, i+1, idjugadores, pujas);
                    Pujas pujasAZero = new Pujas();
                    db.collection("Pujas").document(partida.getUsers().get(i).getPujasID()).set(pujasAZero);
                }
            }
        });

    }

    private void refrescarMercado() {

        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int i =0;
                    for (QueryDocumentSnapshot snapshot: task.getResult()) {
                        ListaPujas lista = new ListaPujas();
                        List<Pokemon> listaMercado = hacerListaMercado();
                        lista.setLista((ArrayList<Pokemon>) listaMercado);

                        db.collection("Mercado").document(snapshot.getId()).set(lista);

                    }
                    estado.setText("Ready");
                }
            }
        });
    }

    private List<Pokemon> hacerListaMercado() {
        int cantidad = 0;
        Random random = new Random();
        int[] idCogidos = new int[10];
        do {

            for (int i = 0; i < idCogidos.length; i++) {
                idCogidos[i] = random.nextInt(151);
            }

            for (int i = 0; i < idCogidos.length; i++) {
                cantidad += pkemonList.get(idCogidos[i]).getPrice();
            }
        }while (cantidad>18000);
        List<Pokemon> listaMercado = new ArrayList<>();
        List<MovementFirebase> listaTemp = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Pokemon pokemon = pkemonList.get(idCogidos[i]);
            for (int j = 0; j < pokemon.getMoves().size(); j++) {
                if (movementList.get(pokemon.getMoves().get(j).move.id - 1).power == 0) {
                    listaTemp.add(movementList.get(pokemon.getMoves().get(j).move.id - 1));
                }
            }

            for (int j = 0; j < listaTemp.size(); j++) {
                for (int k = 0; k < pokemon.getMoves().size(); k++) {
                    if (listaTemp.get(j).id == pokemon.getMoves().get(k).move.id){
                        pokemon.getMoves().remove(k);
                    }
                }
            }

            do {
                pokemon.getMoves().remove( pokemon.getMoves().get(random.nextInt(pokemon.getMoves().size())));
            }while (pokemon.getMoves().size()>4);
            listaMercado.add(pkemonList.get(idCogidos[i]));
        }

        return listaMercado;
    }

    private void prepararCombates() {
        partidas = new ArrayList<>();
        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        partidas.add(snapshot.getId());
                    }

                    for (int i = 0; i < partidas.size(); i++) {
                        calculojornada = false;
                        jornada = 0;
                        db.collection("Partidas").document(partidas.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot snapshot = task.getResult();
                                        partida = snapshot.toObject(Partida.class);
                                        for (int i = partida.getUsers().size()-1; i > 0; i--) {
                                            for (int j = 0; j < i; j++) {
                                                final int finalJ = j;
                                                final int finalI = i;
                                                db.collection("Alineaciones").document(partida.getUsers().get(i).getAlineationID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                            alineation1 = documentSnapshot.toObject(Alineation.class);
                                                            db.collection("Alineaciones").document(partida.getUsers().get(finalJ).getAlineationID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()){
                                                                        final String idCombate = db.collection("Combates").document().getId();

                                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                                        alineation2 = documentSnapshot.toObject(Alineation.class);
                                                                        partida.getUsers().get(finalI).getCombatesID().add(idCombate);
                                                                        partida.getUsers().get(finalJ).getCombatesID().add(idCombate);
                                                                        db.collection("Partidas").document(partida.getId()).set(partida);
                                                                        db.collection("Combates").whereEqualTo("idGame", partida.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()){
                                                                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                                                        Combate combate = snapshot.toObject(Combate.class);
                                                                                        if (combate.getJornada() > jornada && !calculojornada) {
                                                                                            jornada = combate.getJornada();
                                                                                        }
                                                                                    }
                                                                                    if (!calculojornada) {
                                                                                        calculojornada = true;
                                                                                        jornada++;
                                                                                    }
                                                                                    Equipo equipo1 = new Equipo(partida.getUsers().get(finalI).getUser().getUsername(), partida.getUsers().get(finalI).getUser().getEmail(), alineation1, partida.getUsers().get(finalI).getUser().getImgurl());
                                                                                    Equipo equipo2 = new Equipo(partida.getUsers().get(finalJ).getUser().getUsername(), partida.getUsers().get(finalJ).getUser().getEmail(), alineation2, partida.getUsers().get(finalJ).getUser().getImgurl());
                                                                                    Combate nextCombate = new Combate(equipo1, equipo2, jornada, partida.getId());
                                                                                    db.collection("Combates").document(idCombate).set(nextCombate);
                                                                                }
                                                                                else {
                                                                                    Equipo equipo1 = new Equipo(partida.getUsers().get(finalI).getUser().getUsername(), partida.getUsers().get(finalI).getUser().getEmail(), alineation1, partida.getUsers().get(finalI).getUser().getImgurl());
                                                                                    Equipo equipo2 = new Equipo(partida.getUsers().get(finalJ).getUser().getUsername(), partida.getUsers().get(finalJ).getUser().getEmail(), alineation2, partida.getUsers().get(finalJ).getUser().getImgurl());
                                                                                    Combate combate = new Combate(equipo1, equipo2, 1, partida.getId());
                                                                                    db.collection("Combates").document(idCombate).set(combate);
                                                                                }
                                                                            }


                                                                        });



                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                }
                            }
                        });
                    }
                    estado.setText("Ready");

                }
            }
        });


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
        estado.setText("Ready");
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
                                    Movement movement = new Movement(response.body().id, response.body().name, response.body().names, response.body().power, response.body().pp, response.body().accuracy, response.body().priority, response.body().type);
                                    String nombre = "";
                                    for (int j = 0; j < movement.getNames().size(); j++) {
                                        if (movement.getNames().get(j).language.name.equals("es")){
                                            nombre = movement.getNames().get(j).name;
                                        }
                                    }
                                    if (response.body().power == null){
                                        movement.power = "0";
                                    }
                                    MovementFirebase movementFirebase = new MovementFirebase(movement.getId(), movement.getAccuracy(), Integer.parseInt(movement.getPower()), movement.getPp(), movement.getPriority(), movement.getType().getName(), nombre);
                                    db.collection("Movimientos")
                                            .add(movementFirebase);


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
        estado.setText("Ready");
    }
}
