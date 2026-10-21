
package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AtenderCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> =
        repo.observarCita(citaId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    private val _errorDiagnostico =
        MutableStateFlow<String?>(null)
    val errorDiagnostico = _errorDiagnostico.asStateFlow()

    private val _errorPeso =
        MutableStateFlow<String?>(null)
    val errorPeso = _errorPeso.asStateFlow()

    private val _mensaje =
        MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando = _procesando.asStateFlow()

    private val _finalizada = MutableStateFlow(false)
    val finalizada = _finalizada.asStateFlow()

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun registrar(
        diagnostico: String,
        tratamiento: String,
        pesoTexto: String
    ) {
        if (_procesando.value) return

        val diagnosticoLimpio = diagnostico.trim()
        val peso = pesoTexto.trim()
            .replace(',', '.')
            .toDoubleOrNull()

        _errorDiagnostico.value =
            if (diagnosticoLimpio.length < 5) {
                "El diagnóstico debe tener al menos 5 caracteres"
            } else null

        _errorPeso.value =
            if (peso == null || !peso.isFinite() || peso <= 0.0) {
                "Ingresa un peso válido mayor que 0"
            } else null

        if (_errorDiagnostico.value != null ||
            _errorPeso.value != null
        ) return

        viewModelScope.launch {
            _procesando.value = true

            try {
                val actual = repo.observarCita(citaId)
                    .first()?.cita
                    ?: throw IllegalStateException(
                        "La cita no existe"
                    )

                require(
                    actual.estado == EstadoCita.PENDIENTE.name ||
                            actual.estado == EstadoCita.CONFIRMADA.name
                ) {
                    "Esta cita ya no se puede atender"
                }

                repo.registrarAtencion(
                    citaId = citaId,
                    diagnostico = diagnosticoLimpio,
                    tratamiento = tratamiento.trim(),
                    peso = requireNotNull(peso)
                )

                _finalizada.value = true

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = e.message
                    ?: "No se pudo registrar la atención"
            } finally {
                _procesando.value = false
            }
        }
    }

    fun cancelar() {
        if (_procesando.value) return

        viewModelScope.launch {
            _procesando.value = true

            try {
                val actual = repo.observarCita(citaId)
                    .first()?.cita
                    ?: throw IllegalStateException(
                        "La cita no existe"
                    )

                require(
                    actual.estado == EstadoCita.PENDIENTE.name ||
                            actual.estado == EstadoCita.CONFIRMADA.name
                ) {
                    "Esta cita ya no se puede cancelar"
                }

                repo.cambiarEstadoCita(
                    citaId,
                    EstadoCita.CANCELADA
                )

                _finalizada.value = true

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = e.message
                    ?: "No se pudo cancelar la cita"
            } finally {
                _procesando.value = false
            }
        }
    }
}
