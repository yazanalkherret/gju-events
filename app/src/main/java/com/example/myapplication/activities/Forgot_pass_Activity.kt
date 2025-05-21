package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.activities.Login_Activity
import com.example.myapplication.screens.ForgotPasswordScreen
import com.example.myapplication.viewmodels.ForgotPasswordViewModel

class Forgot_pass_Activity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ForgotPasswordScreen(
                forgotPasswordViewModel = ForgotPasswordViewModel(),
                onNavigateBack = {

                    val intent = Intent(this, Login_Activity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }




    }
}