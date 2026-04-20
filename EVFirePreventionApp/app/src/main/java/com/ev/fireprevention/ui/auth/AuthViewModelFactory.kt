package com.ev.fireprevention.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ev.fireprevention.data.FirebaseAuthRepository
import com.ev.fireprevention.data.FirestoreRepository
import com.ev.fireprevention.data.SessionManager

class AuthViewModelFactory(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(firebaseAuthRepository, firestoreRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
