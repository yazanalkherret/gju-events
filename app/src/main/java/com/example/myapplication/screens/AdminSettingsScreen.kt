package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.components.Screen
import com.example.myapplication.R

@Composable
fun SettingsScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val userData by userViewModel.userData.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )


                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = userData?.fullName ?: "Loading...",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = userData?.email ?: "Loading...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            SettingsButton(
                //icon = Icons.Filled.Lock,
                text = "Past Events",
                onClick = {navController.navigate(Screen.AdminPastEvents.route)}
            )
            SettingsButton(
                //icon = Icons.Filled.Lock,
                text = "Manage Admins",
                onClick = {
                    navController.navigate(Screen.ManageAdmins.route)
                }
            )

            SettingsButton(
                //icon = Icons.Filled.ExitToApp,
                text = "Logout",
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    onLogout()
                }
            )


        }
    }
}

@Composable
fun SettingsButton(/*icon: ImageVector, */text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)

            .padding(vertical = 4.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1F6BAD),
            contentColor = Color.White // Optional: icon/text color
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            /* Icon(
                 imageVector = icon,
                 contentDescription = text,
                 modifier = Modifier.size(24.dp),
                 tint = Color.White
             )*/
            //Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                textAlign = TextAlign.Center,
                //fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = Color.White
            )
            /*Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Action",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )*/
        }
    }
}