package com.example.pokemonproject.api;


import com.example.pokemonproject.model.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PokemonApi {

    @GET("pokemon/{id}/")
    Call<Pokemon> getPokemon(@Path("id") int id);

}
