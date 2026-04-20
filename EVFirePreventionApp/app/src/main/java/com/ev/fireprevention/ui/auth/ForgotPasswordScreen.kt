package com.ev.fireprevention.ui.auth

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Lock Icon
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Reset",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Title
            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Help Text
            Text(
                text = "No worries! Enter your email address and we'll send you a link to reset your password.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your registered email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isEmailValid && email.isNotEmpty(),
                supportingText = { if (!isEmailValid && email.isNotEmpty()) Text("Invalid email address.", color = Color.Red) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.sendPasswordResetEmail(email)
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Password reset email sent successfully!",
                            actionLabel = "OK",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEmailValid
            ) {
                Text("Reset Password")
            }
        }
    }
}
