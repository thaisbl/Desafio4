package com.thaisbl.desafio4.user.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.thaisbl.desafio4.shared.model.data.Response

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    // firebase registration methods does not have return
    val onRegisterUserSuccess: MutableLiveData<Any> = MutableLiveData()
    val onRegisterUserFailure: MutableLiveData<String> = MutableLiveData()

    private val business by lazy {
        RegisterBusiness()
    }

    fun registerUser(name: String, email: String, password: String, repeatPassword: String) {
        viewModelScope.launch {
            when (val response = business.registerUser(name, email, password, repeatPassword)) {
                is Response.Success -> {
                    onRegisterUserSuccess.postValue(response.data)
                }
                is Response.Failure -> {
                    onRegisterUserFailure.postValue(response.error)
                }
            }
        }
    }

}