package com.ev.fireprevention.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ev.fireprevention.data.FirebaseAuthRepository
import com.ev.fireprevention.data.FirestoreRepository
import com.ev.fireprevention.data.SessionManager
import com.ev.fireprevention.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    private val _signupSuccess = MutableStateFlow(false)
    val signupSuccess = _signupSuccess.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        phone: String
    ) {
        viewModelScope.launch {
            try {
                val authResult = firebaseAuthRepository.createUserWithEmailAndPassword(email, password)
                authResult.user?.let { firebaseUser ->
                    firebaseUser.sendEmailVerification()
                    val user = User(
                        uid = firebaseUser.uid,
                        name = name,
                        email = email,
                        phone = phone
                    )
                    firestoreRepository.createUser(user)
                    _signupSuccess.value = true
                }
            } catch (e: FirebaseAuthException) {
                _loginError.value = when (e.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "This email address is already in use."
                    "ERROR_WEAK_PASSWORD" -> "The password is too weak (must be at least 6 characters)."
                    "ERROR_INVALID_EMAIL" -> "Please enter a valid email address."
                    else -> "Signup failed. Please try again."
                }
            } catch (e: Exception) {
                _loginError.value = "An unexpected error occurred."
            }
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = firebaseAuthRepository.signInWithEmailAndPassword(email, password)
                authResult.user?.let { firebaseUser ->
                    if (firebaseUser.isEmailVerified) {
                        val firestoreUser = firestoreRepository.getUser(firebaseUser.uid)
                        val name = firestoreUser?.name ?: ""
                        val phone = firestoreUser?.phone ?: ""
                        sessionManager.saveSession(firebaseUser.uid, name, firebaseUser.email!!, phone)
                        _loginSuccess.value = true
                    } else {
                        _loginError.value = "Email not verified. Please check your inbox."
                    }
                }
            } catch (e: FirebaseAuthException) {
                _loginError.value = when (e.errorCode) {
                    "ERROR_USER_NOT_FOUND", "ERROR_WRONG_PASSWORD", "ERROR_INVALID_CREDENTIAL" -> "Invalid email or password."
                    else -> "Login failed. Please try again."
                }
            } catch (e: Exception) {
                _loginError.value = "An unexpected error occurred."
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            try {
                FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            } catch (e: Exception) {
                _loginError.value = e.message
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            firebaseAuthRepository.sendPasswordResetEmail(email)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseAuthRepository.signOut()
            sessionManager.clearSession()
        }
    }

    fun onNavigationComplete() {
        _signupSuccess.value = false
        _loginSuccess.value = false
        _loginError.value = null
    }
}
