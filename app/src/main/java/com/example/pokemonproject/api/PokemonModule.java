package com.example.pokemonproject.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonModule {
    static PokemonApi pokemonApi;

    public static PokemonApi getAPI(){
        if(pokemonApi == null){
            final OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            pokemonApi = retrofit.create(PokemonApi.class);
        }
        return pokemonApi;
    }
}
