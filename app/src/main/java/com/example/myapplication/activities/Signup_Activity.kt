package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.myapplication.screens.SignupScreen
import com.example.myapplication.viewmodels.SignupViewModel

class Signup_Activity : ComponentActivity() {

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SignupScreen(
                signupViewModel = signupViewModel,
                onNavigateToLogin = { message ->
                    val intent = Intent(this, Login_Activity::class.java)
                    intent.putExtra("message", message)
                    startActivity(intent)
                }

            )
        }
    }
}