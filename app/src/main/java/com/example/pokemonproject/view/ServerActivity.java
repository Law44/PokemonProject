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
import com.example.pokemonproject.model.ListaPujasPiedras;
import com.example.pokemonproject.model.Movement;
import com.example.pokemonproject.model.MovementFirebase;
import com.example.pokemonproject.model.Partida;
import com.example.pokemonproject.model.PiedraEvo;
import com.example.pokemonproject.model.PiedrasEvoFirebase;
import com.example.pokemonproject.model.PiedrasEvoUser;
import com.example.pokemonproject.model.PiedrasUser;
import com.example.pokemonproject.model.Pokemon;
import com.example.pokemonproject.model.Pujas;
import com.example.pokemonproject.model.PujasPiedras;
import com.example.pokemonproject.model.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    ArrayList<PiedrasEvoFirebase> listaPiedras;
    ArrayList<PiedrasEvoUser> listaPiedrasMercado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        estado = findViewById(R.id.estado);
        db = FirebaseFirestore.getInstance();
        pkemonList = new ArrayList<>();
        movementList = new ArrayList<>();
        pokemonApi = PokemonModule.getAPI();
        listaPiedras = new ArrayList<>();
        listaPiedrasMercado = new ArrayList<>();

        Button btnCombates = findViewById(R.id.Combates);
        Button btnRefrescarMercado = findViewById(R.id.Mercado);
        Button btnPujas = findViewById(R.id.Pujas);
        Button btnPrepararCombate = findViewById(R.id.preparaCombate);
        Button btnListaPokemon = findViewById(R.id.listapokemon);
        Button btnListaMovimientos = findViewById(R.id.listamovimientos);
        Button btnLoadPokemon = findViewById(R.id.loadPokemon);
        Button btnLoadMovement = findViewById(R.id.loadMovement);
        Button btnListaPiedrasEvo = findViewById(R.id.listapiedrasevo);
        Button btnRefrescarMercadoPiedras = findViewById(R.id.MercadoPiedras);
        Button btnPujasPiedras = findViewById(R.id.PujasPiedras);

        btnLoadPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setText("Wait");
                db.collection("ListaPokemon").orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot: task.getResult()) {
                                pkemonList.add(snapshot.toObject(Pokemon.class));
                            }
                            estado.setText("Ready");
                        }
                    }
                });
            }
        });

        btnLoadMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setText("Wait");
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
                hacerCombate();
            }
        });

        btnRefrescarMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                refrescarMercado();
            }
        });
        btnRefrescarMercadoPiedras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setText("Wait");
                refrescarMercadPiedras();
            }
        });

        btnPujas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estado.setText("Wait");
                asignarPujas();
            }
        });

        btnPujasPiedras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarPujasPiedras();
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

        btnListaPiedrasEvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estado.setText("Wait");
                createEvoStone();
            }
        });
    }

    private void createEvoStone() {
        final ArrayList<PiedrasEvoFirebase> listaPiedras = new ArrayList<>();
        db.collection("ListaPokemon").orderBy("id").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();
                    subirEvoStone(a);

                }
            }
        });
    }

    private void subirEvoStone(Iterator<QueryDocumentSnapshot> a) {
        if (!a.hasNext()){
            for (int i = 0; i < listaPiedras.size(); i++) {
                db.collection("PiedrasEvolucion").add(listaPiedras.get(i));
            }
            estado.setText("Ready");
            return;
        }
        QueryDocumentSnapshot document = a.next();
        Pokemon pokemon = document.toObject(Pokemon.class);
        boolean presente = false;
        for (int i = 0; i < listaPiedras.size(); i++) {
            if (listaPiedras.get(i).getId() == pokemon.getPiedrasEvo().getId()){
                presente = true;
                break;
            }
        }
        if (!presente && pokemon.getPiedrasEvo().getId() != 0){
            listaPiedras.add(new PiedrasEvoFirebase(pokemon.getPiedrasEvo().getId(), pokemon.getName(), pokemon.getSprites().front_default));
        }
        subirEvoStone(a);
    }

    private void hacerCombate() {
        estado.setText("Wait");
        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();
                    consultarCombates(a);
                }

            }
        });
    }

    private void consultarCombates(final Iterator<QueryDocumentSnapshot> a) {
        if(!a.hasNext()){
            estado.setText("Ready");
            return;
        }
        QueryDocumentSnapshot queryDocumentSnapshot = a.next();
        final Partida partida = queryDocumentSnapshot.toObject(Partida.class);

        if (partida.getUsers().size()> 0) {
            if (partida.getUsers().get(0).getCombatesID().size() > 0) {
                final String combateId = partida.getUsers().get(0).getCombatesID().get(partida.getUsers().get(0).getCombatesID().size() - 1);
                db.collection("Combates").document(combateId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Combate combateSacarJornada = task.getResult().toObject(Combate.class);
                            final int jornadaActual = combateSacarJornada.getJornada();
                            db.collection("Combates").whereEqualTo("idGame",partida.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                            Combate combate = snapshot.toObject(Combate.class);
                                            if (combate.getJornada() == jornadaActual) {
                                                combate = lucharCombate(combate);
                                                combate.setFinalizado("si");
                                                db.collection("Combates").document(snapshot.getId()).set(combate);
                                                int cantidad = 0;
                                                for (int i = 0; i <combate.getEquipo1().getAlineacion().getLista().size();  i++) {
                                                    if (combate.getEquipo1().getAlineacion().getLista().get(i)!=null) {
                                                        if (combate.getEquipo1().getAlineacion().getLista().get(i).getLife() > 0) {
                                                            cantidad++;
                                                        }
                                                    }
                                                }
                                                int cantidad2 = 0;
                                                for (int i = 0; i <combate.getEquipo2().getAlineacion().getLista().size();  i++) {
                                                    if (combate.getEquipo2().getAlineacion().getLista().get(i)!= null) {
                                                        if (combate.getEquipo2().getAlineacion().getLista().get(i).getLife() > 0) {
                                                            cantidad2++;
                                                        }
                                                    }
                                                }
                                                for (int i = 0; i < partida.getUsers().size(); i++) {
                                                    if (partida.getUsers().get(i).getUser().getEmail().equals(combate.getEquipo1().getUseremail())){
                                                        if (cantidad>cantidad2){
                                                            partida.getUsers().get(i).setPoints(partida.getUsers().get(i).getPoints()+ 2);
                                                        }
                                                        partida.getUsers().get(i).setPoints(partida.getUsers().get(i).getPoints()+ cantidad);
                                                        partida.getUsers().get(i).setMoney(partida.getUsers().get(i).getMoney()+200+50*cantidad);
                                                    }
                                                    if (partida.getUsers().get(i).getUser().getEmail().equals(combate.getEquipo2().getUseremail())){
                                                        if (cantidad<cantidad2){
                                                            partida.getUsers().get(i).setPoints(partida.getUsers().get(i).getPoints()+ 2);
                                                        }
                                                        partida.getUsers().get(i).setPoints(partida.getUsers().get(i).getPoints() + cantidad2);
                                                        partida.getUsers().get(i).setMoney(partida.getUsers().get(i).getMoney()+200+50*cantidad2);
                                                    }
                                                }
                                                db.collection("Partidas").document(partida.getId()).set(partida);
                                            }
                                        }
                                        consultarCombates(a);
                                    }
                                }
                            });

                        }
                    }
                });
            }
        }
    }

    private Combate lucharCombate(Combate combate) {
        Equipo izquierda = combate.getEquipo1();
        Equipo derecha = combate.getEquipo2();

        for (int i = 0; i < izquierda.getAlineacion().getLista().size(); i++) {
            if (izquierda.getAlineacion().getLista().get(i)!= null && derecha.getAlineacion().getLista().get(i)!= null) {
                Pokemon leftside = izquierda.getAlineacion().getLista().get(i);
                Pokemon rightside = derecha.getAlineacion().getLista().get(i);
                Pokemon atacante;
                Pokemon defensor;
                if (leftside.getStats().get(0).base_stat> rightside.getStats().get(0).base_stat){
                    atacante = leftside;
                    defensor = rightside;
                }else {
                    atacante = rightside;
                    defensor = leftside;
                }
                pokemon1vs1(atacante,defensor);
            }
        }
        return combate;
    }

    private void asignarPujasPiedras(){
        estado.setText("Wait");
        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();
                    consultarPartidasPiedras(a);
                }

            }
        });
    }

    private void consultarPartidasPiedras(Iterator<QueryDocumentSnapshot> a){
        if (!a.hasNext())return;
        Map<Integer,String> idjugadores = new HashMap<>();
        Map<Integer,Integer> pujas = new HashMap<>();

        QueryDocumentSnapshot queryDocumentSnapshot = a.next();
        idPujas = new ArrayList<>();
        for (int i = 0; i<10;i++){
            idjugadores.put(i,"");
            pujas.put(i,0);
        }
        Partida partida = queryDocumentSnapshot.toObject(Partida.class);

        consultarPujasPiedras(partida, 0,idjugadores,pujas);

        consultarPartidasPiedras(a);
    }

    private void consultarPujasPiedras(final Partida partida, final int i, final Map<Integer, String> idjugadores, final Map<Integer, Integer> pujas) {

        if(i>=partida.getUsers().size()) {
            subirDatosPujasPiedras(partida,0,idjugadores,pujas);
            return;
        }

        idPujas.add(partida.getUsers().get(i).getTeamID());
        /**
         * Cambia el nombre de la ruta de firestore
         * Y el modelo de datos del task.getResult()
         */
        db.collection("PujasPiedras").document(partida.getUsers().get(i).getPujasPiedras()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    PujasPiedras listaPujas = task.getResult().toObject(PujasPiedras.class);
                    for (int j =0;j<listaPujas.getPujas().size();j++){
                        if (pujas.get(j) < listaPujas.getPujas().get(j)){
                            idjugadores.put(j,task.getResult().getId());
                            pujas.put(j,listaPujas.getPujas().get(j));

                        }
                    }
                    consultarPujasPiedras(partida, i+1, idjugadores, pujas);
                    Log.e("Pujas",""+i);
                    PujasPiedras pujasAZero = new PujasPiedras();
                    db.collection("PujasPiedras").document(partida.getUsers().get(i).getPujasPiedras()).set(pujasAZero);
                }
            }
        });

    }

    private void subirDatosPujasPiedras(final Partida partida, final int i, final Map<Integer, String> idjugadores, final Map<Integer, Integer> pujas) {
        if (i>=idjugadores.size()){
            refrescarMercadPiedras();
            estado.setText("Done");
            return;
        }
        /**
         * hay que cambiar el nombre de las rutas de firestore y las variables de modelo de datos
         */
        if (pujas.get(i)>0){
            for (int j = 0; j < partida.getUsers().size(); j++) {
                if (partida.getUsers().get(j).getPujasPiedras().equals(idjugadores.get(i))){
                    final String idTeamJugador = partida.getUsers().get(j).getObjetosID();
                    final int piedraPos = i;
                    final int posJugadorPartida = j;
                    db.collection("PiedrasUser").document(idTeamJugador).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final PiedrasUser team = task.getResult().toObject(PiedrasUser.class);
                                Log.e("Piedra", ""+team.getPiedras().size());
                                db.collection("PiedrasMercado").document(partida.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            ListaPujasPiedras mercado = task.getResult().toObject(ListaPujasPiedras.class);
                                            boolean existepiedra = false;
                                            for (int i = 0; i < team.getPiedras().size(); i++) {
                                                if (team.getPiedras().get(i).getId()== mercado.getLista().get(piedraPos).getId()){
                                                    team.getPiedras().get(i).setCantidad(team.getPiedras().get(i).getCantidad()+mercado.getLista().get(piedraPos).getCantidad());
                                                    existepiedra = true;
                                                }
                                            }
                                            if (!existepiedra){
                                                team.getPiedras().add(mercado.getLista().get(piedraPos));
                                            }
                                            Log.e("Piedra", idTeamJugador);

                                            db.collection("PiedrasUser").document(idTeamJugador).update("piedras", team.getPiedras());

                                            partida.getUsers().get(posJugadorPartida).setMoney(partida.getUsers().get(posJugadorPartida).getMoney()-pujas.get(i));
                                            db.collection("Partidas").document(partida.getId()).set(partida);


                                            subirDatosPujasPiedras(partida,i+1,idjugadores,pujas);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

        }else subirDatosPujasPiedras(partida,i+1,idjugadores,pujas);

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
        if (pujas.get(i)>0){
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

        idPujas.add(partida.getUsers().get(i).getPujasID());

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
                    if (task.isSuccessful()) {
                        int i = 0;
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
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

    private void refrescarMercadPiedras() {

        db.collection("Partidas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()) {
                        hacerListaPiedras(snapshot.getId());

                    }
                }
            }
        });
    }

    private void hacerListaPiedras(final String id){
        final Random random = new Random();
        final ArrayList<String> totalIDS = new ArrayList<>();
        final ArrayList<String> idsFinales = new ArrayList<>();
        listaPiedrasMercado = new ArrayList<>();


        db.collection("PiedrasEvolucion").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot snapshot: task.getResult()){
                            totalIDS.add(snapshot.getId());
                        }

                        for (int i = 0; i < 10; i++) {
                            int position = random.nextInt(totalIDS.size());
                            idsFinales.add(totalIDS.get(position));
                        }

                        randomPiedras(idsFinales, 0, id);

                    }
            }
        });
    }

    private void randomPiedras(final ArrayList<String> idsFinales, final int i, final String id) {
        final Random random = new Random();

        if (i == idsFinales.size()){
            ListaPujasPiedras lista = new ListaPujasPiedras();
            lista.setLista(listaPiedrasMercado);
            db.collection("PiedrasMercado").document(id).set(lista);
            estado.setText("Ready");
            return;
        }

        db.collection("PiedrasEvolucion").document(idsFinales.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    PiedrasEvoFirebase piedrasEvoFirebase = documentSnapshot.toObject(PiedrasEvoFirebase.class);
                    int cantidad = random.nextInt(3) + 1;
                    listaPiedrasMercado.add(new PiedrasEvoUser(piedrasEvoFirebase.getId(), piedrasEvoFirebase.getName(), cantidad, piedrasEvoFirebase.getSprite()));

                    randomPiedras(idsFinales, i+1, id);
                }
            }
        });
    }


    private void pokemon1vs1(Pokemon atacante, Pokemon defensor) {
        Random random = new Random();
        int idMovAtk = atacante.getMoves().get(random.nextInt(atacante.getMoves().size())).move.id;
        int danoAtk = movementList.get(idMovAtk).power;
        double stab = 1;
        for (int i = 0; i < atacante.getTypes().size(); i++) {
            if (atacante.getTypes().get(i).getType().getName().equals(movementList.get(idMovAtk).type)){
                stab = 1.5;
            }
        }
        int statAtk = atacante.getStats().get(2).base_stat;
        int statDef = defensor.getStats().get(1).base_stat;
        int variable = random.nextInt(17)+84;

        int dano = (int) (0.01*variable*((11*statAtk*danoAtk)/(25*statDef))*stab);
        defensor.setLife(defensor.getLife()-dano);


        if (atacante.getLife()>0 && defensor.getLife()>0){
            pokemon1vs1(defensor,atacante);
        }else {
            defensor.setLife(0);

        }
    }


    private List<Pokemon> hacerListaMercado() {
        int cantidad = 0;
        Random random = new Random();
        int[] idCogidos = new int[10];
        boolean igualdad = false;
        do {
            igualdad = false;
            for (int i = 0; i < idCogidos.length; i++) {
                idCogidos[i] = random.nextInt(151);
            }

            for (int i = 0; i < idCogidos.length; i++) {
                cantidad += pkemonList.get(idCogidos[i]).getPrice();
            }
            for (int i = 0; i < idCogidos.length; i++) {
                for (int j = idCogidos.length-1; j >i ; j--) {
                    if (idCogidos[i]==idCogidos[j]){
                        igualdad= true;
                    }
                }
            }
        }while (cantidad>18000 || igualdad);
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
            Log.e("Pokemon", pokemon.getName());
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
                    Iterator<QueryDocumentSnapshot> a = task.getResult().iterator();

                    sacarPartidaParaCombate(a,0);
                }
            }
        });


    }

    private void sacarPartidaParaCombate(final Iterator<QueryDocumentSnapshot> a, final int i) {
        if (!a.hasNext())return;

        calculojornada = false;
        jornada = 0;
        QueryDocumentSnapshot snapshot = a.next();

        db.collection("Partidas").document(snapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    partida = snapshot.toObject(Partida.class);
                    sacarAlineacion(partida,0,partida.getUsers().size()-1,0);
                }
                sacarPartidaParaCombate(a,i+1);
            }
        });
    }

    private void sacarAlineacion(final Partida partida, final int primerJugador, final int segundoJguador, int limite) {
        if (segundoJguador<=primerJugador){
            limite++;
            if (limite>=partida.getUsers().size()-1){
                estado.setText("Ready");
            }else {
                sacarAlineacion(partida, limite, partida.getUsers().size() - 1, limite);
            }
        }else {
            final int finalLimite = limite;
            db.collection("Alineaciones").document(partida.getUsers().get(primerJugador).getAlineationID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        alineation1 = documentSnapshot.toObject(Alineation.class);
                        db.collection("Alineaciones").document(partida.getUsers().get(segundoJguador).getAlineationID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    final String idCombate = db.collection("Combates").document().getId();

                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    alineation2 = documentSnapshot.toObject(Alineation.class);
                                    partida.getUsers().get(primerJugador).getCombatesID().add(idCombate);
                                    partida.getUsers().get(segundoJguador).getCombatesID().add(idCombate);
                                    db.collection("Partidas").document(partida.getId()).set(partida);
                                    db.collection("Combates").whereEqualTo("idGame", partida.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
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
                                                Equipo equipo1 = new Equipo(partida.getUsers().get(primerJugador).getUser().getUsername(), partida.getUsers().get(primerJugador).getUser().getEmail(), alineation1, partida.getUsers().get(primerJugador).getUser().getImgurl());
                                                Equipo equipo2 = new Equipo(partida.getUsers().get(segundoJguador).getUser().getUsername(), partida.getUsers().get(segundoJguador).getUser().getEmail(), alineation2, partida.getUsers().get(segundoJguador).getUser().getImgurl());
                                                Combate nextCombate = new Combate(equipo1, equipo2, jornada, partida.getId());
                                                db.collection("Combates").document(idCombate).set(nextCombate);
                                                sacarAlineacion(partida, primerJugador, segundoJguador - 1, finalLimite);
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
                                    pokemon.setPiedrasEvo(new PiedraEvo(1,5));
                                    break;
                                case 2:
                                    pokemon.setIdEvo(3);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(1, 10));
                                    break;
                                case 3:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0,0));
                                    break;
                                case 4:
                                    pokemon.setIdEvo(5);
                                    pokemon.setPrice(350);
                                    pokemon.setPiedrasEvo(new PiedraEvo(4, 5));
                                    break;
                                case 5:
                                    pokemon.setIdEvo(6);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(4, 10));
                                    break;
                                case 6:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 7:
                                    pokemon.setIdEvo(8);
                                    pokemon.setPrice(350);
                                    pokemon.setPiedrasEvo(new PiedraEvo(7, 5));
                                    break;
                                case 8:
                                    pokemon.setIdEvo(9);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(7, 10));
                                    break;
                                case 9:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 10:
                                    pokemon.setIdEvo(11);
                                    pokemon.setPrice(150);
                                    pokemon.setPiedrasEvo(new PiedraEvo(10, 1));
                                    break;
                                case 11:
                                    pokemon.setIdEvo(12);
                                    pokemon.setPrice(300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(10, 3));
                                    break;
                                case 12:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 13:
                                    pokemon.setIdEvo(14);
                                    pokemon.setPrice(150);
                                    pokemon.setPiedrasEvo(new PiedraEvo(13, 1));
                                    break;
                                case 14:
                                    pokemon.setIdEvo(15);
                                    pokemon.setPrice(300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(13,3));
                                    break;
                                case 15:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 16:
                                    pokemon.setIdEvo(17);
                                    pokemon.setPrice(250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(16,3));
                                    break;
                                case 17:
                                    pokemon.setIdEvo(18);
                                    pokemon.setPrice(900);
                                    pokemon.setPiedrasEvo(new PiedraEvo(16, 7));
                                    break;
                                case 18:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 19:
                                    pokemon.setIdEvo(20);
                                    pokemon.setPrice(90);
                                    pokemon.setPiedrasEvo(new PiedraEvo(19, 5));
                                    break;
                                case 20:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 21:
                                    pokemon.setIdEvo(22);
                                    pokemon.setPrice(200);
                                    pokemon.setPiedrasEvo(new PiedraEvo(21, 5));
                                    break;
                                case 22:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 23:
                                    pokemon.setIdEvo(24);
                                    pokemon.setPrice(400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(23, 5));
                                    break;
                                case 24:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1850);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 25:
                                    pokemon.setIdEvo(26);
                                    pokemon.setPrice(750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(25, 5));
                                    break;
                                case 26:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 27:
                                    pokemon.setIdEvo(28);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(27, 5));
                                    break;
                                case 28:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 29:
                                    pokemon.setIdEvo(30);
                                    pokemon.setPrice(400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(29, 3));
                                    break;
                                case 30:
                                    pokemon.setIdEvo(31);
                                    pokemon.setPrice(975);
                                    pokemon.setPiedrasEvo(new PiedraEvo(29, 7));
                                    break;
                                case 31:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 32:
                                    pokemon.setIdEvo(33);
                                    pokemon.setPrice(400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(32, 3));
                                    break;
                                case 33:
                                    pokemon.setIdEvo(34);
                                    pokemon.setPrice(975);
                                    pokemon.setPiedrasEvo(new PiedraEvo(32, 7));
                                    break;
                                case 34:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 35:
                                    pokemon.setIdEvo(36);
                                    pokemon.setPrice(850);
                                    pokemon.setPiedrasEvo(new PiedraEvo(35, 5));
                                    break;
                                case 36:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 37:
                                    pokemon.setIdEvo(38);
                                    pokemon.setPrice(600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(37, 5));
                                    break;
                                case 38:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 39:
                                    pokemon.setIdEvo(40);
                                    pokemon.setPrice(850);
                                    pokemon.setPiedrasEvo(new PiedraEvo(39, 5));
                                    break;
                                case 40:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 41:
                                    pokemon.setIdEvo(42);
                                    pokemon.setPrice(165);
                                    pokemon.setPiedrasEvo(new PiedraEvo(41, 5));
                                    break;
                                case 42:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2225);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 43:
                                    pokemon.setIdEvo(44);
                                    pokemon.setPrice(160);
                                    pokemon.setPiedrasEvo(new PiedraEvo(43, 3));
                                    break;
                                case 44:
                                    pokemon.setIdEvo(45);
                                    pokemon.setPrice(900);
                                    pokemon.setPiedrasEvo(new PiedraEvo(43, 7));
                                    break;
                                case 45:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 46:
                                    pokemon.setIdEvo(47);
                                    pokemon.setPrice(250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(46, 5));
                                    break;
                                case 47:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 48:
                                    pokemon.setIdEvo(49);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(48, 5));
                                    break;
                                case 49:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 50:
                                    pokemon.setIdEvo(51);
                                    pokemon.setPrice(700);
                                    pokemon.setPiedrasEvo(new PiedraEvo(50, 5));
                                    break;
                                case 51:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 52:
                                    pokemon.setIdEvo(53);
                                    pokemon.setPrice(650);
                                    pokemon.setPiedrasEvo(new PiedraEvo(52, 5));
                                    break;
                                case 53:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 54:
                                    pokemon.setIdEvo(55);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(54, 5));
                                    break;
                                case 55:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 56:
                                    pokemon.setIdEvo(57);
                                    pokemon.setPrice(800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(56, 5));
                                    break;
                                case 57:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 58:
                                    pokemon.setIdEvo(59);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(58, 5));
                                    break;
                                case 59:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 60:
                                    pokemon.setIdEvo(61);
                                    pokemon.setPrice(450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(60, 3));
                                    break;
                                case 61:
                                    pokemon.setIdEvo(62);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(60, 7));
                                    break;
                                case 62:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 63:
                                    pokemon.setIdEvo(64);
                                    pokemon.setPrice(400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(63, 3));
                                    break;
                                case 64:
                                    pokemon.setIdEvo(65);
                                    pokemon.setPrice(1600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(63, 10));
                                    break;
                                case 65:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(5000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 66:
                                    pokemon.setIdEvo(67);
                                    pokemon.setPrice(450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(66,3));
                                    break;
                                case 67:
                                    pokemon.setIdEvo(68);
                                    pokemon.setPrice(1400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(66, 10));
                                    break;
                                case 68:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 69:
                                    pokemon.setIdEvo(70);
                                    pokemon.setPrice(300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(69,3));
                                    break;
                                case 70:
                                    pokemon.setIdEvo(71);
                                    pokemon.setPrice(975);
                                    pokemon.setPiedrasEvo(new PiedraEvo(69, 7));
                                    break;
                                case 71:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2150);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0,0));
                                    break;
                                case 72:
                                    pokemon.setIdEvo(73);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(72,5));
                                    break;
                                case 73:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0,0));
                                    break;
                                case 74:
                                    pokemon.setIdEvo(75);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(74, 3));
                                    break;
                                case 75:
                                    pokemon.setIdEvo(76);
                                    pokemon.setPrice(1500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(74, 10));
                                    break;
                                case 76:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4375);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0,0));
                                    break;
                                case 77:
                                    pokemon.setIdEvo(78);
                                    pokemon.setPrice(950);
                                    pokemon.setPiedrasEvo(new PiedraEvo(77, 5));
                                    break;
                                case 78:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0,0));
                                    break;
                                case 79:
                                    pokemon.setIdEvo(80);
                                    pokemon.setPrice(300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(79, 5));
                                    break;
                                case 80:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 81:
                                    pokemon.setIdEvo(82);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(81, 5));
                                    break;
                                case 82:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2200);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 83:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 84:
                                    pokemon.setIdEvo(85);
                                    pokemon.setPrice(750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(84, 5));
                                    break;
                                case 85:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 86:
                                    pokemon.setIdEvo(87);
                                    pokemon.setPrice(900);
                                    pokemon.setPiedrasEvo(new PiedraEvo(86, 5));
                                    break;
                                case 87:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 88:
                                    pokemon.setIdEvo(89);
                                    pokemon.setPrice(700);
                                    pokemon.setPiedrasEvo(new PiedraEvo(88, 5));
                                    break;
                                case 89:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1850);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 90:
                                    pokemon.setIdEvo(91);
                                    pokemon.setPrice(650);
                                    pokemon.setPiedrasEvo(new PiedraEvo(90, 5));
                                    break;
                                case 91:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1900);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 92:
                                    pokemon.setIdEvo(93);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(92, 3));
                                    break;
                                case 93:
                                    pokemon.setIdEvo(94);
                                    pokemon.setPrice(1500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(92, 10));
                                    break;
                                case 94:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 95:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 96:
                                    pokemon.setIdEvo(97);
                                    pokemon.setPrice(600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(96, 5));
                                    break;
                                case 97:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 98:
                                    pokemon.setIdEvo(99);
                                    pokemon.setPrice(550);
                                    pokemon.setPiedrasEvo(new PiedraEvo(98, 5));
                                    break;
                                case 99:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 100:
                                    pokemon.setIdEvo(101);
                                    pokemon.setPrice(800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(100, 5));
                                    break;
                                case 101:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 102:
                                    pokemon.setIdEvo(103);
                                    pokemon.setPrice(1450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(102, 5));
                                    break;
                                case 103:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 104:
                                    pokemon.setIdEvo(105);
                                    pokemon.setPrice(500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(104, 5));
                                    break;
                                case 105:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1700);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 106:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 107:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 108:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 109:
                                    pokemon.setIdEvo(110);
                                    pokemon.setPrice(600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(109, 5));
                                    break;
                                case 110:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 111:
                                    pokemon.setIdEvo(112);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(111, 5));
                                    break;
                                case 112:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(3500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 113:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 114:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 115:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 116:
                                    pokemon.setIdEvo(117);
                                    pokemon.setPrice(800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(116, 5));
                                    break;
                                case 117:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1650);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 118:
                                    pokemon.setIdEvo(119);
                                    pokemon.setPrice(700);
                                    pokemon.setPiedrasEvo(new PiedraEvo(118, 5));
                                    break;
                                case 119:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1450);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 120:
                                    pokemon.setIdEvo(121);
                                    pokemon.setPrice(1150);
                                    pokemon.setPiedrasEvo(new PiedraEvo(120, 5));
                                    break;
                                case 121:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 122:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1700);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 123:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1850);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 124:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 125:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2100);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 126:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2100);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 127:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 128:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2150);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 129:
                                    pokemon.setIdEvo(130);
                                    pokemon.setPrice(50);
                                    pokemon.setPiedrasEvo(new PiedraEvo(129, 15));
                                    break;
                                case 130:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4750);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 131:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2300);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 132:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1250);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 133:
                                    pokemon.setIdEvo(134135136);
                                    pokemon.setPrice(800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(133, 5));
                                    break;
                                case 134:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 135:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 136:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 137:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1600);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 138:
                                    pokemon.setIdEvo(139);
                                    pokemon.setPrice(950);
                                    pokemon.setPiedrasEvo(new PiedraEvo(138, 5));
                                    break;
                                case 139:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 140:
                                    pokemon.setIdEvo(141);
                                    pokemon.setPrice(950);
                                    pokemon.setPiedrasEvo(new PiedraEvo(140, 5));
                                    break;
                                case 141:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(1800);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 142:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(2500);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 143:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(4400);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 144:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(10000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 145:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(10000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 146:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(10000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 147:
                                    pokemon.setIdEvo(148);
                                    pokemon.setPrice(1000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(147, 5));
                                    break;
                                case 148:
                                    pokemon.setIdEvo(149);
                                    pokemon.setPrice(3000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(147, 15));
                                    break;
                                case 149:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(7000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 150:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(12000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
                                    break;
                                case 151:
                                    pokemon.setIdEvo(0);
                                    pokemon.setPrice(8000);
                                    pokemon.setPiedrasEvo(new PiedraEvo(0, 0));
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

    public void readApiMovements() {
            for (int i = 1; i < 729; i++) {
                pokemonApi.getMovement(i).enqueue(new Callback<Movement>() {
                    @Override
                    public void onResponse(Call<Movement> call, final Response<Movement> response) {
                        executor2.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (response.body() != null) {
                                    Movement movement = new Movement(response.body().id, response.body().name, response.body().names, response.body().power, response.body().pp, response.body().accuracy, response.body().priority, response.body().type, response.body().getDamage_class());
                                    String nombre = "";
                                    for (int j = 0; j < movement.getNames().size(); j++) {
                                        if (movement.getNames().get(j).language.name.equals("es")) {
                                            nombre = movement.getNames().get(j).name;
                                        }
                                    }
                                    if (response.body().power == null) {
                                        movement.power = "0";
                                    }
                                    MovementFirebase movementFirebase = new MovementFirebase(movement.getId(), movement.getAccuracy(), Integer.parseInt(movement.getPower()), movement.getPp(), movement.getPriority(), movement.getType().getName(), nombre, movement.getDamage_class().name);
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

