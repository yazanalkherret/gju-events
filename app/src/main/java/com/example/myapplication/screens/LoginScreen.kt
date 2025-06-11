package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.activities.Forgot_pass_Activity
import com.example.myapplication.activities.Signup_Activity
import com.example.myapplication.viewmodels.LoginViewModel
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onNavigateToAdmin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val loginSuccessRole by loginViewModel.loginSuccessRole.collectAsState()
    val message by loginViewModel.message.collectAsState()
    val context = LocalContext.current

    var login_email by remember { mutableStateOf("") }
    var login_password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Toast for login message
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loginViewModel.clearMessage()
        }
    }

    // Navigate based on login role
    LaunchedEffect(loginSuccessRole) {
        loginSuccessRole?.let { role ->
            if (role == "admin") {
                onNavigateToAdmin()
            } else {
                onNavigateToHome()
            }
            loginViewModel.clearLoginSuccess()
        }
    }

    val primaryBlue = Color(0xFF1F6BAD)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Profile",
            modifier = Modifier
                .size(184.dp)
                .clip(CircleShape)
        )

        OutlinedTextField(
            value = login_email,
            onValueChange = { login_email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
            ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryBlue,
                focusedLabelColor = primaryBlue,
                unfocusedBorderColor = Color(0xFF1F6BAD),
                cursorColor = primaryBlue
            )
        )

        OutlinedTextField(
            value = login_password,
            onValueChange = { login_password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
            ,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (passwordVisible)
                    painterResource(id = R.drawable.visibility_on)
                else
                    painterResource(id = R.drawable.visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = icon,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF1F6BAD),
                focusedLabelColor = primaryBlue,
                cursorColor = primaryBlue,
                unfocusedBorderColor = Color(0xFF1F6BAD)
            )
        )

        Text(
            text = "Forgot Password",
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    val intent = Intent(context, Forgot_pass_Activity::class.java)
                    context.startActivity(intent)
                },
            color = primaryBlue,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { loginViewModel.login(login_email, login_password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryBlue,
                contentColor = Color.White
            )
        ) {
            Text("Login")
        }

        Text(
            text = "Don't have an account? Sign Up",
            modifier = Modifier.clickable {
                val intent = Intent(context, Signup_Activity::class.java)
                context.startActivity(intent)
            },
            color = primaryBlue
        )
    }
}