package com.maldonado.ejercicioa

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AgendarScreen(
    alConfirmar: (String, String) -> Unit
) {
    // Comenzamos con las opciones resaltadas en la imagen de referencia.
    // Cada selección se guarda por separado y solo admite una opción.
    var fechaSeleccionada by remember { mutableStateOf("Viernes 27") }
    var horaSeleccionada by remember { mutableStateOf("10:30 am") }

    // Son fechas de demostración tomadas del diseño, sin mes ni año.
    val fechas = listOf(
        Triple("Jue", "26", "Jueves 26"),
        Triple("Vie", "27", "Viernes 27"),
        Triple("Sáb", "28", "Sábado 28")
    )

    val horas = listOf(
        "9:00" to "9:00 am",
        "10:30" to "10:30 am",
        "3:00" to "3:00 pm"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // Dejamos los controles arriba y reservamos la parte inferior al botón.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(18.dp))

            Text(
                text = "Selecciona fecha",
                fontSize = 13.sp,
                color = Color(0xFF817C86)
            )

            Spacer(Modifier.height(10.dp))

            // El grupo indica que las fechas funcionan como opciones únicas.
            Row(
                modifier = Modifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                fechas.forEach { (dia, numero, fecha) ->
                    Column(
                        modifier = Modifier
                            .width(78.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (fechaSeleccionada == fecha) MoradoClinica
                                else Color(0xFFF3F0F7)
                            )
                            .selectable(
                                selected = fechaSeleccionada == fecha,
                                role = Role.RadioButton,
                                onClick = {
                                    fechaSeleccionada = fecha
                                }
                            )
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia,
                            fontSize = 11.sp,
                            color = if (fechaSeleccionada == fecha) Color.White
                            else Color(0xFF55515B)
                        )

                        Text(
                            text = numero,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fechaSeleccionada == fecha) Color.White
                            else Color(0xFF292929)
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = "Selecciona hora",
                fontSize = 13.sp,
                color = Color(0xFF817C86)
            )

            Spacer(Modifier.height(10.dp))

            // Elegir otro horario reemplaza el anterior.
            Row(
                modifier = Modifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                horas.forEach { (etiqueta, hora) ->
                    Box(
                        modifier = Modifier
                            .width(78.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (horaSeleccionada == hora) MoradoClinica
                                else Color(0xFFF3F0F7)
                            )
                            .selectable(
                                selected = horaSeleccionada == hora,
                                role = Role.RadioButton,
                                onClick = {
                                    horaSeleccionada = hora
                                }
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = etiqueta,
                            fontSize = 13.sp,
                            color = if (horaSeleccionada == hora) Color.White
                            else Color(0xFF55515B)
                        )
                    }
                }
            }
        }

        // Enviamos la fecha y la hora elegidas para registrar la cita.
        Button(
            onClick = {
                alConfirmar(fechaSeleccionada, horaSeleccionada)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoClinica,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Confirmar cita",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}