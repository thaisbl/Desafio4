package com.thaisbl.desafio4.user

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @DocumentId
    val uid: String? = "",
    val name: String? = "",
): Parcelable