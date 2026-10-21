package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: VetRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = preferencias.modoOscuro.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    fun limpiarError() {
        _error.value = null
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        val correoLimpio = correo.trim().lowercase()
        _error.value = null

        if (correoLimpio.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        if (_cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            try {
                val usuario = repo.login(correoLimpio, clave)

                when {
                    usuario == null -> {
                        _error.value = "Correo o contraseña incorrectos"
                    }
                    usuario.rol != rolSeleccionado.name -> {
                        _error.value = "Selecciona el perfil que corresponde a esta cuenta"
                    }
                    else -> {
                        preferencias.guardarSesion(usuario.rol, usuario.id, usuario.nombre, usuario.correo)
                        _usuarioActual.value = usuario
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudo iniciar sesión. Vuelve a intentarlo"
            } finally {
                _cargando.value = false
            }
        }
    }

    // Cambio de perfil SOLO para el modo práctica local del laboratorio.
    // Nunca utilizar credenciales de prueba fijas en una aplicación real.
    suspend fun cambiarPerfilPractica(destino: Rol): Boolean {
        return try {
            val usuario = when (destino) {
                Rol.CLIENTE -> repo.login("ana@correo.test", "1234")
                Rol.ADMIN -> repo.login("vet@huellitas.test", "admin")
            }
            if (usuario == null || usuario.rol != destino.name) {
                _error.value = "No se encontró la cuenta de práctica del otro perfil"
                false
            } else {
                preferencias.guardarSesion(usuario.rol, usuario.id, usuario.nombre, usuario.correo)
                _usuarioActual.value = usuario
                _error.value = null
                true
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _error.value = e.message ?: "No se pudo cambiar de perfil"
            false
        }
    }

    // El Splash usa este valor después de su animación inicial.
    suspend fun recuperarRolGuardado(): Rol? {
        val perfil = preferencias.perfilActivo.first()
        val id = preferencias.usuarioId.first()
        if (id <= 0) return null
        return Rol.entries.firstOrNull { it.name == perfil }
    }

    suspend fun cerrarSesion() {
        preferencias.cerrarSesion()
        _usuarioActual.value = null
        _error.value = null
    }
}
