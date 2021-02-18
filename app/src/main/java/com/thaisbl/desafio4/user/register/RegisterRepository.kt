package com.thaisbl.desafio4.user.register

import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thaisbl.desafio4.shared.constant.FirebaseFirestoreConstants
import com.thaisbl.desafio4.shared.model.data.Response
import com.thaisbl.desafio4.user.User
import kotlinx.coroutines.tasks.await

class RegisterRepository {

    private val firebaseAuth by lazy {
        Firebase.auth
    }
    private val firebaseFirestore by lazy {
        Firebase.firestore
    }

    private suspend fun registerUserOnFirestore(userUid: String, userName: String): Response {
        try {
            firebaseFirestore
                .collection(FirebaseFirestoreConstants.Users.COLLECTION_NAME)
                .document(userUid)
                .set(User(userUid, userName), SetOptions.merge())
                .await()
            return Response.Success(true)
        } catch (e: FirebaseFirestoreException) {
            return Response.Failure(e.localizedMessage)
        }
    }

    private suspend fun registerUserOnAuthentication(email: String, password: String): Response {
        return try {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            Response.Success(true)
        } catch (e: FirebaseAuthException) {
            Response.Failure(e.localizedMessage)
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): Response {
        return try {
            when (val result = registerUserOnAuthentication(email, password)) {
                is Response.Success -> {
                    firebaseAuth.currentUser?.let {
                        registerUserOnFirestore(it.uid, name)
//                        firebaseFirestore
//                            .collection(FirebaseFirestoreConstants.Users.COLLECTION_NAME)
//                            .document(it.uid)
//                            .set(User(it.uid, name), SetOptions.merge())
                        Response.Success(true)
                    } ?: run {
                        Response.Failure("An error occurred. Try again later")
                    }
                }
                is Response.Failure -> {
                    result
                }
            }
        } catch (e: FirebaseException) {
            Response.Failure(e.localizedMessage)
        }
    }

}