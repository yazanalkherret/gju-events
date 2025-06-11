package com.example.myproject.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.components.Event
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Student(
    val email: String,
    var attended: Boolean = false
)

class AttendanceViewModel : ViewModel() {
    private val db = Firebase.firestore
    private var eventId: String = ""
    private var eventListener: ListenerRegistration? = null
    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event.asStateFlow()

    private val _enrolledStudents = MutableStateFlow<List<String>>(emptyList())
    val enrolledStudents: StateFlow<List<String>> = _enrolledStudents.asStateFlow()

    private val _attendedStudents = MutableStateFlow<Set<String>>(emptySet())
    val attendedStudents: StateFlow<Set<String>> = _attendedStudents.asStateFlow()


    fun initialize(eventId: String) {
        this.eventId = eventId
        fetchEvent()
    }
    private fun fetchEvent() {
        eventListener?.remove()
        eventListener = db.collection("events").document(eventId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("AttendanceVM", "Error fetching event", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    if (it.exists()) {
                        val eventData = it.toObject(Event::class.java)
                        _event.value = eventData

                        // Update enrolled students
                        eventData?.enrolledStudents?.let { students ->
                            _enrolledStudents.value = students
                        }

                        // Update attended students
                        eventData?.attendedStudents?.let { attended ->
                            _attendedStudents.value = attended.toSet()
                        }
                    } else {
                        Log.w("AttendanceVM", "Event document does not exist")
                    }
                }
            }
    }

    // New  update
    fun toggleAttendance(studentEmail: String, isPresent: Boolean) {
        db.collection("events").document(eventId)
            .update(
                "attendedStudents",
                if (isPresent) FieldValue.arrayUnion(studentEmail)
                else FieldValue.arrayRemove(studentEmail)
            )
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

    fun getFilteredStudents(): List<Student> {
        return students.filter {
            it.email.contains(searchText, ignoreCase = true)
        }
    }

    fun markAllAsAttended() {
        val emails = _event.value?.enrolledStudents ?: emptyList()
        db.collection("events").document(eventId)
            .update("attendedStudents", emails)
    }

    override fun onCleared() {
        eventListener?.remove()
        super.onCleared()
    }
}