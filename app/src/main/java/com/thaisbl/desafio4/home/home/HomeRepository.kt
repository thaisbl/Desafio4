package com.thaisbl.desafio4.home.home

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thaisbl.desafio4.home.Game
import kotlinx.coroutines.tasks.await
import com.thaisbl.desafio4.shared.constant.FirebaseFirestoreConstants
import com.thaisbl.desafio4.shared.model.data.Response

class HomeRepository {

    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val firebaseFirestore by lazy {
        Firebase.firestore
    }

    fun signOutUser(): Response {
        return try {
            // this returns nothing
            firebaseAuth.signOut()

            Response.Success(null)
        } catch (e: FirebaseAuthException) {
            Response.Failure(e.localizedMessage)
        }
    }

    suspend fun getUserGamesList(): Response {
        var gamesList: MutableList<Game?> = mutableListOf()

        return try {
            firebaseAuth.currentUser?.let { currentUser ->
                val query = firebaseFirestore
                    .collection(FirebaseFirestoreConstants.Games.COLLECTION_NAME)
                    .whereEqualTo(FirebaseFirestoreConstants.Games.FIELD_USER_UID, currentUser.uid)
                    .get()
                    .await()

                gamesList = query.toObjects(Game::class.java)
            }

            Response.Success(gamesList)
        } catch (e: FirebaseException) {
            Response.Failure(e.localizedMessage)
        }
    }

}