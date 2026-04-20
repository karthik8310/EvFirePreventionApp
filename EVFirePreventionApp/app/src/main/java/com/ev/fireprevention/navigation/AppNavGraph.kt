package com.ev.fireprevention.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ev.fireprevention.EVFirePreventionApplication
import com.ev.fireprevention.OnboardingScreen
import com.ev.fireprevention.ui.auth.AuthViewModel
import com.ev.fireprevention.ui.dashboard.DashboardScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as EVFirePreventionApplication
    val sessionManager = application.sessionManager
    val isLoggedIn by sessionManager.getIsLoggedIn().collectAsState(initial = false)

    val startDestination = if (isLoggedIn) "dashboard" else "onboarding"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(navController = navController)
        }
        authNavGraph(navController = navController)
        composable("dashboard") {
            // Integrate with DashboardViewModel
            val dashboardViewModel: com.ev.fireprevention.ui.dashboard.DashboardViewModel = viewModel()
            val state by dashboardViewModel.uiState.collectAsState()

            // Get User Data from SessionManager
            val uid by sessionManager.getUid().collectAsState(initial = "")
            val name by sessionManager.getName().collectAsState(initial = "")
            val email by sessionManager.getEmail().collectAsState(initial = "")
            val phone by sessionManager.getPhone().collectAsState(initial = "")

            val userProfile = com.ev.fireprevention.data.model.User(
                uid = uid ?: "",
                name = name ?: "",
                email = email ?: "",
                phone = phone ?: ""
            )
            
            // AuthViewModel for Logout
            val authViewModel: AuthViewModel = viewModel(factory = application.authViewModelFactory)

            DashboardScreen(
                state = state,
                userProfile = userProfile,
                onNav = { dest -> 
                    // Handle navigation if needed
                },
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true } // Clear back stack
                    }
                }
            )
        }
    }
}
