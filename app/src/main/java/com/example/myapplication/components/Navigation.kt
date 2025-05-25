package com.example.myapplication.components

import AttendanceScreen
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
import com.example.myapplication.screens.LoginScreen
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.viewmodels.UserViewModel
import com.example.myapplication.screens.ManageAdminsScreen
import com.example.myapplication.screens.ModifyEventScreen
import com.example.myapplication.viewmodels.LoginViewModel


sealed class Screen(
    val route: String,
    val title: String
) {

    sealed class BottomNavScreen(
        route: String,
        title: String,
        val icon: ImageVector
    ) : Screen(route, title) {
        object Home : BottomNavScreen("home", "Home", Icons.Default.Home)
        object Create : BottomNavScreen("create", "Create", Icons.Default.Add)
        object Settings : BottomNavScreen("settings", "Settings", Icons.Default.Settings)

    }
    object Attendance : Screen("attendance/{eventId}", "Attendance") {
        fun createRoute(eventId: String) = "attendance/$eventId"
    }


    object EventDetails : Screen("eventDetails/{eventId}", "Event Details") {
        fun createRoute(eventId: String) = "eventDetails/$eventId" // Changed to String
    }

    object ModifyEvent : Screen("modifyEvent/{eventId}", "Edit Event") {
        fun createRoute(eventId: String) = "modifyEvent/$eventId"
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

        composable(Screen.BottomNavScreen.Home.route) {
            HomeScreen(
                viewModel = eventViewModel,  // Matches first parameter
                navController = navController,
                onEventClick = { /* ... */ }// Matches second parameter
            )
        }

        composable(Screen.BottomNavScreen.Create.route) { // Fixed reference
            CreateEventScreen(viewModel = eventViewModel)
        }

        composable(
            route = Screen.ModifyEvent.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            ModifyEventScreen(viewModel = eventViewModel,navController = navController, eventId = eventId)
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
            arguments = listOf(navArgument("eventId") { type = NavType.StringType  })
        ) { backStackEntry ->
            EventDetailsScreen(
                navController = navController,
                viewModel = eventViewModel,
                eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            )
        }

        composable(
            route = Screen.Attendance.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            AttendanceScreen(
                eventId = eventId,
                onBackClick = { navController.popBackStack() }
            )
        }



        composable("login") {
            LoginScreen(
                loginViewModel = LoginViewModel(),   // provide the LoginViewModel here
                onNavigateToAdmin = {
                    navController.navigate("manageAdmins") {
                        popUpTo(0)
                    }
                },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}