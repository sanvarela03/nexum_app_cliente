package com.example.neuxum_cliente.user_preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.neuxum_cliente.common.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 30/08/2025
 * @version 1.0
 */
class UserPreferences(private val context: Context) {
    companion object {
        private val FRONT_DOCUMENT_URL = stringPreferencesKey("front_document_url")
        private val BACK_DOCUMENT_URL = stringPreferencesKey("back_document_url")
        private val PROFILE_PICTURE_URL = stringPreferencesKey("profile_picture_url")
    }

    suspend fun saveFrontDocumentUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[FRONT_DOCUMENT_URL] = url
        }
    }

    suspend fun saveBackDocumentUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[BACK_DOCUMENT_URL] = url
        }
    }

    suspend fun saveProfilePictureUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[PROFILE_PICTURE_URL] = url
        }
    }

    fun getFrontDocumentUrl(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[FRONT_DOCUMENT_URL]
        }
    }

    fun getBackDocumentUrl(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[BACK_DOCUMENT_URL]
        }
    }

    fun getProfilePictureUrl(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PROFILE_PICTURE_URL]
        }
    }

}