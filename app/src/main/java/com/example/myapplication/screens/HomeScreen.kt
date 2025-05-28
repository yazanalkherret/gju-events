package com.example.myapplication.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.components.EventItem
import com.example.myapplication.components.Screen
import com.example.myapplication.viewmodels.isEventInPast

@Composable
fun HomeScreen(
    viewModel: EventViewModel,
    navController: NavController
) {

    val events by viewModel.events.collectAsState()
    val activeEvents = events.filter { event ->
        event.date != null && event.time != null &&
                !isEventInPast(event.date!!, event.time!!)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Latest Events", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (activeEvents.isEmpty()) {
            Text("No events found")
        } else {
            LazyColumn {
                items(items = activeEvents.takeLast(3).reversed()) { event ->
                    EventItem(
                        event = event,
                        navController = navController,
                        onCardClick = {
                            navController.navigate(Screen.EventDetails.createRoute(event.id))
                        },
                        onAttendanceClick = {
                            navController.navigate(Screen.Attendance.createRoute(event.id))
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}