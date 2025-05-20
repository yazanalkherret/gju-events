package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserData(
    val id: String = "",
    val email: String = "",
    val role: String = "user",
    val certificate: Int = 0
)

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            auth.currentUser?.email?.let { email ->
                val safeEmail = email.replace(".", "_")
                db.collection("users").document(safeEmail)
                    .addSnapshotListener { snapshot, error ->
                        error?.let {
                            // Handle error
                            return@addSnapshotListener
                        }
                        _userData.value = snapshot?.toObject(UserData::class.java) ?: UserData()
                    }
            }
        }
    }
}