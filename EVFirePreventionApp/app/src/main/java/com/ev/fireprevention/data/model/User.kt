package com.ev.fireprevention.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)
