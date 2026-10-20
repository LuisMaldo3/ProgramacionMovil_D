package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Paso 9: los horarios y los servicios se actualizan con Flow, las reglas viven aqui. */
class PedirCitaViewModel(
    private val repo: VetRepository,
    private val duenoId: Int,
    mascotaInicialId: Int? = null
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _mascotaId = MutableStateFlow<Int?>(mascotaInicialId)
    val mascotaId: StateFlow<Int?> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { dia -> repo.observarHorasOcupadas(dia.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _procesando = MutableStateFlow(false)
    val procesando: StateFlow<Boolean> = _procesando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _citaCreada = MutableSharedFlow<Long>(extraBufferCapacity = 1)
    val citaCreada = _citaCreada

    init {
        viewModelScope.launch {
            val lista = mascotas.filter { it.isNotEmpty() }.first()
            if (_mascotaId.value !in lista.map { it.id }) {
                _mascotaId.value = lista.first().id
            }
        }
        viewModelScope.launch {
            val lista = servicios.filter { it.isNotEmpty() }.first()
            // Selecciona vacunacion cuando existe, igual al ejemplo del PDF.
            _servicioId.value = lista.firstOrNull {
                it.nombre.contains("vacun", ignoreCase = true)
            }?.id ?: lista.first().id
        }
    }

    fun elegirMascota(id: Int) {
        if (mascotas.value.any { it.id == id && it.duenoId == duenoId }) {
            _mascotaId.value = id
            _error.value = null
        }
    }

    fun elegirServicio(id: Int) {
        if (servicios.value.any { it.id == id && it.activo }) {
            _servicioId.value = id
            _error.value = null
        }
    }

    fun elegirFecha(dia: LocalDate) {
        val hoy = LocalDate.now()
        if (dia.isBefore(hoy) || dia.isAfter(hoy.plusDays(7))) return
        _fecha.value = dia
        _hora.value = null // obligatorio: no conservar una hora al cambiar de dia
        _error.value = null
    }

    fun horaDeshabilitada(turno: String): Boolean {
        if (turno in horasOcupadas.value) return true
        return try {
            !LocalDateTime.of(_fecha.value, LocalTime.parse(turno))
                .isAfter(LocalDateTime.now())
        } catch (_: Exception) {
            true
        }
    }

    fun elegirHora(turno: String) {
        if (turno in Horarios.todos && !horaDeshabilitada(turno)) {
            _hora.value = turno
            _error.value = null
        }
    }

    fun limpiarError() { _error.value = null }

    fun confirmar() {
        if (_procesando.value) return
        val idMascota = _mascotaId.value
        val idServicio = _servicioId.value
        val turno = _hora.value
        val dia = _fecha.value
        val hoy = LocalDate.now()

        _error.value = when {
            idMascota == null || mascotas.value.none { it.id == idMascota && it.duenoId == duenoId } ->
                "Selecciona una mascota de tu cuenta"
            idServicio == null || servicios.value.none { it.id == idServicio && it.activo } ->
                "Selecciona un servicio disponible"
            dia.isBefore(hoy) || dia.isAfter(hoy.plusDays(7)) ->
                "La fecha debe estar entre hoy y los proximos 7 dias"
            turno == null || turno !in Horarios.todos ->
                "Selecciona una hora"
            horaDeshabilitada(turno) ->
                "El horario esta ocupado o ya paso. Elige otro"
            else -> null
        }
        if (_error.value != null) return

        viewModelScope.launch {
            _procesando.value = true
            try {
                // crearCita comprueba de nuevo la ocupacion DENTRO de withTransaction.
                val nuevaId = repo.crearCita(
                    mascotaId = requireNotNull(idMascota),
                    servicioId = requireNotNull(idServicio),
                    fecha = dia.toString(),
                    hora = requireNotNull(turno),
                    nota = ""
                )
                _citaCreada.emit(nuevaId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _hora.value = null
                _error.value = e.message ?: "No se pudo reservar la cita"
            } finally {
                _procesando.value = false
            }
        }
    }
}
