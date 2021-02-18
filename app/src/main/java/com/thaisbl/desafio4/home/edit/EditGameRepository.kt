package com.thaisbl.desafio4.home.edit

import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.thaisbl.desafio4.home.Game
import kotlinx.coroutines.tasks.await
import com.thaisbl.desafio4.shared.constant.FirebaseFirestoreConstants
import com.thaisbl.desafio4.shared.constant.FirebaseStorageConstants
import com.thaisbl.desafio4.shared.model.data.Response
import com.thaisbl.desafio4.shared.utils.createMD5Hash
import com.thaisbl.desafio4.shared.utils.getCurrentDateTime

class EditGameRepository {

    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val firebaseFirestore by lazy {
        Firebase.firestore
    }
    private val firebaseStorage by lazy {
        Firebase.storage
    }

    private suspend fun uploadImageToStorage(
        userUid: String,
        imageBytes: ByteArray
    ): Response {
        return try {
            val imagePath = "${createMD5Hash(userUid + getCurrentDateTime())}.jpg"

            firebaseStorage
                .reference
                .child(FirebaseStorageConstants.REFERENCE_GAME_IMAGES_FOLDER)
                .child(imagePath)
                .putBytes(imageBytes)
                .await()

            Response.Success(imagePath)
        } catch (e: FirebaseException) {
            Response.Failure(e.localizedMessage)
        }
    }

    suspend fun editGame(
        gameUid: String,
        title: String,
        releaseYear: Int,
        description: String,
        imageBytes: ByteArray? = null,
    ): Response {
        return try {
            firebaseAuth.currentUser?.let { user ->
                val reference = firebaseFirestore
                    .collection(FirebaseFirestoreConstants.Games.COLLECTION_NAME)
                    .document(gameUid)

                val game = reference.get().await().toObject(Game::class.java)

                game?.title = title
                game?.description = description
                game?.releaseYear = releaseYear

                imageBytes?.let { bytes ->
                    when (val response = uploadImageToStorage(user.uid, bytes)) {
                        is Response.Success -> {
                            game?.imagePath = response.data as String
                        }
                        is Response.Failure -> {
                            response
                        }
                    }
                }

                game?.let {
                    reference.set(game, SetOptions.merge()).await()
                    Response.Success(game)
                } ?: run {
                    Response.Failure("An error occurred. Try again later")
                }
            } ?: run {
                Response.Failure("An error occurred. Try again later")
            }
        } catch (e: FirebaseException) {
            Response.Failure(e.localizedMessage)
        }
    }

    suspend fun saveGame(
        title: String,
        releaseYear: Int,
        description: String,
        imageBytes: ByteArray
    ): Response {
        return try {
            firebaseAuth.currentUser?.let { user ->
                when (val response = uploadImageToStorage(user.uid, imageBytes)) {
                    is Response.Success -> {
                        val imagePath = response.data as String

                        val newGame = Game(
                            userUid = user.uid,
                            imagePath = imagePath,
                            title = title,
                            releaseYear = releaseYear,
                            description = description
                        )

                        // this returns nothing
                        firebaseFirestore
                            .collection(FirebaseFirestoreConstants.Games.COLLECTION_NAME)
                            .document()
                            .set(newGame, SetOptions.merge())
                            .await()

                        Response.Success(newGame)
                    }
                    is Response.Failure -> {
                        response
                    }
                }
            } ?: run {
                Response.Failure("An error occurred. Try again later")
            }
        } catch (e: FirebaseException) {
            Response.Failure(e.localizedMessage)
        }
    }

}