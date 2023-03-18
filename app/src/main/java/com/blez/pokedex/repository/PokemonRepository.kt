package com.blez.pokedex.repository

import com.blez.pokedex.data.api.PokemonAPI
import com.blez.pokedex.data.model.PokemonData
import com.blez.pokedex.data.model.pokemons
import com.blez.pokedex.util.ResultState
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository(val api : PokemonAPI) {

    suspend fun getPokemons(limit : Int, offset : Int) : ResultState<pokemons>{
        return try {

            val data = api.getPokemons(limit, offset)
            if (data.isSuccessful){
                ResultState.Success(data.body())
            }
            else
                ResultState.Failure(data.errorBody().toString())
        }catch (e : Exception){
            ResultState.Failure(message = e.message)
        }
    }


    suspend fun getPokemonData(name : String) : ResultState<PokemonData>{
        return try {
            val data = api.getPokemonInfo(name)
            if (data.isSuccessful)
                ResultState.Success(data.body())
            else
                ResultState.Failure(data.errorBody().toString())
        }catch (e: Exception){
            ResultState.Failure(e.message)
        }

    }
}