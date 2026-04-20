package com.ev.fireprevention.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ev.fireprevention.EVFirePreventionApplication
import com.ev.fireprevention.ui.auth.*

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(startDestination = "login", route = "auth") {
        composable("login") {
            val application = LocalContext.current.applicationContext as EVFirePreventionApplication
            val authViewModel: AuthViewModel = viewModel(factory = application.authViewModelFactory)
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("signup") {
            val application = LocalContext.current.applicationContext as EVFirePreventionApplication
            val authViewModel: AuthViewModel = viewModel(factory = application.authViewModelFactory)
            SignupScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("forgot_password") {
            val application = LocalContext.current.applicationContext as EVFirePreventionApplication
            val authViewModel: AuthViewModel = viewModel(factory = application.authViewModelFactory)
            ForgotPasswordScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}
