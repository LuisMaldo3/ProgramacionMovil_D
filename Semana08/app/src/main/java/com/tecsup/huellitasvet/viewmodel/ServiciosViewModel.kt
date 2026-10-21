package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando = _procesando.asStateFlow()

    private val _guardado = MutableStateFlow(false)
    val guardado = _guardado.asStateFlow()

    fun limpiarMensaje() { _mensaje.value = null }
    fun limpiarGuardado() { _guardado.value = false }

    fun alternar(servicio: Servicio) {
        viewModelScope.launch {
            try {
                repo.cambiarActivo(servicio.id, !servicio.activo)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _mensaje.value = "No se pudo cambiar el estado del servicio" }
        }
    }

    fun guardar(servicio: Servicio) {
        if (_procesando.value) return
        _mensaje.value = when {
            servicio.nombre.isBlank() -> "El nombre es obligatorio"
            servicio.duracionMin !in listOf(15, 30, 45, 60) -> "Selecciona una duración válida"
            !servicio.precio.isFinite() || servicio.precio <= 0 -> "El precio debe ser mayor que cero"
            servicio.icono !in listOf("heart", "syringe", "scissors") -> "Selecciona un ícono"
            else -> null
        }
        if (_mensaje.value != null) return
        viewModelScope.launch {
            _procesando.value = true
            try {
                repo.guardarServicio(servicio.copy(nombre = servicio.nombre.trim()))
                _guardado.value = true
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _mensaje.value = e.message ?: "No se pudo guardar el servicio" }
            finally { _procesando.value = false }
        }
    }

    fun eliminar(servicio: Servicio) {
        if (_procesando.value) return
        viewModelScope.launch {
            _procesando.value = true
            try {
                if (repo.servicioTieneCitas(servicio.id)) {
                    repo.cambiarActivo(servicio.id, false)
                    _mensaje.value = "Este servicio tiene citas; no se eliminó, solo se desactivó"
                } else {
                    repo.eliminarServicio(servicio)
                    _guardado.value = true
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _mensaje.value = e.message ?: "No se pudo eliminar el servicio" }
            finally { _procesando.value = false }
        }
    }
}
