package com.thaisbl.desafio4.home.detail

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thaisbl.desafio4.home.Game
import kotlinx.coroutines.tasks.await
import com.thaisbl.desafio4.shared.constant.FirebaseFirestoreConstants
import com.thaisbl.desafio4.shared.model.data.Response

class GameDetailRepository {

    private val firebaseFirestore by lazy {
        Firebase.firestore
    }

    suspend fun getGameData(gameUid: String): Response {
        return try {
            val game = firebaseFirestore
                .collection(FirebaseFirestoreConstants.Games.COLLECTION_NAME)
                .document(gameUid)
                .get()
                .await()
                .toObject(Game::class.java)

            Response.Success(game)
        } catch (e: FirebaseException) {
            Response.Failure(e.localizedMessage)
        }
    }

}