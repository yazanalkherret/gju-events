package com.example.myapplication.viewmodels

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
            val docId = "$eventTitle ${userEmail}"

            // Use Firestore's server timestamp
            val enrollmentData = hashMapOf(
                "userEmail" to userEmail,
                "eventTitle" to eventTitle,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("enrollments")
                .document(docId)
                .set(enrollmentData)
                .addOnSuccessListener {
                    _enrollments.value += Enrollment(
                        userEmail = userEmail,
                        eventTitle = eventTitle,
                        timestamp = Timestamp.now()
                    )
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
    fun logout() {
        viewModelScope.launch {
            // Add actual logout logic here
            _currentUser.value = User() // Reset user
        }
    }
}