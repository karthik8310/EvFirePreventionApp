package com.ev.fireprevention.ui.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    val loginSuccess by authViewModel.loginSuccess.collectAsState()
    val loginError by authViewModel.loginError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    val isPasswordValid by derivedStateOf { password.isNotEmpty() }
    val isFormValid by derivedStateOf { isEmailValid && isPasswordValid }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("Login Successful!")
                navController.navigate("dashboard") { popUpTo("auth") { inclusive = true } }
                authViewModel.onNavigationComplete()
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = com.ev.fireprevention.R.mipmap.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // App Name
            Text(
                text = "EV Fire Prevention",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Battery Health Monitoring",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("alice@example.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isEmailValid && email.isNotEmpty(),
                supportingText = { if (!isEmailValid && email.isNotEmpty()) Text("Invalid email address.", color = Color.Red) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = loginError != null,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            loginError?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                    if (it.contains("Email not verified")) {
                        TextButton(onClick = { authViewModel.resendVerificationEmail() }) {
                            Text("Resend Verification Email")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.loginWithEmailAndPassword(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Don't have an account? Sign up")
            }
            TextButton(onClick = { navController.navigate("forgot_password") }) {
                Text("Forgot Password?")
            }
        }
    }
}
