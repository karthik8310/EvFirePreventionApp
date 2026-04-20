package com.ev.fireprevention.data

import com.ev.fireprevention.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository(private val firestore: FirebaseFirestore) {

    suspend fun createUser(user: User) {
        firestore.collection("users").document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        return firestore.collection("users").document(uid).get().await().toObject(User::class.java)
    }
}
