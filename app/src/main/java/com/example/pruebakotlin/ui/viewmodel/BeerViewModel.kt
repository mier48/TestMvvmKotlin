package com.example.pruebakotlin.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebakotlin.domain.*
import com.example.pruebakotlin.domain.model.Beer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
    private val getBeerById: GetBeerById,
    private val addFavorite: AddFavorite,
    private val removeFavorite: RemoveFavorite,
    private val getFavoritesBeerById: GetFavoritesBeerById
) : ViewModel() {

    var beerListModel = MutableLiveData<List<Beer>>()
    var beerModel = MutableLiveData<Beer>()
    val isLoading = MutableLiveData<Boolean>()
    private var allItems: List<Beer> = ArrayList()

    fun onCreate(id: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)

            val result = getBeerById(id)

            if (result != null) {
                beerModel.postValue(result!!)
                isLoading.postValue(false)
            }
        }
    }

    fun favoriteBeer(id: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)

            val result = getFavoritesBeerById(id)

            beerModel.postValue(result)
            isLoading.postValue(false)
        }
    }

    fun addToFavorite(beer: Beer) {
        viewModelScope.launch {
            addFavorite.invoke(beer)

            for (i in allItems) {
                if (i.id == beer.id) {
                    i.fav = true
                }
            }

            beerListModel.postValue(allItems)
        }
    }

    fun deleteFavorite(beer: Beer) {
        viewModelScope.launch {
            removeFavorite.invoke(beer)

            for (i in allItems) {
                if (i.id == beer.id) {
                    i.fav = false
                }
            }

            beerListModel.postValue(allItems)
        }
    }
}