package com.thaisbl.desafio4.shared.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateTime(): String {
    return SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ROOT).format(Date())
}