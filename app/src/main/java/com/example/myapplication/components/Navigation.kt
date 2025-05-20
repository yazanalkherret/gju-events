package com.example.myapplication.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.screens.CreateEventScreen
import com.example.myapplication.screens.EventDetailsScreen
import com.example.myapplication.screens.SettingsScreen
import com.example.myapplication.screens.HomeScreen
import com.example.myapplication.EventViewModel
import com.example.myapplication.UserViewModel
import com.example.myapplication.screens.ManageAdminsScreen
// dsdsdsdsdsdsdsdsdsads
sealed class Screen(
    val route: String,
    val title: String
) {
    // For screens with icons (Bottom Navigation)
    sealed class BottomNavScreen(
        route: String,
        title: String,
        val icon: ImageVector  // Non-null icon
    ) : Screen(route, title) {
        object Home : BottomNavScreen("home", "Home", Icons.Default.Home)
        object Create : BottomNavScreen("create", "Create", Icons.Default.Add)
        object Settings : BottomNavScreen("settings", "Settings", Icons.Default.Settings)

    }

    // For screens without icons
    object EventDetails : Screen("eventDetails/{eventId}", "Event Details") {
        fun createRoute(eventId: Int) = "eventDetails/$eventId"
    }

    object ManageAdmins : Screen("manageAdmins", "Manage Admins")
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    eventViewModel: EventViewModel,
    userViewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavScreen.Home.route // Fixed reference
    ) {
        composable(Screen.BottomNavScreen.Home.route) { // Fixed reference
            HomeScreen(viewModel = eventViewModel) { eventId ->
                navController.navigate(Screen.EventDetails.createRoute(eventId))
            }
        }
        composable(Screen.BottomNavScreen.Create.route) { // Fixed reference
            CreateEventScreen(viewModel = eventViewModel)
        }

        composable(Screen.BottomNavScreen.Settings.route) { // Fixed reference
            SettingsScreen(
                navController = navController,
                userViewModel = userViewModel) {
                navController.navigate("login") { popUpTo(0) }
            }
        }
        composable(Screen.ManageAdmins.route) {
            ManageAdminsScreen()
        }

        composable(
            route = Screen.EventDetails.route,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            EventDetailsScreen(
                navController = navController,
                viewModel = eventViewModel,
                eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            )
        }
    }
}