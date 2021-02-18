package com.thaisbl.desafio4.shared.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun isReadStoragePermissionAlreadyGranted(context: Context): Boolean {
    val isPermissionDenied = (ContextCompat
        .checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED)

    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || isPermissionDenied
}

// https://devofandroid.blogspot.com/2018/09/pick-image-from-gallery-android-studio_15.html
