package com.example.myapplication.components



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.utils.decodeBase64ToImage

@Composable
fun EventItemUser(
    event: Event,
    onEnrollClick: () -> Unit,
    onCardClick: () -> Unit,
    isEnrolled: Boolean
) {

    var localEnrolled by remember { mutableStateOf(isEnrolled) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(vertical = 8.dp)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left Column (Image + Date/Time)
                Column(modifier = Modifier.width(160.dp)) {
                    val imageBase64 = event.imageBase64
                    if (imageBase64 != null) {
                        val imageBitmap = decodeBase64ToImage(imageBase64)
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "Event Image",
                                modifier = Modifier
                                    .size(160.dp, 160.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Base64 string exists but decoding failed
                            NoImagePlaceholder()
                        }
                    } else {
                        // No Base64 string at all
                        NoImagePlaceholder()
                    }
                }

                // Right Column (Content)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${event.date} ${event.time}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = event.room,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF1F6BAD)
                )
            }

            // Buttons Row
            Button(
                onClick = {
                    localEnrolled = !localEnrolled // Immediate toggle
                    onEnrollClick()
                },
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (localEnrolled) Color(0xFFBDE4FA)
                    else Color(0xFF1F6BAD),
                    contentColor = if (localEnrolled) Color(0xFF1F6BAD)
                    else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (localEnrolled) "Unenroll" else "Enroll",
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}

@Composable
fun NoImagePlaceholder() {
    Box(
        modifier = Modifier
            .size(160.dp, 160.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFFF1F9FE)),
        contentAlignment = Alignment.Center
    ) {
        Text("No Image", color = Color.Black)
    }
}