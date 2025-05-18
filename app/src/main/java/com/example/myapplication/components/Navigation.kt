package com.example.myapplication.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.screens.CreateEventScreen
import com.example.myapplication.screens.SettingsScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.EventViewModel
import com.example.myapplication.UserViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = eventViewModel)
        }
        composable(Screen.Create.route) {
            CreateEventScreen(viewModel = eventViewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                userViewModel = userViewModel,
                onLogout = {
                    // Add your logout navigation logic here
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}