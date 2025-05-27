package com.example.myapplication.components.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.components.Screen
import com.example.myapplication.components.Screen.PastEventsScreen
import com.example.myapplication.screens.UserHomePage
import com.example.myapplication.screens.UserMyEvents
import com.example.myapplication.screens.UserSettings
import com.example.myapplication.screens.LatestEvents
import com.example.myapplication.screens.PastEventsScreen
import com.example.myapplication.screens.UserEventDetailsScreen
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
                navController = navController,
                viewModel = eventViewModel)
        }
        composable("user_settings") {
            UserSettings(
                navController = navController,
                userViewModel = userViewModel
            )
        }
        composable("user_past_events") {
            PastEventsScreen(
                navController = navController,
                viewModel = eventViewModel
            )
        }
        composable("latest_events") {  // Add new route
            LatestEvents(navController = navController)
        }
        composable(
            route = "user_event_details/{eventTitle}",
            arguments = listOf(navArgument("eventTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventTitle = backStackEntry.arguments?.getString("eventTitle") ?: ""
            UserEventDetailsScreen(
                navController = navController,
                viewModel = eventViewModel,
                eventTitle = eventTitle
            )
        }
    }
}