package com.example.lmsapp.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to delegate DataStore creation to the Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_auth_prefs")

class AuthRepository(private val context: Context) {

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        // You could add other keys here, e.g., USER_ID_KEY, USER_EMAIL_KEY
    }

    /**
     * Flow to observe the authentication token.
     * Emits the token if it exists, or null otherwise.
     */
    val authToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[AUTH_TOKEN_KEY]
        }

    /**
     * Saves the authentication token to DataStore.
     * @param token The token to save.
     */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    /**
     * Clears the authentication token from DataStore.
     * Useful for logout.
     */
    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }

    // Example: A simple synchronous check if a token exists.
    // Note: Prefer observing the `authToken` Flow for reactive updates.
    // This might be useful for initial checks, but be mindful of blocking UI thread if used improperly.
    // For ViewModels, it's better to collect the Flow.
    // suspend fun hasToken(): Boolean {
    //     return context.dataStore.data.firstOrNull()?.get(AUTH_TOKEN_KEY) != null
    // }
}
