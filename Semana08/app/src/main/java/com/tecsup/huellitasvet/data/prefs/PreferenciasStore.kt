package com.tecsup.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {
    companion object {
        private val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")
        private val USUARIO_ID = intPreferencesKey("usuarioId")
        private val NOMBRE_USUARIO = stringPreferencesKey("nombreUsuario")
        private val CORREO_USUARIO = stringPreferencesKey("correoUsuario")
        private val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        private val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        private val ACEPTAR_CITAS = booleanPreferencesKey("aceptarCitas")
    }

    val perfilActivo: Flow<String> = context.dataStore.data.map { it[PERFIL_ACTIVO] ?: "" }
    val usuarioId: Flow<Int> = context.dataStore.data.map { it[USUARIO_ID] ?: 0 }
    val nombreUsuario: Flow<String> = context.dataStore.data.map { it[NOMBRE_USUARIO] ?: "" }
    val correoUsuario: Flow<String> = context.dataStore.data.map { it[CORREO_USUARIO] ?: "" }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[MODO_OSCURO] ?: false }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICACIONES] ?: true }
    val aceptarCitas: Flow<Boolean> = context.dataStore.data.map { it[ACEPTAR_CITAS] ?: true }

    suspend fun guardarSesion(rol: String, usuarioId: Int, nombre: String = "", correo: String = "") {
        require(rol == "CLIENTE" || rol == "ADMIN") { "Rol no válido" }
        require(usuarioId > 0) { "Usuario no válido" }
        context.dataStore.edit { p ->
            p[PERFIL_ACTIVO] = rol
            p[USUARIO_ID] = usuarioId
            p[NOMBRE_USUARIO] = nombre
            p[CORREO_USUARIO] = correo
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { it[MODO_OSCURO] = b }
    }
    suspend fun guardarNotificaciones(b: Boolean) {
        context.dataStore.edit { it[NOTIFICACIONES] = b }
    }
    suspend fun guardarAceptarCitas(b: Boolean) {
        context.dataStore.edit { it[ACEPTAR_CITAS] = b }
    }
    suspend fun cerrarSesion() {
        context.dataStore.edit { p ->
            p[PERFIL_ACTIVO] = ""
            p[USUARIO_ID] = 0
            p[NOMBRE_USUARIO] = ""
            p[CORREO_USUARIO] = ""
        }
    }
}
