package com.thaisbl.desafio4.user.register

import com.thaisbl.desafio4.shared.extension.isValidEmailAddress
import com.thaisbl.desafio4.shared.model.data.Response

class RegisterBusiness {

    private val repository by lazy {
        RegisterRepository()
    }

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Response {
        if (name.isBlank()) {
            return Response.Failure("Name field is required")
        }

        if (email.isBlank()) {
            return Response.Failure("E-mail field is required")
        }
        if (!email.isValidEmailAddress()) {
            return Response.Failure("Email is not valid")
        }

        if (password.isBlank()) {
            return Response.Failure("Password field is required")
        }
        if (repeatPassword.isBlank()) {
            return Response.Failure("Repeat Password field is required")
        }
        if (password != repeatPassword) {
            return Response.Failure("Password and Repeat Password does not match")
        }

        return repository.registerUser(name.trim(), email.trim(), password)
    }

}