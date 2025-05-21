@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.myapplication.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myapplication.components.Event
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.event_planner.utils.rememberImagePicker
import android.util.Base64


@Composable
fun rememberImagePicker(): Pair<Uri?, () -> Unit> {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri.value = uri }
    )

    val pickImage = {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    return Pair(imageUri.value, pickImage)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(viewModel: EventViewModel) {
    var title by rememberSaveable { mutableStateOf("") }
    var room by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    // Date/Time states
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()



    // Image picker
    val (selectedImageUri, pickImage) = rememberImagePicker()
    val context = LocalContext.current

    // Formatters
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Image Picker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { pickImage() },
            contentAlignment = Alignment.Center
        ) {
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Event Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Photo",
                modifier = Modifier.size(64.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Create New Event", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Title Field
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Event Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Standard text field height
        ) {
            OutlinedTextField(
                value = datePickerState.selectedDateMillis?.let {
                    dateFormatter.format(it)
                } ?: "Select Date",
                onValueChange = {},
                label = { Text("Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                enabled = true,  // Critical for maintaining styling
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            // Invisible
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
                    .alpha(0f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Time Picker Field (same structure)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            OutlinedTextField(
                value = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute),
                onValueChange = {},
                label = { Text("Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true },
                readOnly = true,
                enabled = true,  // Critical for maintaining styling
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showTimePicker = true }
                    .alpha(0f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Room Field
        OutlinedTextField(
            value = room,
            onValueChange = { room = it },
            label = { Text("Room") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Description Field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            //maxLines = 3
        )
        Spacer(modifier = Modifier.height(24.dp))

        val base64Image = selectedImageUri?.let { uri ->
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val bytes = stream.readBytes()
                Base64.encodeToString(bytes, Base64.NO_WRAP) // Changed to NO_WRAP
            }
        }

        // Create Button
        Button(
            onClick = {

                val base64Image = selectedImageUri?.let { uri ->
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val bytes = stream.readBytes()
                        Base64.encodeToString(bytes, Base64.NO_WRAP) // Changed to NO_WRAP
                    }
                }


                val calendar = Calendar.getInstance().apply {
                    timeInMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }

                val newEvent = Event(
                    //id = viewModel.events.value.size + 1,
                    title = title,
                    room = room,
                    date = dateFormatter.format(calendar.time),
                    time = timeFormatter.format(calendar.time),
                    description = description,
                    imageBase64 = base64Image
                )
                viewModel.addEvent(
                    event = newEvent,
                    onSuccess = {
                        // Clear fields
                        title = ""
                        room = ""
                        datePickerState.reset()  // Reset date picker
                        timePickerState.reset()
                        description = ""
                        Toast.makeText(context, "Event added successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onError = { e: Exception ->  // Explicitly specify the Exception type here
                        Toast.makeText(
                            context,
                            "Failed to add event: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        println("Upload failed: ${e.message}")
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Event")
        }

        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Time Picker Dialog
        if (showTimePicker) {
            DatePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    Button(onClick = { showTimePicker = false }) {
                        Text("OK")
                    }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}

// Add this extension function
private fun DatePickerState.reset() {
    this.selectedDateMillis = null
}

private fun TimePickerState.reset() {
   // this.hour = LocalTime.now().hour
   // this.minute = LocalTime.now().minute
}