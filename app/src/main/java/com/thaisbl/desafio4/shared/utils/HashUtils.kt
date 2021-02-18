package com.thaisbl.desafio4.shared.utils

import java.math.BigInteger
import java.security.MessageDigest

fun createMD5Hash(text: String): String {
    val digest = MessageDigest.getInstance("MD5")
    val message = text.toByteArray()

    return BigInteger(1, digest.digest(message))
        .toString(16)
        .padStart(32, '0')
}