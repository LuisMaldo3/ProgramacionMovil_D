
package com.tecsup.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Instancia unica de DataStore
val Context.dataStore by preferencesDataStore(
    name = "ajustes"
)

class PreferenciasStore(
    private val context: Context
) {

    companion object {

        private val PERFIL_ACTIVO =
            stringPreferencesKey("perfilActivo")

        private val USUARIO_ID =
            intPreferencesKey("usuarioId")

        private val MODO_OSCURO =
            booleanPreferencesKey("modoOscuro")

        private val NOTIFICACIONES =
            booleanPreferencesKey("notificaciones")

        private val ACEPTAR_CITAS =
            booleanPreferencesKey("aceptarCitas")
    }

    val perfilActivo: Flow<String> =
        context.dataStore.data.map { preferencias ->
            preferencias[PERFIL_ACTIVO] ?: ""
        }

    val usuarioId: Flow<Int> =
        context.dataStore.data.map { preferencias ->
            preferencias[USUARIO_ID] ?: 0
        }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[MODO_OSCURO] ?: false
        }

    val notificaciones: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[NOTIFICACIONES] ?: true
        }

    val aceptarCitas: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[ACEPTAR_CITAS] ?: true
        }


    suspend fun guardarSesion(
        rol: String,
        usuarioId: Int
    ) {
        require(rol == "CLIENTE" || rol == "ADMIN") {
            "El rol debe ser CLIENTE o ADMIN"
        }

        require(usuarioId > 0) {
            "El identificador del usuario no es valido"
        }

        context.dataStore.edit { preferencias ->
            preferencias[PERFIL_ACTIVO] = rol
            preferencias[USUARIO_ID] = usuarioId
        }
    }


    suspend fun guardarModoOscuro(
        b: Boolean
    ) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_OSCURO] = b
        }
    }


    suspend fun guardarNotificaciones(
        b: Boolean
    ) {
        context.dataStore.edit { preferencias ->
            preferencias[NOTIFICACIONES] = b
        }
    }


    suspend fun guardarAceptarCitas(
        b: Boolean
    ) {
        context.dataStore.edit { preferencias ->
            preferencias[ACEPTAR_CITAS] = b
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias[PERFIL_ACTIVO] = ""
            preferencias[USUARIO_ID] = 0
        }
    }
}
