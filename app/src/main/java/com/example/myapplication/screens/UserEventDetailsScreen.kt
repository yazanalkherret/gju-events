package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.viewmodels.EventViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.example.myapplication.utils.decodeBase64ToImage
import com.example.myapplication.components.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEventDetailsScreen(
    navController: NavController,
    viewModel: EventViewModel,
    eventTitle: String
) {
    // Fetch event details from ViewModel
    val event = viewModel.getEventByTitle(eventTitle)
    val isEnrolled = event?.let { viewModel.isUserEnrolled(it) } ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Add the enroll button at the bottom
            Button(
                onClick = {
                    if (isEnrolled) {
                        viewModel.unenrollFromEvent(eventTitle) // Use parameter directly
                        {
                            // Navigate back after successful unenrollment
                            navController.popBackStack()
                        }
                    } else {
                        viewModel.enrollToEvent(eventTitle)
                            navController.popBackStack()

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEnrolled) Color(0xFFBDE4FA)
                    else Color(0xFF1F6BAD),
                    contentColor = if (isEnrolled) Color(0xFF1F6BAD)
                    else Color.White
                )
            ) {
                Text(if (isEnrolled) "Unenroll" else "Enroll")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Event Image
            event?.imageBase64?.let { base64 ->
                val imageBitmap = decodeBase64ToImage(base64)
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Event Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        "Failed to load image",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Event Content
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Title
                Text(
                    text = event?.title ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Details Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Date and Time
                    UserDetailItem(
                        label = "Date & Time",
                        value = "${event?.date ?: ""} • ${event?.time ?: ""}"
                    )

                    UserDetailItem(
                        label = "Room",
                        value = event?.room ?: ""
                    )

                    // Description
                    Column {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = event?.description ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Buttons at bottom
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Composable
private fun UserDetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}