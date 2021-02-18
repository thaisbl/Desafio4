package com.thaisbl.desafio4.home

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    @DocumentId
    val uid: String = "",
    val userUid: String? = "",
    var imagePath: String? = "",
    var title: String = "",
    var releaseYear: Int? = 0,
    var description: String? = "",
    @Exclude
    var mImageStoragePath: String? = "",
) : Parcelable