
package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(
    private val repo: VetRepository
) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> =
        repo.observarServicios()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun alternar(servicio: Servicio) {

        viewModelScope.launch {

            repo.cambiarActivo(
                id = servicio.id,
                activo = !servicio.activo
            )
        }
    }
}
