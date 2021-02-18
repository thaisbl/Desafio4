package com.thaisbl.desafio4.home.home

import android.util.Log
import com.thaisbl.desafio4.home.Game
import com.thaisbl.desafio4.shared.model.data.Response
import com.thaisbl.desafio4.shared.model.repository.GameRepository
import java.util.*

class HomeBusiness {

    private val gameRepository by lazy {
        GameRepository()
    }
    private val homeRepository by lazy {
        HomeRepository()
    }

    fun signOutUser(): Response {
        return homeRepository.signOutUser()
    }

    suspend fun getUserGamesList(): Response {
        return when (val gamesListResponse = homeRepository.getUserGamesList()) {
            is Response.Success -> {
                var gamesList =
                    (gamesListResponse.data as? MutableList<*>)?.filterIsInstance(Game::class.java)

                gamesList = gamesList?.map { game ->
                    // get downloadable url of image from firestore
                    game.imagePath?.let { imagePath ->
                        when (val imgGameResponse =
                            gameRepository.getGameImageStorageUrl(imagePath)) {
                            is Response.Success -> {
                                game.mImageStoragePath = imgGameResponse.data as? String
                            }
                            is Response.Failure -> {
                                Log.e("NMCJ", "Image from ${game.title} does not exist!")
                            }
                        }
                    }

                    return@map game
                }

                Response.Success(gamesList)
            }
            is Response.Failure -> {
                gamesListResponse
            }
        }
    }

    suspend fun filterUserGamesList(text: String): Response {
        return when (val gamesListResponse = getUserGamesList()) {
            is Response.Success -> {
                var gamesList =
                    (gamesListResponse.data as? MutableList<*>)?.filterIsInstance(Game::class.java)

                gamesList = gamesList?.filter {
                    it.title
                        .toLowerCase(Locale.ROOT)
                        .startsWith(text.toLowerCase(Locale.ROOT))
                }

                Response.Success(gamesList)
            }
            is Response.Failure -> {
                gamesListResponse
            }
        }
    }


}