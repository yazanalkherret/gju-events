package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import androidx.compose.runtime.*
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.activities.Forgot_pass_Activity
import com.example.myapplication.activities.Signup_Activity
import com.example.myapplication.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onNavigateToAdmin: () -> Unit,
    onNavigateToHome: () -> Unit

    ){
    val loginSuccessRole by loginViewModel.loginSuccessRole.collectAsState()
    val message by loginViewModel.message.collectAsState()

    val context = LocalContext.current


    var login_email by remember { mutableStateOf("") }
    var login_password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loginViewModel.clearMessage()
        }
    }

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

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.account),
            contentDescription = "Account Icon",
            modifier = Modifier
                .size(width = 133.dp, height = 127.dp)
                .padding(bottom = 16.dp)


        )

        Spacer(modifier = Modifier.height(180.dp))

        TextField(
            value = login_email,
            onValueChange = { login_email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = login_password,
            onValueChange = { login_password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(top = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {

                val image = if(passwordVisible)
                    painterResource(id = R.drawable.visibility_on)
                else
                    painterResource(id = R.drawable.visibility_off)

                IconButton (onClick = { passwordVisible = !passwordVisible }){
                    Icon(painter = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }


            }
            )

        Text(
            text = "Forgot Password",
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, bottom = 8.dp)
                .clickable() {
                    val intent = Intent(context, Forgot_pass_Activity::class.java)
                    context.startActivity(intent)
                },

            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                onClick = {
                    val intent = Intent(context, Signup_Activity::class.java)
                    context.startActivity(intent)
                          },
                modifier = Modifier.weight(1f).height(49.dp),
            ){
                Text("Sign Up")

                }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    loginViewModel.login(login_email, login_password)
                },
                modifier = Modifier.weight(1f).height(49.dp)
            ){
                Text("Login")
            }
        }
    }
}