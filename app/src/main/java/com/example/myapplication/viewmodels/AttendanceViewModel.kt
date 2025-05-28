package com.example.myproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.components.Event
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Student(
    val email: String,
    var attended: Boolean = false
)

class AttendanceViewModel : ViewModel() {
    private val db = Firebase.firestore  // Initialize
    private var eventId: String = ""  // Add eventId storage
    private var enrollmentListener: ListenerRegistration? = null  // Add listener reference
    private var eventListener: ListenerRegistration? = null
    private val _attendedStudents = MutableStateFlow<Set<String>>(emptySet())
    val attendedStudents: StateFlow<Set<String>> = _attendedStudents.asStateFlow()
    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event

    private fun fetchEvent() {
        db.collection("events").document(eventId)
            .addSnapshotListener { snapshot, _ ->
                _event.value = snapshot?.toObject(Event::class.java)
            }
    }

    private fun fetchEnrollments() {
        enrollmentListener = db.collection("enrollments")
            .whereEqualTo("eventTitle", eventId) // Get enrollments for THIS event
            .addSnapshotListener { snapshots, _ ->
                snapshots?.documents?.forEach { doc ->
                    val email = doc.getString("userEmail") ?: return@forEach
                    if (students.none { it.email == email }) {
                        students.add(Student(email)) // Populate with enrolled emails
                    }
                }
            }
    }

    // New  sync for attendance
    private fun fetchAttendedStudents() {
        eventListener = db.collection("events").document(eventId)
            .addSnapshotListener { snapshot, _ ->
                val attended = snapshot?.get("attendedStudents") as? List<String> ?: emptyList()
                _attendedStudents.value = attended.toSet() // Update StateFlow
            }
    }
    // New  update
    fun toggleAttendance(studentEmail: String, isPresent: Boolean) {
        db.collection("events").document(eventId)
            .update("attendedStudents", if (isPresent) {
                FieldValue.arrayUnion(studentEmail) // Add to attended
            } else {
                FieldValue.arrayRemove(studentEmail) // Remove from attended
            })
    }

    // State management
    var students = mutableStateListOf<Student>()
    var searchText by mutableStateOf("")

    init {
        loadPlaceholderData()
    }

    private fun loadPlaceholderData() {
        students.addAll(listOf(

        ))
    }
    // In AttendanceViewModel.kt
    fun initialize(eventId: String) {
        this.eventId = eventId
        fetchEvent()
        fetchEnrollments()
        fetchAttendedStudents()
    }
    fun getFilteredStudents(): List<Student> {
        return students.filter {
            it.email.contains(searchText, ignoreCase = true)
        }
    }

    fun markAllAsAttended() {
        students.replaceAll { it.copy(attended = true) }
    }
    override fun onCleared() {
        enrollmentListener?.remove()
        eventListener?.remove()
        super.onCleared()
    }
}