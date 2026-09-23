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
            // Conservamos la franja lateral de las reservas confirmadas.
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

                // Las citas completadas y el historial no ofrecen cancelación.
                if (mostrarAcciones && cita.estado == "Confirmada") {
                    TextButton(onClick = alCancelar) {
                        Text("Cancelar cita", color = Color(0xFFB3261E))
                    }
                }
            }
        }
    }
}