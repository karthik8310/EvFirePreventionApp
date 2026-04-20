package com.ev.fireprevention.ui.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val signupError by authViewModel.loginError.collectAsState()
    val signupSuccess by authViewModel.signupSuccess.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- Validation Logic ---
    val isEmailValid by derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    val isPhoneValid by derivedStateOf { phone.startsWith("+") && phone.length > 10 }
    val isPasswordValid by derivedStateOf { password.length >= 6 }
    val doPasswordsMatch by derivedStateOf { password == confirmPassword }

    val isFormValid by derivedStateOf {
        name.isNotEmpty() && isEmailValid && isPhoneValid && isPasswordValid && doPasswordsMatch
    }

    LaunchedEffect(signupSuccess) {
        if (signupSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("Account created successfully! Please verify your email.")
                navController.navigate("login") { popUpTo("signup") { inclusive = true } }
                authViewModel.onNavigationComplete()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Compact Logo
            Image(
                painter = painterResource(id = com.ev.fireprevention.R.mipmap.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Welcome Message
            Text(
                text = "Join Us",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Create your account to get started",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                placeholder = { Text("Alice") },
                isError = name.isBlank() && name.isNotEmpty(),
                supportingText = { if (name.isBlank() && name.isNotEmpty()) Text("Name cannot be empty.", color = Color.Red) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

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
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Mobile Number") },
                placeholder = { Text("+911234567890") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = !isPhoneValid && phone.isNotEmpty(),
                supportingText = { if (!isPhoneValid && phone.isNotEmpty()) Text("Include country code (e.g., +91).", color = Color.Red) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !isPasswordValid && password.isNotEmpty(),
                supportingText = { if (!isPasswordValid && password.isNotEmpty()) Text("Password must be at least 6 characters.", color = Color.Red) },
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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = !doPasswordsMatch && confirmPassword.isNotEmpty(),
                supportingText = { if (!doPasswordsMatch && confirmPassword.isNotEmpty()) Text("Passwords do not match.", color = Color.Red) },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            signupError?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.createUserWithEmailAndPassword(name, email, password, phone) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Sign Up")
            }
        }
    }
}
