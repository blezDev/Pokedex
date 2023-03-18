package com.blez.pokedex.navigation.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.blez.pokedex.data.model.model.PokedexListEntry
import com.blez.pokedex.repository.PokemonRepository
import com.blez.pokedex.util.Constants.PAGE_SIZE
import com.blez.pokedex.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val repository: PokemonRepository) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)


    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated(){
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.getPokemons(PAGE_SIZE,curPage * PAGE_SIZE)
            when(result){
                is ResultState.Success->{
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile {
                              it.isDigit()
                            }
                        }else{
                            entry.url.takeLastWhile {
                                it.isDigit()
                            }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.uppercase(Locale.ROOT), url, number.toInt())

                    }
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }
                is ResultState.Failure->{
                    loadError.value = result.message!!
                    isLoading.value = false

                }

                else->{
                    Unit
                }
            }
        }
    }

    fun calcDominantColor(drawable : Drawable,onFinish : (Color)-> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.from(bitmap).generate { palette->
            palette?.dominantSwatch?.rgb?.let {color->
            onFinish(Color(color))

            }
        }

    }
}