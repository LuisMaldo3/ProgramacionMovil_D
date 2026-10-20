package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Paso 7: solo depende de VetRepository, nunca de Room ni de la pantalla. */
class InicioViewModel(
    repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo
        .observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val proximaCita: StateFlow<CitaConDetalle?> = repo
        .observarCitasDeDueno(duenoId)
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.mapNotNull { detalle ->
                val cita = detalle.cita
                val vigente = cita.estado == EstadoCita.PENDIENTE.name ||
                        cita.estado == EstadoCita.CONFIRMADA.name
                if (!vigente) return@mapNotNull null

                val fechaHora = runCatching {
                    LocalDateTime.of(
                        LocalDate.parse(cita.fecha),
                        LocalTime.parse(cita.hora)
                    )
                }.getOrNull() ?: return@mapNotNull null

                if (fechaHora.isBefore(ahora)) null else fechaHora to detalle
            }.minByOrNull { it.first }?.second
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
