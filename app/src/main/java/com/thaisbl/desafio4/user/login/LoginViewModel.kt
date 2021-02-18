package com.thaisbl.desafio4.user.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.thaisbl.desafio4.shared.model.data.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val onSignInUserSuccess: MutableLiveData<String> = MutableLiveData()
    val onSignInUserFailure: MutableLiveData<String> = MutableLiveData()

    val onGetSavedLoginCredentialsSuccess: MutableLiveData<String> = MutableLiveData()
    val onGetSavedLoginCredentialsFailure: MutableLiveData<String> = MutableLiveData()

    val onSaveLoginCredentialsSuccess: MutableLiveData<Any> = MutableLiveData()
    val onSaveLoginCredentialsFailure: MutableLiveData<String> = MutableLiveData()

    val onDeleteSavedLoginCredentialsSuccess: MutableLiveData<Any> = MutableLiveData()
    val onDeleteSavedLoginCredentialsFailure: MutableLiveData<String> = MutableLiveData()

    private val business by lazy {
        LoginBusiness(application.applicationContext)
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            when (val response = business.signInUser(email, password)) {
                is Response.Success -> {
                    onSignInUserSuccess.postValue(response.data as? String)
                }
                is Response.Failure -> {
                    onSignInUserFailure.postValue(response.error)
                }
            }
        }
    }

    fun getSavedLoginCredentials() {
        when (val response = business.getSavedLoginCredentials()) {
            is Response.Success -> {
                onGetSavedLoginCredentialsSuccess.postValue(response.data as? String)
            }
            is Response.Failure -> {
                onGetSavedLoginCredentialsFailure.postValue(response.error)
            }
        }
    }

    fun saveLoginCredentials(email: String) {
        when (val response = business.saveLoginCredentials(email)) {
            is Response.Success -> {
                onSaveLoginCredentialsSuccess.postValue(response.data)
            }
            is Response.Failure -> {
                onSaveLoginCredentialsFailure.postValue(response.error)
            }
        }
    }

    fun deleteSavedLoginCredentials() {
        when (val response = business.deleteSavedLoginCredentials()) {
            is Response.Success -> {
                onDeleteSavedLoginCredentialsSuccess.postValue(response.data)
            }
            is Response.Failure -> {
                onDeleteSavedLoginCredentialsFailure.postValue(response.error)
            }
        }
    }

}