package com.ev.fireprevention

import android.app.Application
import com.ev.fireprevention.data.FirebaseAuthRepository
import com.ev.fireprevention.data.FirestoreRepository
import com.ev.fireprevention.data.SessionManager
import com.ev.fireprevention.ui.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EVFirePreventionApplication : Application() {

    // Create singletons that will live for the lifetime of the application
    lateinit var sessionManager: SessionManager
    lateinit var authViewModelFactory: AuthViewModelFactory
    lateinit var historyRepository: com.ev.fireprevention.data.repository.HistoryRepository

    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)
        val firebaseAuthRepository = FirebaseAuthRepository(FirebaseAuth.getInstance())
        val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
        authViewModelFactory = AuthViewModelFactory(
            firebaseAuthRepository,
            firestoreRepository,
            sessionManager
        )

        val database = com.ev.fireprevention.data.local.AppDatabase.getDatabase(this)
        historyRepository = com.ev.fireprevention.data.repository.HistoryRepository(database.historyDao())
    }
}
