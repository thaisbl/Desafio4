package com.thaisbl.desafio4.home.edit

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.thaisbl.desafio4.home.Game
import com.thaisbl.desafio4.shared.model.data.Response

class EditGameViewModel(application: Application) : AndroidViewModel(application) {

    val onStoreGameResultSuccess: MutableLiveData<Game> = MutableLiveData()
    val onStoreGameResultFailure: MutableLiveData<String> = MutableLiveData()

    private val business by lazy {
        EditGameBusiness()
    }

    fun editGame(gameUid: String, name: String, year: String, description: String, img: Bitmap?) {
        viewModelScope.launch {
            when (val response = business.editGame(gameUid, name, year, description, img)) {
                is Response.Success -> {
                    onStoreGameResultSuccess.postValue(response.data as Game)
                }
                is Response.Failure -> {
                    onStoreGameResultFailure.postValue(response.error)
                }
            }
        }
    }

    fun saveGame(name: String, year: String, description: String, img: Bitmap?) {
        viewModelScope.launch {
            when (val response = business.saveGame(name, year, description, img)) {
                is Response.Success -> {
                    onStoreGameResultSuccess.postValue(response.data as Game)
                }
                is Response.Failure -> {
                    onStoreGameResultFailure.postValue(response.error)
                }
            }
        }
    }

}