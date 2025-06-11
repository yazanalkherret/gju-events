package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodels.ForgotPasswordViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(forgotPasswordViewModel: ForgotPasswordViewModel,
                         onNavigateBack: () -> Unit
){
    val primaryBlue = Color(0xFF1F6BAD)
    val message by forgotPasswordViewModel.message.collectAsState()

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            forgotPasswordViewModel.clearMessage()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // centers content both vertically & horizontally
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your email address") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryBlue,
                    focusedLabelColor = primaryBlue,
                    unfocusedBorderColor = primaryBlue,
                    cursorColor = primaryBlue
                )
            )



            Button(
                onClick = {
                    when {
                        email.isEmpty() -> {
                            Toast.makeText(context, "Please enter your email.", Toast.LENGTH_SHORT)
                                .show()
                        }

                        !email.endsWith("@gju.edu.jo") -> {
                            Toast.makeText(context, "Only GJU emails allowed", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            forgotPasswordViewModel.sendResetEmail(email)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F6BAD),
                    contentColor = Color.White
                )
            ) {
                Text("Send Link")
            }


            Text(
                text = "Back to Login",
                modifier = Modifier.clickable() { onNavigateBack() },
                color = Color(0xFF1F6BAD)
            )
        }
    }
}