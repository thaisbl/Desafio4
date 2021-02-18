package com.thaisbl.desafio4.user.splash

import com.thaisbl.desafio4.shared.model.data.Response

class SplashBusiness {

    private val repository by lazy {
        SplashRepository()
    }

    fun isUserSignedIn(): Response {
        return repository.isUserSignedIn()
    }

}