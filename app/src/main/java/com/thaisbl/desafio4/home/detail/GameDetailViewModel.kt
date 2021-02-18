package com.thaisbl.desafio4.home.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.thaisbl.desafio4.home.Game
import com.thaisbl.desafio4.shared.model.data.Response

class GameDetailViewModel(application: Application) : AndroidViewModel(application) {

    val onGameDataResultSuccess: MutableLiveData<Game> = MutableLiveData()
    val onGameDataResultFailure: MutableLiveData<String> = MutableLiveData()

    private val business by lazy {
        GameDetailBusiness()
    }

    fun getGameData(gameUid: String) {
        viewModelScope.launch {
            when (val response = business.getGameData(gameUid)) {
                is Response.Success -> {
                    onGameDataResultSuccess.postValue(response.data as Game)
                }
                is Response.Failure -> {
                    onGameDataResultFailure.postValue(response.error)
                }
            }
        }
    }

}