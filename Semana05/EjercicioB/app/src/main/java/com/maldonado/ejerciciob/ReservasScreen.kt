package com.maldonado.ejerciciob

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
fun ReservasScreen(
    reservas: List<Reserva>,
    alCancelar: (Int) -> Unit
) {
    // Guardamos únicamente el ID de la reserva que se quiere cancelar.
    var reservaPendienteId by remember { mutableStateOf<Int?>(null) }

    // Consultamos los datos actuales antes de mostrar la confirmación.
    val reservaPendiente = reservas.firstOrNull {
        it.id == reservaPendienteId && it.estado == "Confirmada"
    }

    if (reservas.isEmpty()) {
        MensajeFit(
            titulo = "Sin reservas",
            descripcion = "No hay reservas para mostrar."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 12.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mostramos primero las reservas más recientes.
            items(
                items = reservas.sortedByDescending { it.id },
                key = { it.id }
            ) { reserva ->
                val confirmada = reserva.estado == "Confirmada"
                val cancelada = reserva.estado == "Cancelada"

                val colorEstado = when {
                    confirmada -> VerdeFit
                    cancelada -> Color(0xFFB3261E)
                    else -> Color(0xFF777777)
                }

                val fondoEstado = when {
                    confirmada -> VerdeClaroFit
                    cancelada -> Color(0xFFFCE8E6)
                    else -> Color(0xFFE7E7E7)
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = FondoTarjetaFit
                ) {
                    Box {
                        // La franja verde identifica las reservas activas.
                        if (confirmada) {
                            Box(Modifier.matchParentSize()) {
                                Box(
                                    modifier = Modifier
                                        .width(5.dp)
                                        .fillMaxHeight()
                                        .background(VerdeFit)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 14.dp
                            )
                        ) {
                            Text(
                                text = reserva.clase.nombre,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoFit
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "${reserva.horario.dia}, " +
                                        reserva.horario.hora,
                                fontSize = 13.sp,
                                color = TextoSecundarioFit
                            )

                            Spacer(Modifier.height(7.dp))

                            Surface(
                                color = fondoEstado,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = reserva.estado,
                                    fontSize = 12.sp,
                                    color = colorEstado,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    )
                                )
                            }

                            // Solo permitimos cancelar reservas confirmadas.
                            if (confirmada) {
                                TextButton(
                                    onClick = {
                                        reservaPendienteId = reserva.id
                                    }
                                ) {
                                    Text(
                                        text = "Cancelar reserva",
                                        color = Color(0xFFB3261E)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Cerrar el diálogo o pulsar Mantener reserva no modifica el registro.
    if (reservaPendiente != null) {
        AlertDialog(
            onDismissRequest = {
                reservaPendienteId = null
            },
            title = {
                Text(
                    text = "¿Cancelar esta reserva?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = reservaPendiente.clase.nombre,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "${reservaPendiente.horario.dia}, " +
                                "${reservaPendiente.horario.hora} · " +
                                reservaPendiente.horario.sala
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Se liberará tu cupo y la reserva " +
                                "quedará registrada como cancelada."
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // El estado compartido se actualiza desde FitApp.
                        alCancelar(reservaPendiente.id)
                        reservaPendienteId = null
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
                        reservaPendienteId = null
                    }
                ) {
                    Text(
                        text = "Mantener reserva",
                        color = VerdeFit
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}