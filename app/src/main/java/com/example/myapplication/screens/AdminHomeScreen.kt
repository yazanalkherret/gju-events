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
import com.example.myapplication.EventViewModel
import com.example.myapplication.components.EventItem

@Composable
fun HomeScreen(
    viewModel: EventViewModel,
    onEventClick: (String) -> Unit
               ) {
    // Correct way to observe StateFlow
    val events by viewModel.events.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("All Events", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (events.isEmpty()) {
            Text("No events found")
        } else {
            LazyColumn {
                items(items = events.takeLast(3).reversed()) { event ->
                    EventItem(
                        event = event,
                        onCardClick = { onEventClick(event.id) }
                        )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}