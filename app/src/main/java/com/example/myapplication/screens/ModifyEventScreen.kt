@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.myapplication.screens

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.components.Event
import com.example.myapplication.viewmodels.EventViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyEventScreen(
    navController: NavController,
    viewModel: EventViewModel,
    eventId: String
) {
    val context = LocalContext.current
    val event = remember { viewModel.getEventById(eventId) }

    // Show error if event is not found
    if (event == null) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Event not found", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        return
    }

    val currentEvent = event

    // Form states
    var title by rememberSaveable { mutableStateOf(event.title) }
    var room by rememberSaveable { mutableStateOf(event.room) }
    var description by rememberSaveable { mutableStateOf(event.description) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var base64Image by rememberSaveable { mutableStateOf(event.imageBase64) }

    // Date/Time initialization
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    // Parse existing date/time
    val initialDate = try {
        dateFormatter.parse(event.date)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }

    val initialTime = try {
        val calendar = Calendar.getInstance()
        timeFormatter.parse(event.time)?.let {
            calendar.time = it
        }
        calendar
    } catch (e: Exception) {
        Calendar.getInstance()
    }

    // Date/Time states
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialTime.get(Calendar.MINUTE)
    )

    // Image handling
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val bytes = stream.readBytes()
                base64Image = Base64.encodeToString(bytes, Base64.NO_WRAP)
            }
        }
    }
Column(
    modifier = Modifier.verticalScroll(rememberScrollState())
){


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Image Picker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            contentAlignment = Alignment.Center
        ) {
            when {
                selectedImageUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Event Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                !base64Image.isNullOrEmpty() -> {
                    val bitmap = remember {
                        try {
                            val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Event Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        Text("Invalid image data")
                    }
                }

                else -> {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Edit Event", style = MaterialTheme.typography.headlineMedium)
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
                    .height(56.dp)
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
                    enabled = true,
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
                        .clickable { showDatePicker = true }
                        .alpha(0f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Picker Field
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
                    enabled = true,
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
                modifier = Modifier.fillMaxWidth().height(256.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Update Button
            Button(
                onClick = {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = datePickerState.selectedDateMillis ?: initialDate
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }
                    val updatedEvent = Event(
                        id = eventId,
                        title = title,
                        room = room,
                        date = dateFormatter.format(calendar.time),
                        time = timeFormatter.format(calendar.time),
                        description = description,
                        imageBase64 = base64Image
                    )
                    viewModel.updateEvent(
                        event = updatedEvent,
                        onSuccess = {
                            Toast.makeText(context, "Event updated!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { e ->
                            Toast.makeText(
                                context,
                                "Update failed: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F6BAD),
                    contentColor = Color.White
                )
            ) {
                Text("Update Event")
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

}
// Extension functions for reset (if needed)
private fun DatePickerState.reset() {
    this.selectedDateMillis = null
}

private fun TimePickerState.reset() {
    // Implementation if needed
}