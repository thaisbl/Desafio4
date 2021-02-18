package com.thaisbl.desafio4.home.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.thaisbl.desafio4.home.Game
import com.thaisbl.desafio4.shared.model.data.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val onFetchUserGamesListSuccess: MutableLiveData<List<Game>> = MutableLiveData()
    val onFetchUserGamesListFailure: MutableLiveData<String> = MutableLiveData()

    val onSignOutUserSuccess: MutableLiveData<Any> = MutableLiveData()
    val onSignOutUserFailure: MutableLiveData<String> = MutableLiveData()

    private val homeBusiness by lazy {
        HomeBusiness()
    }

    fun getUserGamesList() {
        viewModelScope.launch {
            when (val response = homeBusiness.getUserGamesList()) {
                is Response.Success -> {
                    val gamesList = (response.data as? MutableList<*>)
                    onFetchUserGamesListSuccess.postValue(gamesList?.filterIsInstance<Game>())
                }
                is Response.Failure -> {
                    onFetchUserGamesListFailure.postValue(response.error)
                }
            }
        }
    }

    fun filterUserGamesList(text: String) {
        viewModelScope.launch {
            when (val response = homeBusiness.filterUserGamesList(text)) {
                is Response.Success -> {
                    val gamesList = (response.data as? MutableList<*>)
                    onFetchUserGamesListSuccess.postValue(gamesList?.filterIsInstance<Game>())
                }
                is Response.Failure -> {
                    onFetchUserGamesListFailure.postValue(response.error)
                }
            }
        }
    }

    fun signOutUser() {
        when (val response = homeBusiness.signOutUser()) {
            is Response.Success -> {
                onSignOutUserSuccess.postValue(response.data)
            }
            is Response.Failure -> {
                onSignOutUserFailure.postValue(response.error)
            }
        }
    }

}