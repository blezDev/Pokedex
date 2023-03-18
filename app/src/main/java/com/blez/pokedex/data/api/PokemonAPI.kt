package com.blez.pokedex.data.api

import com.blez.pokedex.data.model.PokemonData
import com.blez.pokedex.data.model.pokemons
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPokemons(@Query("limit") limit : Int = 20,@Query("offset") offset : Int) : Response<pokemons>

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(@Path("name") name : String) : Response<PokemonData>
}