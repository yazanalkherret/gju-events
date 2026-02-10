package com.example.myapplication.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignupViewModel : BaseAuthViewModel() {

    fun createAccount(fullName: String, email: String, password: String, confirmPassword: String, major: String){
        if (fullName.isEmpty()) {
            messageState.value = "Please enter your full name"
            return
        }
        if (email.isEmpty()) {
            messageState.value = "Please enter your Email"
            return
        }
        if (!email.endsWith("@gju.edu.jo")) {
            messageState.value = "Only GJU emails allowed"
            return
        }
        if (password != confirmPassword) {
            messageState.value = "Passwords do not match"
            return
        }
        if(major.isEmpty()){
            messageState.value = "Please enter your Major"
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

                                messageState.value = "Account created. Verification email sent."
                            } else {
                                messageState.value = "Failed to send verification email."
                            }
                        }
                    } else {
                        messageState.value = "Sign up failed: ${task.exception?.message}"
                    }
                }
        }
    }


}