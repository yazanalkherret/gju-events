package com.example.myapplication.components.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// User-specific screens
sealed class UserScreen(val route: String, val title: String, val icon: ImageVector) {
    object UserHome : UserScreen("user_home", "Home", Icons.Default.Home)
    object UserMyEvents : UserScreen("user_my_events", "My Events", Icons.Default.List)
    object UserSettings : UserScreen("user_settings", "Settings", Icons.Default.Settings)
}

@Composable
fun UserBottomNavigationBar(navController: NavController) {
    val items = listOf(
        UserScreen.UserHome,
        UserScreen.UserMyEvents,
        UserScreen.UserSettings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}