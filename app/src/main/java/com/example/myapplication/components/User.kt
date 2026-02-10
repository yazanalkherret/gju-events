package com.example.myapplication.components

data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "user",
    val certificate: Int = 0
)