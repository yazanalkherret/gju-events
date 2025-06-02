package com.example.myapplication.components


import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.BottomNavScreen.Home,
        Screen.BottomNavScreen.Create,
        Screen.BottomNavScreen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Custom colors
    val navbarBackground = Color(0xFFECF2FD)
    val selectedContainer = Color(0xFFBDE4FA)
    val textAndIconColor = Color.Black

    NavigationBar(
        containerColor = navbarBackground
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