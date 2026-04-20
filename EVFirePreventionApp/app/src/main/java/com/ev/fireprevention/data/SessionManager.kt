package com.ev.fireprevention.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val UID = stringPreferencesKey("uid")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone")
    }

    suspend fun saveSession(
        uid: String,
        name: String,
        email: String,
        phone: String
    ) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[UID] = uid
            it[NAME] = name
            it[EMAIL] = email
            it[PHONE] = phone
        }
    }

    fun getIsLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[IS_LOGGED_IN] ?: false
        }
    }

    fun getUid(): Flow<String?> {
        return context.dataStore.data.map {
            it[UID]
        }
    }

    fun getName(): Flow<String?> {
        return context.dataStore.data.map {
            it[NAME]
        }
    }

    fun getEmail(): Flow<String?> {
        return context.dataStore.data.map {
            it[EMAIL]
        }
    }

    fun getPhone(): Flow<String?> {
        return context.dataStore.data.map {
            it[PHONE]
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
