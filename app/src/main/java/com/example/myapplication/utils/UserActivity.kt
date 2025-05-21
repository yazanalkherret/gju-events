package com.example.myapplication.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier  // Added missing import
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.user.UserBottomNavigationBar
import com.example.myapplication.components.user.UserNavigationHost
import com.example.myapplication.EventViewModel
import com.example.myapplication.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {


                UserMainScreen()
            }
        }
    }
}

@Composable
fun UserMainScreen() {
    val navController = rememberNavController()
    val eventViewModel: EventViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    Scaffold(
        bottomBar = { UserBottomNavigationBar(navController) }
    ) { innerPadding ->
        // 3. Fix modifier usage
        UserNavigationHost(
            navController = navController,
            eventViewModel = eventViewModel,
            userViewModel = userViewModel,
            modifier = Modifier.padding(innerPadding) )
    }
}