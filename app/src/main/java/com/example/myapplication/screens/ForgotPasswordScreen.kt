package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodels.ForgotPasswordViewModel


@Composable
fun ForgotPasswordScreen(forgotPasswordViewModel: ForgotPasswordViewModel,
                         onNavigateBack: () -> Unit
){
    val message by forgotPasswordViewModel.message.collectAsState()

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            forgotPasswordViewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = {
                when {
                email.isEmpty() -> {
                    Toast.makeText(context, "Please enter your email.", Toast.LENGTH_SHORT).show()
                    }
                !email.endsWith("@gju.edu.jo") -> {
                    Toast.makeText(context, "Only GJU emails allowed", Toast.LENGTH_SHORT).show()
                    }
                else -> {
                    forgotPasswordViewModel.sendResetEmail(email)
                            }
                        }
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Link")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Back to Login",
            modifier = Modifier.clickable() { onNavigateBack() },
            color = MaterialTheme.colorScheme.primary
        )
    }
}