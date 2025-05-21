package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel(){

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _loginSuccessRole = MutableStateFlow<String?>(null)
    val loginSuccessRole: StateFlow<String?> = _loginSuccessRole


    fun login(email: String, password: String){
        if (email.isBlank()) {
            _message.value = "Please enter your email"
            return
        }
        if (password.isBlank()) {
            _message.value = "Please enter your password"
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
                                    _message.value = "User role not found."
                                }
                            }
                            .addOnFailureListener {
                                _message.value = "Failed to fetch user data: ${it.message}"
                            }
                    } else {
                        _message.value = "Please verify your email first."
                    }
                } else {
                    _message.value = "Login failed: ${task.exception?.message}"
                }
            }

    }
    fun clearMessage() {
        _message.value = null
    }
    fun clearLoginSuccess() {
        _loginSuccessRole.value = null
    }
}