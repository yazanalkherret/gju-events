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
        loadEnrollments()
        loadEnrolledEvents()
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
    fun enrollToEvent(eventTitle: String) {
        viewModelScope.launch {
            val userEmail = currentUser.value.email
            // Remove space between title and email
            val docId = "$eventTitle$userEmail" // Fixed

            val enrollmentData = hashMapOf(
                "userEmail" to userEmail,
                "eventTitle" to eventTitle,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("enrollments").document(docId)
                .set(enrollmentData)
                .addOnSuccessListener {
                    loadEnrollments()
                }
        }
    }

    fun isUserEnrolled(eventTitle: String): Boolean {
        return enrollments.value.any {
            it.userEmail == currentUser.value.email &&
                    it.eventTitle == eventTitle
        }
    }
    private fun loadEnrollments() {
        val userEmail = currentUser.value.email

        db.collection("enrollments")
            .whereEqualTo("userEmail", userEmail)
            .addSnapshotListener { snapshots, error ->
                snapshots?.documents?.mapNotNull {
                    it.toObject(Enrollment::class.java)
                }?.let { enrollmentsList ->
                    _enrollments.value = enrollmentsList
                }
            }
    }
    private fun loadEnrolledEvents() {
        val userEmail = currentUser.value.email
        db.collection("enrollments")
            .whereEqualTo("userEmail", userEmail)
            .addSnapshotListener { enrollSnap, _ ->
                val enrolledTitles = enrollSnap?.documents?.mapNotNull {
                    it.getString("eventTitle")
                } ?: emptyList()

                if (enrolledTitles.isNotEmpty()) {
                    db.collection("events")
                        .whereIn("title", enrolledTitles)
                        .addSnapshotListener { eventSnap, _ ->
                            _enrolledEvents.value = eventSnap?.toObjects(Event::class.java) ?: emptyList()
                        }
                } else {
                    _enrolledEvents.value = emptyList()
                }
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
    fun unenrollFromEvent(
        eventTitle: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val userEmail = currentUser.value.email
            // Must match EXACTLY with enrollment ID format
            val docId = "$eventTitle${userEmail}"

            Log.d("Unenroll", "Attempting to delete: $docId") // Add this

            db.collection("enrollments").document(docId)
                .delete()
                .addOnSuccessListener {
                    Log.d("Unenroll", "Successfully deleted: $docId")
                    loadEnrollments()
                    loadEnrolledEvents()
                    onSuccess() // Trigger callback
                }
                .addOnFailureListener { e ->
                    Log.e("Unenroll", "Failed to delete $docId", e)
                }
        }
    }
}