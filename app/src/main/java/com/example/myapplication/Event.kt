package com.example.myapplication

data class Event(
    val id: Int,
    val title: String,
    val room: String,
    val date: String,
    val location: String,
    val description: String,
    val imageUri: String? = null
)