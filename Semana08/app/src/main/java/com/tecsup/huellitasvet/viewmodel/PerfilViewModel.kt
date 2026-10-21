package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

/** Estadísticas de perfil a través de la interfaz Repository; la View no consulta datos. */
data class ResumenPerfil(val mascotas: Int = 0, val proximas: Int = 0, val atenciones: Int = 0)

class PerfilViewModel(repo: VetRepository, duenoId: Int) : ViewModel() {
    val resumen: StateFlow<ResumenPerfil> = combine(
        repo.observarMascotasDeDueno(duenoId),
        repo.observarCitasDeDueno(duenoId)
    ) { mascotas, citas ->
        val ahora = LocalDateTime.now()
        ResumenPerfil(
            mascotas = mascotas.size,
            proximas = citas.count { detalle ->
                detalle.cita.estado in setOf("PENDIENTE", "CONFIRMADA") &&
                    runCatching {
                        LocalDateTime.parse("${detalle.cita.fecha}T${detalle.cita.hora}").isAfter(ahora)
                    }.getOrDefault(false)
            },
            atenciones = citas.count { it.cita.estado == "ATENDIDA" }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumenPerfil())
}
