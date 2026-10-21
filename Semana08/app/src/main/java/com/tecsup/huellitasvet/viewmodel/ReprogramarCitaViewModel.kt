package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.DisponibilidadCitas
import com.tecsup.huellitasvet.model.HorarioOcupado
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReprogramarCitaViewModel(
    private val repo: VetRepository,
    private val duenoId: Int,
    private val citaId: Int
) : ViewModel() {
    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .map { detalle -> detalle?.takeIf { it.mascota.dueno.id == duenoId } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _cargado = MutableStateFlow(false)
    val cargado: StateFlow<Boolean> = _cargado.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val ocupados: StateFlow<List<HorarioOcupado>> = _fecha
        .flatMapLatest { dia -> repo.observarHorariosConDuracion(dia.toString()) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado.asStateFlow()

    init {
        viewModelScope.launch {
            val actual = repo.observarCita(citaId).first()
            if (actual != null && actual.mascota.dueno.id == duenoId) {
                val original = runCatching { LocalDate.parse(actual.cita.fecha) }.getOrNull()
                val hoy = LocalDate.now()
                _fecha.value = if (original != null && !original.isBefore(hoy) &&
                    !original.isAfter(hoy.plusDays(7))) original else hoy
            }
            _cargado.value = true
        }
    }

    fun puedeReprogramar(detalle: CitaConDetalle?): Boolean {
        if (detalle == null || detalle.mascota.dueno.id != duenoId) return false
        if (detalle.cita.estado !in setOf("PENDIENTE", "CONFIRMADA")) return false
        val inicio = runCatching {
            LocalDateTime.of(LocalDate.parse(detalle.cita.fecha), LocalTime.parse(detalle.cita.hora))
        }.getOrNull() ?: return false
        return Duration.between(LocalDateTime.now(), inicio) > Duration.ofHours(2)
    }

    fun elegirFecha(dia: LocalDate) {
        val hoy = LocalDate.now()
        if (dia.isBefore(hoy) || dia.isAfter(hoy.plusDays(7))) return
        _fecha.value = dia
        _hora.value = null
        _error.value = null
    }

    fun disponible(turno: String, ocupacion: List<HorarioOcupado> = ocupados.value): Boolean {
        val detalle = cita.value ?: return false
        if (!puedeReprogramar(detalle)) return false
        return turno in Horarios.todos &&
            DisponibilidadCitas.permitido(_fecha.value, turno, detalle.servicio.duracionMin) &&
            DisponibilidadCitas.libre(turno, detalle.servicio.duracionMin, ocupacion, citaId)
    }

    fun elegirHora(turno: String) {
        if (disponible(turno)) {
            _hora.value = turno
            _error.value = null
        }
    }

    fun confirmar() {
        if (_guardando.value) return
        val detalle = cita.value
        val turno = _hora.value
        if (!puedeReprogramar(detalle)) {
            _error.value = "Esta cita no puede reprogramarse (límite de 2 horas)"
            return
        }
        if (turno == null || !disponible(turno)) {
            _error.value = "Selecciona un horario libre y futuro"
            return
        }
        if (_fecha.value.toString() == detalle?.cita?.fecha && turno == detalle.cita.hora) {
            _error.value = "Elige una fecha u hora diferente a la original"
            return
        }
        viewModelScope.launch {
            _guardando.value = true
            try {
                repo.reprogramarCita(citaId, duenoId, _fecha.value.toString(), turno)
                _guardado.value = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _hora.value = null
                _error.value = e.message ?: "No se pudo reprogramar la cita"
            } finally {
                _guardando.value = false
            }
        }
    }
    fun limpiarError() { _error.value = null }
}
