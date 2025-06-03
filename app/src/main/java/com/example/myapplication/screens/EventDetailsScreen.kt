package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.NoImagePlaceholder
import com.example.myapplication.components.Screen
import com.example.myapplication.utils.decodeBase64ToImage
import com.example.myapplication.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(navController: NavController, viewModel: EventViewModel, eventId: String) {
    val event = viewModel.getEventById(eventId)
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                        NoImagePlaceholder()
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
                    DetailItem(
                        label = "Date & Time",
                        value = "${event?.date ?: ""} • ${event?.time ?: ""}"
                    )

                    DetailItem(
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
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Button(
                            onClick = { navController.navigate(Screen.Attendance.createRoute(eventId)) },
                            modifier = Modifier.width(190.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1F6BAD),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Attendance")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedButton(
                            onClick = { navController.navigate(Screen.ModifyEvent.createRoute(eventId)) },
                            modifier = Modifier.width(190.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1F6BAD) // text/icon color
                            ),
                            border = BorderStroke(1.dp, Color(0xFF1F6BAD)) // border color and thickness

                        ) {
                            Text("Modify")
                        }
                    }

                    // Delete Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.fillMaxWidth(), // Match width of other buttons
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text("Delete Event")
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Event") },
                text = { Text("Are you sure you want to delete this event?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.deleteEvent(
                                eventId = eventId,
                                onSuccess = {
                                    navController.popBackStack()
                                    Toast.makeText(
                                        context,
                                        "Event deleted successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onError = { e ->
                                    Toast.makeText(
                                        context,
                                        "Delete failed: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
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