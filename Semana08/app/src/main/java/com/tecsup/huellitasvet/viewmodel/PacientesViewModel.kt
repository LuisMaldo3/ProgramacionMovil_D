
package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

data class PacienteResumen(
    val datos: MascotaConDueno,
    val ultimaVisita: String?
)

@OptIn(ExperimentalCoroutinesApi::class)
class PacientesViewModel(
    private val repo: VetRepository
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()

    private val _especie = MutableStateFlow("TODOS")
    val especie = _especie.asStateFlow()

    val todos: StateFlow<List<PacienteResumen>> =
        repo.observarTodasLasMascotas()
            .flatMapLatest { mascotas ->

                if (mascotas.isEmpty()) {
                    flowOf(emptyList<PacienteResumen>())
                } else {
                    combine(
                        mascotas.map { datos ->
                            repo.observarHistorial(
                                datos.mascota.id
                            ).map { historial ->

                                PacienteResumen(
                                    datos = datos,
                                    ultimaVisita =
                                        historial.firstOrNull()
                                            ?.cita?.fecha
                                )
                            }
                        }
                    ) { array ->
                        array.toList()
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val pacientes: StateFlow<List<PacienteResumen>> =
        combine(
            todos,
            _busqueda,
            _especie
        ) { lista, texto, filtro ->

            lista.filter { paciente ->

                val coincideNombre =
                    texto.isBlank() ||
                            paciente.datos.mascota.nombre.contains(
                                texto.trim(),
                                ignoreCase = true
                            ) ||
                            paciente.datos.dueno.nombre.contains(
                                texto.trim(),
                                ignoreCase = true
                            )

                val coincideEspecie =
                    filtro == "TODOS" ||
                            paciente.datos.mascota.especie == filtro

                coincideNombre && coincideEspecie

            }.sortedBy {
                it.datos.mascota.nombre.lowercase()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun filtrarPorEspecie(valor: String) {
        if (valor in setOf(
                "TODOS", "PERRO", "GATO", "OTRO"
            )
        ) {
            _especie.value = valor
        }
    }

    fun historial(
        mascotaId: Int
    ): Flow<List<CitaConDetalle>> {
        return repo.observarHistorial(mascotaId)
    }
}
