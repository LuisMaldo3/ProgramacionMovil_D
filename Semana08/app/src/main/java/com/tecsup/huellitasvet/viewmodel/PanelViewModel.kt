package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.DatoGrafico
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(private val repo: VetRepository) : ViewModel() {
    private val hoy = LocalDate.now()
    private val mesInicio = hoy.withDayOfMonth(1).toString()

    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy.toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(mesInicio)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val confirmadas = repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA)
    private val canceladas = repo.observarCantidadPorEstado(EstadoCita.CANCELADA)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        confirmadas,
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        canceladas
    ) { pendiente, confirmada, atendida, cancelada ->
        listOf(
            DatoGrafico("Pendiente", pendiente.toFloat()),
            DatoGrafico("Confirmada", confirmada.toFloat()),
            DatoGrafico("Atendida", atendida.toFloat()),
            DatoGrafico("Cancelada", cancelada.toFloat())
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { servicios ->
            servicios.take(3).map { DatoGrafico(it.nombre, it.cantidad.toFloat()) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porConfirmarHoyManana: StateFlow<Int> = combine(
        repo.observarCitasPorFecha(hoy.toString()),
        repo.observarCitasPorFecha(hoy.plusDays(1).toString())
    ) { hoyLista, mananaLista ->
        (hoyLista + mananaLista).count { it.cita.estado == EstadoCita.PENDIENTE.name }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
