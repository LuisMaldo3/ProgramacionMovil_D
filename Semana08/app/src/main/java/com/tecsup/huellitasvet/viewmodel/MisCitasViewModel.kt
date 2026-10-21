
package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MisCitasViewModel(
    private val repo: VetRepository,
    private val duenoId: Int
) : ViewModel() {

    private val citas = repo.observarCitasDeDueno(duenoId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val reloj = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(30_000)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocalDateTime.now()
    )

    val proximas: StateFlow<List<CitaConDetalle>> =
        combine(citas, reloj) { todas, ahora ->
            todas.filter { detalle ->
                detalle.cita.estado in setOf(
                    "PENDIENTE",
                    "CONFIRMADA"
                ) && fechaHora(detalle)?.isAfter(ahora) == true
            }.sortedWith(
                compareBy(
                    { it.cita.fecha },
                    { it.cita.hora }
                )
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val historial: StateFlow<List<CitaConDetalle>> =
        combine(citas, reloj) { todas, ahora ->
            todas.filterNot { detalle ->
                detalle.cita.estado in setOf(
                    "PENDIENTE",
                    "CONFIRMADA"
                ) && fechaHora(detalle)?.isAfter(ahora) == true
            }.sortedWith(
                compareByDescending<CitaConDetalle> {
                    it.cita.fecha
                }.thenByDescending {
                    it.cita.hora
                }
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _cancelandoId = MutableStateFlow<Int?>(null)
    val cancelandoId: StateFlow<Int?> =
        _cancelandoId.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> =
        _mensaje.asStateFlow()

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun puedeCancelar(detalle: CitaConDetalle): Boolean {

        if (detalle.mascota.dueno.id != duenoId) {
            return false
        }

        if (detalle.cita.estado !in setOf(
                "PENDIENTE",
                "CONFIRMADA"
            )
        ) {
            return false
        }

        val horaCita = fechaHora(detalle) ?: return false

        return Duration.between(
            LocalDateTime.now(),
            horaCita
        ) > Duration.ofHours(2)
    }

    fun cancelar(detalle: CitaConDetalle) {

        if (_cancelandoId.value != null) return

        viewModelScope.launch {

            _cancelandoId.value = detalle.cita.id

            try {
                val actual = repo.observarCita(
                    detalle.cita.id
                ).first()

                when {
                    actual == null ||
                            actual.mascota.dueno.id != duenoId -> {

                        _mensaje.value =
                            "No se encontró esta cita en tu cuenta"
                    }

                    !puedeCancelar(actual) -> {

                        _mensaje.value =
                            "Ya no puedes cancelar esta cita"
                    }

                    else -> {

                        repo.cambiarEstadoCita(
                            actual.cita.id,
                            EstadoCita.CANCELADA
                        )

                        _mensaje.value =
                            "Cita cancelada correctamente"
                    }
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _mensaje.value =
                    "No se pudo cancelar la cita: " +
                            (e.message ?: "Inténtalo otra vez")

            } finally {
                _cancelandoId.value = null
            }
        }
    }

    private fun fechaHora(
        detalle: CitaConDetalle
    ): LocalDateTime? {

        return runCatching {
            LocalDateTime.of(
                LocalDate.parse(detalle.cita.fecha),
                LocalTime.parse(detalle.cita.hora)
            )
        }.getOrNull()
    }
}
