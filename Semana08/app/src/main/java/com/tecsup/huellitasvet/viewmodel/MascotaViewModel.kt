package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Paso 8: validaciones en el ViewModel y operaciones solamente a traves de VetRepository. */
class MascotaViewModel(
    private val repo: VetRepository,
    val duenoId: Int,
    val mascotaId: Int? = null
) : ViewModel() {

    val mascota: StateFlow<Mascota?> = (
            if (mascotaId != null && mascotaId > 0) {
                repo.observarMascota(mascotaId).map { m ->
                    m?.takeIf { it.duenoId == duenoId }
                }
            } else {
                flowOf(null)
            }
            ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val historiales = mutableMapOf<Int, StateFlow<List<CitaConDetalle>>>()
    private val historialesPeso = mutableMapOf<Int, StateFlow<List<com.tecsup.huellitasvet.model.PuntoPeso>>>()

    fun historial(id: Int): StateFlow<List<CitaConDetalle>> = synchronized(historiales) {
        historiales.getOrPut(id) {
            val fuente: Flow<List<CitaConDetalle>> = if (id > 0) {
                repo.observarHistorial(id)
            } else {
                flowOf(emptyList())
            }
            fuente.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }
    }

    fun historialPeso(id: Int): StateFlow<List<com.tecsup.huellitasvet.model.PuntoPeso>> = synchronized(historialesPeso) {
        historialesPeso.getOrPut(id) {
            val fuente: Flow<List<com.tecsup.huellitasvet.model.PuntoPeso>> = if (id > 0) {
                repo.observarHistorialPeso(id)
            } else {
                flowOf(emptyList())
            }
            fuente.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        }
    }

    private val _errores = MutableStateFlow<Map<String, String>>(emptyMap())
    val errores: StateFlow<Map<String, String>> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando: StateFlow<Boolean> = _procesando.asStateFlow()

    private val _guardado = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val guardado = _guardado.asSharedFlow()

    private val _eliminado = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val eliminado = _eliminado.asSharedFlow()

    fun limpiarMensaje() { _mensaje.value = null }
    fun limpiarErrores() { _errores.value = emptyMap() }

    fun guardar(nueva: Mascota) {
        if (_procesando.value) return
        val errores = mutableMapOf<String, String>()
        if (nueva.nombre.trim().length < 2) errores["nombre"] = "Escribe al menos 2 caracteres"
        if (Especie.entries.none { it.name == nueva.especie }) errores["especie"] = "Elige una especie"
        if (Sexo.entries.none { it.name == nueva.sexo }) errores["sexo"] = "Elige el sexo"
        if (nueva.anioNacimiento !in 1990..LocalDate.now().year) {
            errores["anioNacimiento"] = "Año entre 1990 y ${LocalDate.now().year}"
        }
        if (!nueva.peso.isFinite() || nueva.peso <= 0) errores["peso"] = "El peso debe ser mayor que 0"
        if (nueva.duenoId != duenoId || duenoId <= 0) {
            _mensaje.value = "Sesión no válida para esta mascota"
            return
        }
        _errores.value = errores
        if (errores.isNotEmpty()) return

        viewModelScope.launch {
            _procesando.value = true
            _mensaje.value = null
            try {
                // La mascota que se edita debe pertenecer al dueño autenticado.
                if (nueva.id != 0) {
                    val existente = repo.observarMascota(nueva.id).first()
                    require(existente?.duenoId == duenoId) { "No puedes editar esta mascota" }
                }
                repo.guardarMascota(nueva.copy(nombre = nueva.nombre.trim(), raza = nueva.raza.trim()))
                _guardado.emit(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "No se pudo guardar la mascota"
            } finally {
                _procesando.value = false
            }
        }
    }

    fun eliminar(m: Mascota) {
        if (_procesando.value) return
        if (m.duenoId != duenoId || m.id <= 0) {
            _mensaje.value = "Esta mascota no pertenece a tu cuenta"
            return
        }
        viewModelScope.launch {
            _procesando.value = true
            _mensaje.value = null
            try {
                if (repo.mascotaTieneCitas(m.id)) {
                    _mensaje.value = "No puedes eliminar esta mascota porque tiene citas registradas"
                } else {
                    repo.eliminarMascota(m)
                    _eliminado.emit(Unit)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "No se pudo eliminar la mascota"
            } finally {
                _procesando.value = false
            }
        }
    }
}
