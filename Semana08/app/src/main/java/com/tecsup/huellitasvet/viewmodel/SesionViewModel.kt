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

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    private val _ultimoCorreoRegistrado = MutableStateFlow<String?>(null)
    val ultimoCorreoRegistrado: StateFlow<String?> = _ultimoCorreoRegistrado.asStateFlow()

    private val _recuperacionExitosa = MutableStateFlow(false)
    val recuperacionExitosa: StateFlow<Boolean> = _recuperacionExitosa.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = preferencias.modoOscuro.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    fun limpiarError() {
        _error.value = null
    }

    fun resetearEstadosAuxiliares() {
        _registroExitoso.value = false
        _recuperacionExitosa.value = false
        _error.value = null
    }

    fun registrar(
        nombre: String,
        correo: String,
        clave: String,
        confirmacionClave: String,
        telefono: String,
        distrito: String,
        rolSeleccionado: Rol,
        codigoAdmin: String,
        onExito: () -> Unit
    ) {
        val correoLimpio = correo.trim().lowercase()
        val nombreLimpio = nombre.trim()
        val telLimpio = telefono.trim()
        val distLimpio = distrito.trim()
        _error.value = null

        if (nombreLimpio.isBlank() || correoLimpio.isBlank() || clave.isBlank() || confirmacionClave.isBlank() || telLimpio.isBlank() || distLimpio.isBlank()) {
            _error.value = "Todos los campos (nombre, correo, contraseña, teléfono, distrito) son obligatorios"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches()) {
            _error.value = "Ingresa un correo electrónico válido"
            return
        }

        if (telLimpio.length < 7) {
            _error.value = "Ingresa un número de teléfono válido"
            return
        }

        if (clave.length < 4) {
            _error.value = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        if (clave != confirmacionClave) {
            _error.value = "Las contraseñas no coinciden"
            return
        }

        if (rolSeleccionado == Rol.ADMIN) {
            if (codigoAdmin.trim() != "ADMIN-HUELLITAS-2026") {
                _error.value = "Código de autorización de administrador incorrecto o vacío"
                return
            }
        }

        if (_cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.registrarUsuario(
                    nombre = nombreLimpio,
                    correo = correoLimpio,
                    clave = clave,
                    rol = rolSeleccionado.name,
                    telefono = telLimpio,
                    distrito = distLimpio
                )
                _ultimoCorreoRegistrado.value = correoLimpio
                _registroExitoso.value = true
                onExito()
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudo registrar la cuenta. Inténtalo de nuevo."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun verificarCorreoExiste(correo: String, onResultado: (Usuario?) -> Unit) {
        val correoLimpio = correo.trim().lowercase()
        _error.value = null

        if (correoLimpio.isBlank()) {
            _error.value = "Ingresa tu correo electrónico"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correoLimpio).matches()) {
            _error.value = "Ingresa un correo electrónico válido"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            try {
                val usuario = repo.buscarUsuarioPorCorreo(correoLimpio)
                if (usuario == null) {
                    _error.value = "No existe una cuenta registrada con este correo"
                    onResultado(null)
                } else {
                    _error.value = null
                    onResultado(usuario)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "Error al verificar el correo"
                onResultado(null)
            } finally {
                _cargando.value = false
            }
        }
    }

    fun cambiarContrasena(
        correo: String,
        nuevaClave: String,
        confirmacion: String,
        onExito: () -> Unit
    ) {
        val correoLimpio = correo.trim().lowercase()
        _error.value = null

        if (nuevaClave.isBlank() || confirmacion.isBlank()) {
            _error.value = "Ingresa y confirma la nueva contraseña"
            return
        }

        if (nuevaClave.length < 4) {
            _error.value = "La contraseña debe tener al menos 4 caracteres"
            return
        }

        if (nuevaClave != confirmacion) {
            _error.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            try {
                val exito = repo.actualizarClave(correoLimpio, nuevaClave)
                if (exito) {
                    _recuperacionExitosa.value = true
                    onExito()
                } else {
                    _error.value = "No se pudo actualizar la contraseña"
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "Ocurrió un error al actualizar la contraseña"
            } finally {
                _cargando.value = false
            }
        }
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
                var usuario = repo.login(correoLimpio, clave)

                // Compatibilidad estricta con bases de datos antiguas donde vet@huellitas.test tenía rol ADMIN
                if (usuario != null && correoLimpio == "vet@huellitas.test" && usuario.rol == "ADMIN" && rolSeleccionado == Rol.VETERINARIO) {
                    usuario = usuario.copy(rol = Rol.VETERINARIO.name)
                }

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
            val (correoPrueba, clavePrueba) = when (destino) {
                Rol.CLIENTE -> "ana@correo.test" to "1234"
                Rol.VETERINARIO -> "vet@huellitas.test" to "admin"
                Rol.ADMIN -> "admin@huellitas.test" to "admin123"
            }
            var usuario = repo.login(correoPrueba, clavePrueba)
            if (usuario == null && destino == Rol.ADMIN) {
                usuario = repo.login("admin@huellitas.test", "admin123")
            }
            if (usuario == null) {
                _error.value = "No se encontró la cuenta de práctica"
                false
            } else {
                val rolFinal = if (correoPrueba == "vet@huellitas.test") Rol.VETERINARIO.name else destino.name
                preferencias.guardarSesion(rolFinal, usuario.id, usuario.nombre, usuario.correo)
                _usuarioActual.value = usuario.copy(rol = rolFinal)
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
