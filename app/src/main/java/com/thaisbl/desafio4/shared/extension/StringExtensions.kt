package com.thaisbl.desafio4.shared.extension

fun String.isValidEmailAddress(): Boolean {
    return this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}