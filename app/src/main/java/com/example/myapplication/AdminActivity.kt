package com.example.myapplication

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
import com.example.myapplication.screens.HomeScreen


class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme { // Use your existing theme or create a new one
                val navController = rememberNavController()
                val eventViewModel: EventViewModel = viewModel()
                HomeScreen(viewModel = eventViewModel)

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationHost(
                            navController = navController,
                            viewModel = viewModel()
                        )
                    }
                }
            }
        }
    }
}