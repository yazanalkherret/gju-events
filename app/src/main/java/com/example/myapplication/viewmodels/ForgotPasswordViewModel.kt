package com.example.myapplication.viewmodels


class ForgotPasswordViewModel : BaseAuthViewModel(){
    fun sendResetEmail(email: String) {
        if (email.isEmpty()) {
            messageState.value = "Please enter your email"
            return
        }
        val safeEmail = email.replace(".", "_")
        db.collection("users").document(safeEmail).get()
            .addOnSuccessListener { document ->
                if (document.exists()){
                    mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                messageState.value = "Reset email sent to $email"
                            } else {
                                messageState.value = "Error: ${task.exception?.message}"
                            }
                        }
                }
            else {

             messageState.value = "Email not registered"
                }
            }

    }

}