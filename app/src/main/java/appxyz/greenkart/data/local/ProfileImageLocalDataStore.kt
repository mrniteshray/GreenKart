package appxyz.greenkart.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileImageDataStore by preferencesDataStore(name = "profile_image_prefs")

class ProfileImageLocalDataStore(private val context: Context) {

    fun getProfileImageUri(userId: String): Flow<String> {
        return context.profileImageDataStore.data.map { preferences ->
            preferences[profileImageKey(userId)].orEmpty()
        }
    }

    suspend fun saveProfileImageUri(userId: String, uri: String) {
        context.profileImageDataStore.edit { preferences ->
            preferences[profileImageKey(userId)] = uri
        }
    }

    private fun profileImageKey(userId: String): Preferences.Key<String> {
        return stringPreferencesKey("profile_image_uri_$userId")
    }
}
