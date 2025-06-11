package com.example.myapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.components.PastEventItemUser
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.viewmodels.isEventInPast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastEventsScreen(navController: NavHostController, viewModel: EventViewModel) {
    val allEvents by viewModel.events.collectAsState()
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()

    val finishedAttendedEvents = allEvents.filter { event ->
        event.date.isNotBlank() && event.time.isNotBlank() &&
                isEventInPast(event.date, event.time) &&
                event.attendedStudents.contains(currentUser?.email ?: "")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finished Events") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("user_settings")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to Settings"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (finishedAttendedEvents.isEmpty()) {
                Text("No finished events found.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(finishedAttendedEvents) { event ->
                        val isEnrolled = viewModel.isUserEnrolled(event)
                        PastEventItemUser(
                            event = event,
                            onEnrollClick = {
                                val newEnrollmentState = !isEnrolled
                                if (newEnrollmentState) {
                                    viewModel.enrollToEvent(event.title, context)
                                } else {
                                    viewModel.unenrollFromEvent(event.title, context)
                                }
                            },
                            onCardClick = {
                                navController.navigate("user_event_details/${event.title}")
                            },
                            isEnrolled = isEnrolled
                        )
                    }
                }
            }
        }
    }
}