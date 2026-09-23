package com.maldonado.ejercicioa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CitasScreen(
    citas: List<Cita>,
    esHistorial: Boolean = false,
    alCambiarEstado: (Int, String) -> Unit = { _, _ -> }
) {
    // Guardamos el identificador de la reserva pendiente de confirmación.
    var citaPendienteId by remember { mutableStateOf<Int?>(null) }

    val citaPendiente = citas.firstOrNull {
        it.id == citaPendienteId && it.estado == "Confirmada"
    }

    if (citas.isEmpty()) {
        MensajeSeccion(
            titulo = if (esHistorial) "Sin atenciones anteriores" else "Sin citas",
            descripcion = if (esHistorial) {
                "Tus atenciones completadas aparecerán aquí."
            } else {
                "Agenda una cita desde Inicio."
            }
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(citas, key = { it.id }) { cita ->
                TarjetaReserva(
                    cita = cita,
                    mostrarAcciones = !esHistorial,
                    alCancelar = {
                        citaPendienteId = cita.id
                    }
                )
            }
        }
    }

    // La reserva cambia únicamente cuando se acepta la cancelación.
    if (!esHistorial && citaPendiente != null) {
        AlertDialog(
            onDismissRequest = {
                citaPendienteId = null
            },
            title = {
                Text("¿Cancelar esta cita?", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        citaPendiente.medico.nombre,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("${citaPendiente.fecha}, ${citaPendiente.hora}")
                    Spacer(Modifier.height(12.dp))
                    Text("La cita quedará registrada como cancelada.")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        alCambiarEstado(citaPendiente.id, "Cancelada")
                        citaPendienteId = null
                    }
                ) {
                    Text("Sí, cancelar", color = Color(0xFFB3261E))
                }
            },
            dismissButton = {
                TextButton(onClick = { citaPendienteId = null }) {
                    Text("Mantener cita")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun TarjetaReserva(
    cita: Cita,
    mostrarAcciones: Boolean,
    alCancelar: () -> Unit
) package com.maldonado.ejercicioa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(
    citas: List<Cita>,
    esHistorial: Boolean = false,
    alCambiarEstado: (Int, String) -> Unit = { _, _ -> }
) {
    // La selección del filtro no modifica la lista original.
    var filtroSeleccionado by remember { mutableStateOf("Todas") }
    var citaPendienteId by remember { mutableStateOf<Int?>(null) }

    val filtros = listOf(
        "Todas" to "Todas",
        "Confirmadas" to "Confirmada",
        "Completadas" to "Completada",
        "Canceladas" to "Cancelada"
    )

    val citasVisibles = when {
        esHistorial -> citas.filter { it.estado == "Completada" }
        filtroSeleccionado == "Todas" -> citas
        else -> citas.filter { it.estado == filtroSeleccionado }
    }

    // El diálogo consulta la reserva actual mediante su identificador.
    val citaPendiente = citas.firstOrNull {
        it.id == citaPendienteId && it.estado == "Confirmada"
    }

    Column(Modifier.fillMaxSize()) {
        if (!esHistorial) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtros, key = { it.second }) { (nombre, estado) ->
                    // Cada cantidad se calcula desde las reservas actuales.
                    val cantidad = if (estado == "Todas") {
                        citas.size
                    } else {
                        citas.count { it.estado == estado }
                    }

                    FilterChip(
                        selected = filtroSeleccionado == estado,
                        onClick = {
                            filtroSeleccionado = estado
                        },
                        label = {
                            Text("$nombre ($cantidad)")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoradoClinica,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Reservamos para la lista el espacio que queda debajo de los filtros.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (citasVisibles.isEmpty()) {
                MensajeSeccion(
                    titulo = if (esHistorial) {
                        "Sin atenciones anteriores"
                    } else {
                        "No hay citas"
                    },
                    descripcion = when {
                        esHistorial ->
                            "Tus atenciones completadas aparecerán aquí."

                        citas.isEmpty() ->
                            "Agenda una cita desde Inicio."

                        else ->
                            "No hay reservas en esta categoría. Selecciona otro filtro."
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 24.dp,
                        top = 12.dp,
                        end = 24.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(citasVisibles, key = { it.id }) { cita ->
                        TarjetaReserva(
                            cita = cita,
                            mostrarAcciones = !esHistorial,
                            alCancelar = {
                                citaPendienteId = cita.id
                            },
                            alRecuperar = {
                                // Recuperamos el mismo registro, sin crear otra cita.
                                alCambiarEstado(cita.id, "Confirmada")
                            }
                        )
                    }
                }
            }
        }
    }

    // Cerrar el diálogo o mantener la cita conserva el estado anterior.
    if (!esHistorial && citaPendiente != null) {
        AlertDialog(
            onDismissRequest = {
                citaPendienteId = null
            },
            title = {
                Text(
                    text = "¿Cancelar esta cita?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = citaPendiente.medico.nombre,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("${citaPendiente.fecha}, ${citaPendiente.hora}")

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "La cita quedará registrada como cancelada. " +
                                "Podrás recuperarla desde Mis citas."
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        alCambiarEstado(citaPendiente.id, "Cancelada")
                        citaPendienteId = null
                    }
                ) {
                    Text(
                        text = "Sí, cancelar",
                        color = Color(0xFFB3261E)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        citaPendienteId = null
                    }
                ) {
                    Text("Mantener cita")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun TarjetaReserva(
    cita: Cita,
    mostrarAcciones: Boolean,
    alCancelar: () -> Unit,
    alRecuperar: () -> Unit
) {
    val colorEstado = when (cita.estado) {
        "Confirmada" -> VerdeClinica
        "Cancelada" -> Color(0xFFB3261E)
        else -> Color(0xFF777777)
    }

    val fondoEstado = when (cita.estado) {
        "Confirmada" -> Color(0xFFE0F5ED)
        "Cancelada" -> Color(0xFFFCE8E6)
        else -> Color(0xFFE7E7E7)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF3F0F7)
    ) {
        Box {
            if (cita.estado == "Confirmada") {
                Box(Modifier.matchParentSize()) {
                    Box(
                        Modifier
                            .width(5.dp)
                            .fillMaxHeight()
                            .background(MoradoClinica)
                    )
                }
            }

            Column(Modifier.padding(18.dp)) {
                Text(
                    text = cita.medico.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF292929)
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    fontSize = 13.sp,
                    color = Color(0xFF817C86)
                )

                Spacer(Modifier.height(10.dp))

                Surface(
                    color = fondoEstado,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = cita.estado,
                        color = colorEstado,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 5.dp
                        )
                    )
                }

                // Cada estado ofrece solamente la acción que le corresponde.
                if (mostrarAcciones) {
                    when (cita.estado) {
                        "Confirmada" -> {
                            TextButton(onClick = alCancelar) {
                                Text(
                                    text = "Cancelar cita",
                                    color = Color(0xFFB3261E)
                                )
                            }
                        }

                        "Cancelada" -> {
                            TextButton(onClick = alRecuperar) {
                                Text(
                                    text = "Deshacer cancelación",
                                    color = MoradoClinica
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}