package com.example.myapplication.activities

//import com.example.myapplication.XMLScreens.HomePage
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.myapplication.screens.LoginScreen
import com.example.myapplication.utils.UserActivity
import com.example.myapplication.viewmodels.LoginViewModel


class Login_Activity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(loginViewModel = loginViewModel,
                    onNavigateToAdmin = {
                        val intent = Intent(this, Admin_Activity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onNavigateToHome = {
                        val intent = Intent(this, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
            }
        }
    }
}