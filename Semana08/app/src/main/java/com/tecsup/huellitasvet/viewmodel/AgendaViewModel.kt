package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {
    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()
    private val _procesandoId = MutableStateFlow<Int?>(null)
    val procesandoId: StateFlow<Int?> = _procesandoId.asStateFlow()

    fun seleccionarFecha(fecha: LocalDate) { _fechaSeleccionada.value = fecha }
    fun limpiarMensaje() { _mensaje.value = null }

    fun confirmar(cita: CitaConDetalle) = cambiarEstado(
        cita.cita.id, setOf(EstadoCita.PENDIENTE), EstadoCita.CONFIRMADA
    )

    fun cancelar(cita: CitaConDetalle) = cambiarEstado(
        cita.cita.id,
        setOf(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA),
        EstadoCita.CANCELADA
    )

    private fun cambiarEstado(
        id: Int,
        permitidos: Set<EstadoCita>,
        destino: EstadoCita
    ) {
        if (_procesandoId.value != null) return
        viewModelScope.launch {
            _procesandoId.value = id
            try {
                val actual = repo.observarCita(id).first()?.cita
                    ?: throw IllegalStateException("La cita ya no existe")
                val estado = EstadoCita.valueOf(actual.estado)
                require(estado in permitidos) { "Esta cita ya no admite ese cambio" }
                repo.cambiarEstadoCita(id, destino)
                _mensaje.value = if (destino == EstadoCita.CONFIRMADA)
                    "Cita confirmada" else "Cita cancelada"
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "No se pudo actualizar la cita"
            } finally {
                _procesandoId.value = null
            }
        }
    }
}
