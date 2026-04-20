package com.ev.fireprevention.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class FirebaseAuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun sendOtp(
        phoneNumber: String,
        activity: android.app.Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

    suspend fun signInWithEmailAndPassword(email: String, password: String) = firebaseAuth.signInWithEmailAndPassword(email, password).await()

    suspend fun sendPasswordResetEmail(email: String) = firebaseAuth.sendPasswordResetEmail(email).await()

    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) = firebaseAuth.signInWithCredential(credential).await()

    fun signOut() = firebaseAuth.signOut()
}
