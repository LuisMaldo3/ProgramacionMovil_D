package com.maldonado.ejerciciob

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalleClaseScreen(
    clase: ClaseGimnasio,
    alReservar: (HorarioClase) -> Unit
) {
    // El selector permanece cerrado hasta pulsar Reservar cupo.
    var mostrarHorarios by remember(clase.id) {
        mutableStateOf(false)
    }

    // Comenzamos con el primer horario que tenga disponibilidad.
    var horarioSeleccionadoId by remember(clase.id) {
        mutableStateOf(
            clase.horarios.firstOrNull {
                it.cuposDisponibles > 0
            }?.id
        )
    }

    val horarioSeleccionado = clase.horarios.firstOrNull {
        it.id == horarioSeleccionadoId
    }

    // Si no quedan cupos, igualmente mostramos la información de la clase.
    val horarioResumen = horarioSeleccionado
        ?: clase.horarios.firstOrNull()

    val hayCupos = clase.horarios.any {
        it.cuposDisponibles > 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // El contenido ocupa el espacio superior y puede desplazarse.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
                color = VerdeClaroFit,
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    PesaDetalle(
                        modifier = Modifier
                            .width(105.dp)
                            .height(50.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = clase.nombre,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = TextoFit
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = horarioResumen?.let {
                    "${it.hora} · ${it.sala} · ${clase.duracionMinutos} min"
                } ?: "${clase.duracionMinutos} min",
                fontSize = 13.sp,
                color = TextoSecundarioFit
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = clase.descripcion,
                fontSize = 14.sp,
                color = TextoFit
            )

            Spacer(Modifier.height(22.dp))

            // La cantidad corresponde al horario mostrado.
            Text(
                text = horarioResumen?.let {
                    "${it.cuposDisponibles} de ${it.capacidad} cupos disponibles"
                } ?: "No hay horarios disponibles",
                fontSize = 13.sp,
                color = TextoFit
            )

            Spacer(Modifier.height(20.dp))
        }

        // El botón permanece abajo aunque la descripción sea corta.
        Button(
            onClick = {
                mostrarHorarios = true
            },
            enabled = hayCupos,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeFit,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Reservar cupo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))
    }

    // Elegimos un único horario antes de enviar la reserva.
    if (mostrarHorarios) {
        AlertDialog(
            onDismissRequest = {
                mostrarHorarios = false
            },
            title = {
                Text(
                    text = "Selecciona un horario",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .selectableGroup()
                ) {
                    clase.horarios.forEach { horario ->
                        val disponible = horario.cuposDisponibles > 0
                        val seleccionado = horario.id == horarioSeleccionadoId

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = seleccionado,
                                    enabled = disponible,
                                    role = Role.RadioButton,
                                    onClick = {
                                        horarioSeleccionadoId = horario.id
                                    }
                                )
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Toda la fila se puede pulsar, no solo el círculo.
                            RadioButton(
                                selected = seleccionado,
                                onClick = null,
                                enabled = disponible,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = VerdeFit
                                )
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${horario.dia}, ${horario.hora}",
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextoFit
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = if (disponible) {
                                        "${horario.sala} · ${horario.cuposDisponibles} cupos"
                                    } else {
                                        "${horario.sala} · Agotado"
                                    },
                                    fontSize = 12.sp,
                                    color = TextoSecundarioFit
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = horarioSeleccionado?.let {
                        it.cuposDisponibles > 0
                    } == true,
                    onClick = {
                        horarioSeleccionado?.let { horario ->
                            if (horario.cuposDisponibles > 0) {
                                mostrarHorarios = false

                                // La pantalla principal recibirá el horario elegido.
                                alReservar(horario)
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Confirmar reserva",
                        color = VerdeFit
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarHorarios = false
                    }
                ) {
                    Text(
                        text = "Volver",
                        color = TextoSecundarioFit
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// Dibujamos la pesa con dos extremos rectangulares y una barra central.
@Composable
private fun PesaDetalle(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        drawRect(
            color = VerdeFit,
            topLeft = Offset(
                x = size.width * 0.12f,
                y = size.height * 0.38f
            ),
            size = Size(
                width = size.width * 0.76f,
                height = size.height * 0.24f
            )
        )

        listOf(0f, 0.82f).forEach { posicion ->
            drawRoundRect(
                color = VerdeFit,
                topLeft = Offset(
                    x = size.width * posicion,
                    y = size.height * 0.10f
                ),
                size = Size(
                    width = size.width * 0.18f,
                    height = size.height * 0.80f
                ),
                cornerRadius = CornerRadius(3.dp.toPx())
            )
        }
    }
}