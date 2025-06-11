package com.example.myapplication.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.components.EventItemUser
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.viewmodels.isEventInPast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun UserHomePage(navController: NavHostController, viewModel: EventViewModel) {
    val auth = Firebase.auth
    val events by viewModel.events.collectAsState()
    val activeEvents = events.filter { event ->
        event.date != null && event.time != null &&
                !isEventInPast(event.date!!, event.time!!)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Latest Events",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeEvents.isEmpty()) {
            Text("No events found")
        } else {
            LazyColumn {
                items(items = activeEvents.reversed()) { event ->
                    val isEnrolled = event.let { viewModel.isUserEnrolled(it) }

                    EventItemUser(
                        event = event,
                        onEnrollClick = {
                            val newEnrollmentState = !isEnrolled
                            // Immediate UI update
                            if (newEnrollmentState) {
                                viewModel.enrollToEvent(event.id,context)
                            } else {
                                viewModel.unenrollFromEvent(event.id,context)
                            }
                        },
                        onCardClick = {
                            navController.navigate("user_event_details/${event.title}") // Pass title
                        },
                        isEnrolled = isEnrolled
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}