package com.example.myapplication.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun decodeBase64ToImage(base64: String): ImageBitmap? {
    return try {
        val cleaned = base64.replace(Regex("[^a-zA-Z0-9+/=]"), "")
        val decodedBytes = Base64.decode(cleaned, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}