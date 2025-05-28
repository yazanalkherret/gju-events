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
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.components.EventItem
import com.example.myapplication.components.EventItemUser

@Composable
fun UserMyEvents(navController: NavHostController, viewModel: EventViewModel) {
    val enrolledEvents by viewModel.enrolledEvents.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("My Registered Events", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (enrolledEvents.isEmpty()) {
            Text("No events found")
        } else {
            LazyColumn {
                items(items = enrolledEvents) { event ->
                    EventItemUser(
                        event = event,
                        onEnrollClick = { viewModel.unenrollFromEvent(event.id) },
                        onCardClick = { navController.navigate("user_event_details/${event.title}") },
                        isEnrolled = true
                        //viewModel = EventViewModel()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}