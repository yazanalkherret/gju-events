package com.example.myapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.components.EventItemUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun UserHomePage(navController: NavHostController, viewModel: EventViewModel) {
    val auth = Firebase.auth
    val events by viewModel.events.collectAsState()
    val enrollments by viewModel.enrollments.collectAsState()

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

        if (events.isEmpty()) {
            Text("No events found")
        } else {
            LazyColumn {
                items(items = events.takeLast(3).reversed()) { event ->
                    val isEnrolled = event.let { viewModel.isUserEnrolled(it) }

                    EventItemUser(
                        event = event,
                        onEnrollClick = {
                            val newEnrollmentState = !isEnrolled
                            // Immediate UI update
                            if (newEnrollmentState) {
                                viewModel.enrollToEvent(event.id)
                            } else {
                                viewModel.unenrollFromEvent(event.id)
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