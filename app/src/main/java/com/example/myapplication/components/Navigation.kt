package com.example.myapplication.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.screens.CreateEventScreen
import com.example.myapplication.screens.SettingsScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.EventViewModel

@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModel: EventViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { HomeScreen(viewModel) }
        composable(Screen.Create.route) { CreateEventScreen(viewModel) }
        composable(Screen.Settings.route) { SettingsScreen(viewModel) }
    }
}