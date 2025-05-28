package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.components.Enrollment
import com.example.myapplication.components.Event
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val isAdmin: Boolean = false
)


class EventViewModel() : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()
    private val _enrollments = MutableStateFlow<List<Enrollment>>(emptyList())
    val enrollments: StateFlow<List<Enrollment>> = _enrollments.asStateFlow()

    fun getEventById(eventId: String): Event? {
        return _events.value.find { it.id == eventId }
    }
    fun getEventByTitle(title: String): Event? {
        return _events.value.find { it.title == title }
    }

    fun updateEvent(
        event: Event,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                db.collection("events")
                    .document(event.id)
                    .set(event)
                    .await()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
    private val _enrolledEvents = MutableStateFlow<List<Event>>(emptyList())
    val enrolledEvents: StateFlow<List<Event>> = _enrolledEvents.asStateFlow()



    private val _currentUser = MutableStateFlow(
        User(
            email = FirebaseAuth.getInstance().currentUser?.email?:"" ,
            isAdmin = false
        )
    )

    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
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
                    loadEnrolledEventsFromLocalEvents()
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
                // Use the event's ID as the Firestore document ID
                db.collection("events")
                    .document(event.id)
                    .set(event)
                    .await()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
    fun enrollToEvent(eventId: String) {
        viewModelScope.launch {
            val userEmail = currentUser.value.email
            val eventRef = db.collection("events").document(eventId)

            eventRef.update("enrolledStudents", FieldValue.arrayUnion(userEmail))
                .addOnSuccessListener {
                    Log.d("Enroll", "Successfully enrolled $userEmail in event $eventId")
                }
                .addOnFailureListener { e ->
                    Log.e("Enroll", "Failed to enroll $userEmail in event $eventId", e)
                }
        }
    }


    fun isUserEnrolled(event: Event): Boolean {
        return event.enrolledStudents.contains(currentUser.value.email)
    }

    private fun loadEnrolledEventsFromLocalEvents() {
        val userEmail = currentUser.value.email
        _enrolledEvents.value = _events.value.filter { event ->
            event.enrolledStudents.contains(userEmail)
        }
    }

    fun deleteEvent(
        eventId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        // Get reference to Firestore collection
        val db = FirebaseFirestore.getInstance()
        db.collection("events").document(eventId)
            .delete()
            .addOnSuccessListener {
                // Remove from local list if needed
                _events.value = _events.value.filter { it.id != eventId }
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    fun logout() {
        viewModelScope.launch {
            // Add actual logout logic here
            _currentUser.value = User() // Reset user
        }
    }
    fun unenrollFromEvent(eventId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val userEmail = currentUser.value.email
            val eventRef = db.collection("events").document(eventId)

            eventRef.update("enrolledStudents", FieldValue.arrayRemove(userEmail))
                .addOnSuccessListener {
                    Log.d("Unenroll", "Successfully unenrolled $userEmail from event $eventId")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("Unenroll", "Failed to unenroll $userEmail from event $eventId", e)
                }
        }
    }

}
fun isEventInPast(dateString: String, timeString: String): Boolean {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fullDateTime = formatter.parse("$dateString $timeString")
        fullDateTime?.before(Date()) ?: false
    } catch (e: Exception) {
        false
    }
}