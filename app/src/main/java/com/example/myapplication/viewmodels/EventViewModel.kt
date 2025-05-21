package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.components.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val isAdmin: Boolean = false
)


class EventViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    fun getEventById(eventId: String): Event? {
        return _events.value.find { it.id == eventId }
    }

    private val _currentUser = MutableStateFlow(
        User( // Provide default values
            id = "guest_001",
            name = "Guest User",
            email = "guest@example.com",
            isAdmin = false
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()


    init {
        fetchEventsRealTime() // Start listening for data changes
    }
    private fun fetchEventsRealTime() {
        db.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Event::class.java)
                }?.let { eventsList ->
                    _events.value = eventsList  // Update _events instead of events
                }
            }
    }
    fun addEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val sanitizedTitle = event.title
                    .lowercase()
                    .replace(" ", "")
                    .replace(Regex("[^a-z0-9_]"), "")

                val docRef = db.collection("events").document(sanitizedTitle)
                docRef.set(event.copy(id = sanitizedTitle)).await()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Add actual logout logic here
            _currentUser.value = User() // Reset user
        }
    }
}