package com.thaisbl.desafio4.home.detail

import android.util.Log
import com.thaisbl.desafio4.home.Game
import com.thaisbl.desafio4.shared.model.data.Response
import com.thaisbl.desafio4.shared.model.repository.GameRepository

class GameDetailBusiness {

    private val gameDetailRepository by lazy {
        GameDetailRepository()
    }
    private val gameRepository by lazy {
        GameRepository()
    }

    suspend fun getGameData(gameUid: String): Response {
        return when (val gameResponse = gameDetailRepository.getGameData(gameUid)) {
            is Response.Success -> {
                val game = gameResponse.data as Game

                // get downloadable url of image from firestore
                game.imagePath?.let {
                    when (val gameImgResponse = gameRepository.getGameImageStorageUrl(it)) {
                        is Response.Success -> {
                            game.mImageStoragePath = gameImgResponse.data as String
                        }
                        is Response.Failure -> {
                            Log.e("NMCJ", "Image from ${game.title} does not exist!")
                        }
                    }
                }

                Response.Success(game)
            }
            is Response.Failure -> {
                gameResponse
            }
        }
    }

}