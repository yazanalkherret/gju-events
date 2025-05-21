package com.example.myproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Student(
    val name: String,
    var attended: Boolean = false
)

class AttendanceViewModel : ViewModel() {
    // State management
    var students = mutableStateListOf<Student>()
    var searchText by mutableStateOf("")

    init {
        loadPlaceholderData()
    }

    private fun loadPlaceholderData() {
        students.addAll(listOf(
            Student("Ali Ahmad"),
            Student("Lina Saeed"),
            Student("Kareem Hasan"),
            Student("Sara Yousef"),
            Student("Zaid Imad"),
            Student("Noor Tariq"),
            Student("Tariq Fadi"),
            Student("Hiba Sami"),
            Student("Rami Omar"),
            Student("Dana Basel")
        ))
    }

    fun getFilteredStudents(): List<Student> {
        return students.filter {
            it.name.contains(searchText, ignoreCase = true)
        }
    }

    fun toggleAttendance(student: Student) {
        val index = students.indexOfFirst { it.name == student.name }
        if (index != -1) {
            students[index] = students[index].copy(attended = !students[index].attended)
        }
    }

    fun markAllAsAttended() {
        students.replaceAll { it.copy(attended = true) }
    }
}