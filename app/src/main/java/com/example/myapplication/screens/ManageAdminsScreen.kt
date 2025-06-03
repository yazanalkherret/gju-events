// Add to your Screens package as ManageAdminsScreen.kt
package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodels.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun ManageAdminsScreen(userViewModel: UserViewModel = viewModel()) {
    val db = FirebaseFirestore.getInstance()
    var email by remember { mutableStateOf("") }
    var admins by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load current admins
    LaunchedEffect(Unit) {
        db.collection("users")
            .whereEqualTo("role", "admin")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                admins = snapshot?.documents?.map {
                    Pair(it.id, it.getString("email") ?: "")
                } ?: emptyList()
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Add Admin Section
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("User Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                errorMessage = "User not found"
                            } else {
                                for (document in documents) {
                                    db.collection("users").document(document.id)
                                        .update("role", "admin")
                                        .addOnSuccessListener {
                                            email = ""
                                            errorMessage = null
                                        }
                                }
                            }
                        }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1F6BAD),
                contentColor = Color.White
            )) {
            Text("Add Admin")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Current Admins List
        Text(
            text = "Current Admins",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(admins) { (userId, userEmail) ->
                AdminListItem(
                    email = userEmail,
                    onRemove = {
                        db.collection("users").document(userId)
                            .update("role", "user")
                    }
                )
            }
        }
    }
}

@Composable
fun AdminListItem(email: String, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBDE4FA) // Light blue background
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Text(
                text = email,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Admin"
                )
            }
        }
    }
}