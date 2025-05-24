// Updated Enrollment.kt
package com.example.myapplication.components

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Enrollment(
    val userEmail: String = "",
    val eventTitle: String = "",
    @ServerTimestamp val timestamp: Timestamp = Timestamp.now()
)