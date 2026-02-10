package com.example.myapplication.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class LoginViewModel : BaseAuthViewModel() {

    private val _loginSuccessRole = MutableStateFlow<String?>(null)
    val loginSuccessRole: StateFlow<String?> = _loginSuccessRole


    fun login(email: String, password: String){
        if (email.isBlank()) {
            messageState.value = "Please enter your email"
            return
        }
        if (password.isBlank()) {
            messageState.value = "Please enter your password"
            return
        }

        mAuth.signInWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user != null && user.isEmailVerified) {
                        val safeEmail = email.replace(".", "_")
                        db.collection("users").document(safeEmail).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val role = document.getString("role")
                                    _loginSuccessRole.value = role

                                } else {
                                    messageState.value = "User role not found."
                                }
                            }
                            .addOnFailureListener {
                                messageState.value = "Failed to fetch user data: ${it.message}"
                            }
                    } else {
                        messageState.value = "Please verify your email first."
                    }
                } else {
                    messageState.value = "Login failed: ${task.exception?.message}"
                }
            }

    }
    fun clearLoginSuccess() {
        _loginSuccessRole.value = null
    }
}