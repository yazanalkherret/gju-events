package com.example.myapplication.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.event_planner.ui.theme.MyApplicationTheme
import com.example.myapplication.components.BottomNavigationBar
import com.example.myapplication.components.NavigationHost
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.viewmodels.UserViewModel

class Admin_Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val eventViewModel: EventViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationHost(
                            navController = navController,
                            eventViewModel = eventViewModel,
                            userViewModel = userViewModel
                        )
                    }
                }
            }
        }
    }
}