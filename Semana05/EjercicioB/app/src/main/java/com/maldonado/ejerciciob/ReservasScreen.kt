package com.maldonado.ejerciciob

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReservasScreen(
    reservas: List<Reserva>
) {
    // Mostramos una indicación cuando todavía no existen reservas.
    if (reservas.isEmpty()) {
        MensajeFit(
            titulo = "Sin reservas",
            descripcion = "Selecciona una clase desde Inicio para reservar."
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Las reservas recientes aparecen primero.
            // El identificador permite reconocer cada tarjeta.
            items(
                items = reservas.sortedByDescending { it.id },
                key = { it.id }
            ) { reserva ->
                val confirmada = reserva.estado == "Confirmada"

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = FondoTarjetaFit
                ) {
                    Box {
                        // La franja verde identifica las reservas confirmadas.
                        if (confirmada) {
                            Box(
                                modifier = Modifier.matchParentSize()
                            ) {
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
                                text = "${reserva.horario.dia}, ${reserva.horario.hora}",
                                fontSize = 13.sp,
                                color = TextoSecundarioFit
                            )

                            Spacer(Modifier.height(7.dp))

                            // Diferenciamos los estados mediante el texto y el color.
                            Surface(
                                color = if (confirmada) {
                                    VerdeClaroFit
                                } else {
                                    Color(0xFFE4E4E4)
                                },
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = reserva.estado,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 3.dp
                                    ),
                                    fontSize = 12.sp,
                                    color = if (confirmada) {
                                        VerdeFit
                                    } else {
                                        Color(0xFF777777)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}