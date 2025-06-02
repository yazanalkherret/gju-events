package com.example.myapplication.components.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


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

    // Custom colors
    val navbarBackground = Color(0xFFECF2FD)
    val selectedContainer = Color(0xFFBDE4FA)
    val unselectedContainer = Color.Transparent
    val textAndIconColor = Color.Black

    NavigationBar(
        containerColor = navbarBackground,
        tonalElevation = NavigationBarDefaults.Elevation,
        modifier = Modifier
    ) {
        items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        tint = textAndIconColor
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        color = textAndIconColor
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = selectedContainer,
                    selectedIconColor = textAndIconColor,
                    selectedTextColor = textAndIconColor,
                    unselectedIconColor = textAndIconColor,
                    unselectedTextColor = textAndIconColor,
                )
            )
        }
    }
}