package com.example.myapplication.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.myapplication.components.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class UserViewModel : BaseAuthViewModel() {
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    init {
        loadUserData()
    }
    private fun loadUserData() {
        viewModelScope.launch {
            mAuth.currentUser?.email?.let { email ->
                val safeEmail = email.replace(".", "_")
                db.collection("users").document(safeEmail)
                    .addSnapshotListener { snapshot, error ->
                        error?.let {
                            // Handle error
                            return@addSnapshotListener
                        }
                        _userData.value = snapshot?.toObject(User::class.java) ?: User()
                    }
            }
        }
    }
}