package com.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun createAccount(fullName: String, email: String, password: String, confirmPassword: String, major: String){
        if (fullName.isEmpty()) {
            _message.value = "Please enter your full name"
            return
        }
        if (email.isEmpty()) {
            _message.value = "Please enter your Email"
            return
        }
        if (!email.endsWith("@gju.edu.jo")) {
            _message.value = "Only GJU emails allowed"
            return
        }
        if (password != confirmPassword) {
            _message.value = "Passwords do not match"
            return
        }
        if(major.isEmpty()){
            _message.value = "Please enter your Major"
            return
        }
        viewModelScope.launch {

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener()
                { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->

                            if (emailTask.isSuccessful) {

                                val safeEmail = email.replace(".", "_")
                                val userInfo = hashMapOf(
                                    "fullName" to fullName,
                                    "email" to email,
                                    "role" to "user",
                                    "certificate" to 1
                                )
                                db.collection("users").document(safeEmail).set(userInfo)

                                _message.value = "Account created. Verification email sent."
                            } else {
                                _message.value = "Failed to send verification email."
                            }
                        }
                    } else {
                        _message.value = "Sign up failed: ${task.exception?.message}"
                    }
                }
        }
    }


    fun clearMessage() {
        _message.value = null
    }


}