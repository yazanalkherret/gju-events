package com.example.myapplication.components

data class Event(
    val id: Int,
    val title: String,
    val room: String,
    val date: String,  // Formatted date
    val time: String,  // Formatted time
    val description: String,
    val imageUri: String?
)