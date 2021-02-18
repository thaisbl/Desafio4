package com.thaisbl.desafio4.user.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.thaisbl.desafio4.shared.model.data.Response

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    val onIsUserSignedInSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val onIsUserSignedInFailure: MutableLiveData<String> = MutableLiveData()

    private val business by lazy {
        SplashBusiness()
    }

    fun isUserSignedIn() {
        viewModelScope.launch {
            when (val response = business.isUserSignedIn()) {
                is Response.Success -> {
                    onIsUserSignedInSuccess.postValue(response.data as? Boolean)
                }
                is Response.Failure -> {
                    onIsUserSignedInFailure.postValue(response.error)
                }
            }
        }
    }

}