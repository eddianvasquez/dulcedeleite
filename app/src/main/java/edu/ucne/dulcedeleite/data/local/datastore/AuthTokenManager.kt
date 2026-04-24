package edu.ucne.dulcedeleite.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthTokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
        val USUARIO_ID_KEY = androidx.datastore.preferences.core.intPreferencesKey("usuario_id")
        val USER_NOMBRE_KEY = stringPreferencesKey("user_nombre")
        val USER_CORREO_KEY = stringPreferencesKey("user_correo")
    }

    suspend fun saveTokenAndRole(token: String, rol: String, usuarioId: Int, nombre: String, correo: String) {
        context.dataStore.edit { preferences ->
            preferences[JWT_TOKEN_KEY] = token
            preferences[USER_ROLE_KEY] = rol
            preferences[USUARIO_ID_KEY] = usuarioId
            preferences[USER_NOMBRE_KEY] = nombre
            preferences[USER_CORREO_KEY] = correo
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
            preferences.remove(USER_ROLE_KEY)
            preferences.remove(USUARIO_ID_KEY)
            preferences.remove(USER_NOMBRE_KEY)
            preferences.remove(USER_CORREO_KEY)
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY]
        }
    }

    fun getRole(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE_KEY]
        }
    }

    fun getUsuarioId(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[USUARIO_ID_KEY]
        }
    }

    fun getNombre(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NOMBRE_KEY]
        }
    }

    fun getCorreo(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_CORREO_KEY]
        }
    }
}
