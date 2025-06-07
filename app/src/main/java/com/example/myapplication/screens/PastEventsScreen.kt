package com.example.myapplication.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.components.EventItem
import com.example.myapplication.components.EventItemUser
import com.example.myapplication.components.PastEventItemUser
import com.example.myapplication.components.Screen
import com.example.myapplication.viewmodels.isEventInPast
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastEventsScreen(navController: NavHostController, viewModel: EventViewModel) {
    val allEvents by viewModel.events.collectAsState()
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()

    val finishedAttendedEvents = allEvents.filter { event ->
        event.date.isNotBlank() && event.time.isNotBlank() &&
                isEventInPast(event.date, event.time) &&
                event.attendedStudents.contains(currentUser.email)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finished Events") }
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
                                // Immediate UI update
                                if (newEnrollmentState) {
                                    viewModel.enrollToEvent(event.title, context )
                                } else {
                                    viewModel.unenrollFromEvent(event.title,context)
                                }
                            },
                            onCardClick = {
                                navController.navigate("user_event_details/${event.title}") // Pass title
                            },
                            isEnrolled = isEnrolled
                        )
                    }
                }
            }
        }
    }
}



