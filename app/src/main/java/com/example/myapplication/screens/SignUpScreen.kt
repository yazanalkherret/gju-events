package com.example.myapplication.screens



import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.viewmodels.SignupViewModel


@Composable
fun SignupScreen(signupViewModel: SignupViewModel,
                 onNavigateToLogin: (String) -> Unit,

) {


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }

    val message by signupViewModel.message.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()


            if (it.contains("Account created")) {
                onNavigateToLogin(email)
            }

            signupViewModel.clearMessage()
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

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = major,
            onValueChange = { major = it },
            label = { Text("Major") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(90.dp))

        Button(
            onClick = {
                        signupViewModel.createAccount(email , password , confirmPassword , major)
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)

                .padding(top = 32.dp)
        ) {
            Text("Create Account")
        }

        Text(
            text = "Already Have An Account?",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable() { onNavigateToLogin(email) },
            color = MaterialTheme.colorScheme.primary
        )

    }
}