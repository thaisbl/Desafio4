package com.thaisbl.desafio4.home.edit

import android.graphics.Bitmap
import com.thaisbl.desafio4.shared.model.data.Response
import java.io.ByteArrayOutputStream

class EditGameBusiness {

    private val repository by lazy {
        EditGameRepository()
    }

    suspend fun editGame(
        gameUid: String,
        name: String,
        releaseYear: String,
        description: String,
        imageBitmap: Bitmap?
    ): Response {
        // check required fields
        if (name.isBlank()) {
            return Response.Failure("Name field is required")
        }
        if (releaseYear.isBlank()) {
            return Response.Failure("Release Year field is required")
        }
        if (description.isBlank()) {
            return Response.Failure("Description field is required")
        }

        // compress image
        imageBitmap?.let { bitmap ->
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            return repository.editGame(
                gameUid = gameUid,
                title = name.trim(),
                releaseYear = releaseYear.toInt(),
                description = description.trim(),
                imageBytes = baos.toByteArray()
            )
        } ?: run {
            // user doesn't want to change game image - so we don't send it
            return repository.editGame(
                gameUid = gameUid,
                title = name.trim(),
                releaseYear = releaseYear.toInt(),
                description = description.trim()
            )
        }
    }

    suspend fun saveGame(
        name: String,
        releaseYear: String,
        description: String,
        imageBitmap: Bitmap?
    ): Response {
        // check all required fields
        if (name.isBlank()) {
            return Response.Failure("Name field is required")
        }
        if (releaseYear.isBlank()) {
            return Response.Failure("Release Year field is required")
        }
        if (description.isBlank()) {
            return Response.Failure("Description field is required")
        }
        if (imageBitmap == null) {
            return Response.Failure("Game Image is required")
        }

        // compress image
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return repository.saveGame(
            title = name.trim(),
            releaseYear = releaseYear.toInt(),
            description = description.trim(),
            imageBytes = baos.toByteArray()
        )
    }

}