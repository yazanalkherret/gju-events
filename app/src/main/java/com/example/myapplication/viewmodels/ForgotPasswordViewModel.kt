package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ForgotPasswordViewModel : ViewModel(){

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    fun sendResetEmail(email: String) {
        if (email.isEmpty()) {
            _message.value = "Please enter your email"
            return
        }
        val safeEmail = email.replace(".", "_")
        db.collection("users").document(safeEmail).get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _message.value = "Reset email sent to $email"
                            } else {
                                _message.value = "Error: ${task.exception?.message}"
                            }
                        }
                }
            else {

             _message.value = "Email not registered"
                }
            }



    }

    fun clearMessage() {
        _message.value = null
    }
}