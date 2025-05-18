package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val isAdmin: Boolean = false
)


class EventViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()


    private val _currentUser = MutableStateFlow(
        User( // Provide default values
            id = "guest_001",
            name = "Guest User",
            email = "guest@example.com",
            isAdmin = false
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()


    fun addEvent(event: Event) {
        viewModelScope.launch {
            _events.value = _events.value + event
        }
    }


    fun logout() {
        viewModelScope.launch {
            // Add actual logout logic here
            _currentUser.value = User() // Reset user
        }
    }
}