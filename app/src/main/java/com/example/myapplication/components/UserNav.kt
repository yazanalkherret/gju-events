package com.example.myapplication.components.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.screens.UserHomePage
import com.example.myapplication.screens.UserMyEvents
import com.example.myapplication.screens.UserSettings
import com.example.myapplication.screens.LatestEvents
import com.example.myapplication.screens.UserPastEvents // Add this import
import com.example.myapplication.viewmodels.UserViewModel
import com.example.myapplication.viewmodels.EventViewModel

@Composable
fun UserNavigationHost(
    navController: NavHostController,
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "user_home",
        modifier = modifier
    ) {
        composable("user_home") {
            UserHomePage(
                navController = navController,  // Pass navController here
                viewModel = eventViewModel
            )
        }
        composable("user_my_events") {
            UserMyEvents(
                viewModel = eventViewModel,
                navController = navController
            )
        }
        composable("user_settings") {
            UserSettings(
                navController = navController,
                userViewModel = userViewModel
            )
        }
        composable("user_past_events") {
            UserPastEvents(navController = navController)  // Your new screen
        }
        composable("latest_events") {  // Add new route
            LatestEvents(navController = navController)
        }
    }
}