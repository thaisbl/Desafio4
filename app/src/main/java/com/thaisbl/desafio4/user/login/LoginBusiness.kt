package com.thaisbl.desafio4.user.login

import android.content.Context
import com.thaisbl.desafio4.shared.extension.isValidEmailAddress
import com.thaisbl.desafio4.shared.model.data.Response

class LoginBusiness(context: Context) {

    private val repository by lazy {
        LoginRepository(context)
    }

    fun getSavedLoginCredentials(): Response {
        return repository.getSavedLoginCredentials()
    }

    fun saveLoginCredentials(email: String): Response {
        return repository.saveLoginCredentials(email.trim())
    }

    fun deleteSavedLoginCredentials(): Response {
        return repository.deleteSavedLoginCredentials()
    }

    suspend fun signInUser(email: String, password: String): Response {
        if (email.isBlank()) {
            return Response.Failure("E-mail field is required")
        }
        if (!email.isValidEmailAddress()) {
            return Response.Failure("Email is not valid")
        }

        if (password.isBlank()) {
            return Response.Failure("Password field is required")
        }

        return repository.signInUser(email.trim(), password)
    }

}