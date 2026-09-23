package com.maldonado.ejercicioa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Recibimos la cita registrada y las acciones de los dos botones.
@Composable
fun ConfirmacionScreen(
    cita: Cita,
    alVerCitas: () -> Unit,
    alInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Dibujamos la marca de confirmación dentro de un círculo verde claro.
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(
                    color = Color(0xFFE0F5ED),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(38.dp)
            ) {
                drawLine(
                    color = Color(0xFF16A582),
                    start = Offset(size.width * 0.16f, size.height * 0.52f),
                    end = Offset(size.width * 0.40f, size.height * 0.76f),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = Color(0xFF16A582),
                    start = Offset(size.width * 0.40f, size.height * 0.76f),
                    end = Offset(size.width * 0.85f, size.height * 0.20f),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "¡Cita agendada!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF292929)
        )

        Spacer(Modifier.height(8.dp))

        // Mostramos los datos de la cita que realmente se acaba de registrar.
        Text(
            text = cita.medico.nombre,
            fontSize = 13.sp,
            color = Color(0xFF817C86),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "${cita.fecha}, ${cita.hora}",
            fontSize = 13.sp,
            color = Color(0xFF817C86),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        // Este botón abre el listado sin registrar nuevamente la cita.
        Button(
            onClick = alVerCitas,
            modifier = Modifier
                .width(180.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3F0F7),
                contentColor = Color(0xFF55515B)
            )
        ) {
            Text(
                text = "Ver mis citas",
                fontSize = 13.sp
            )
        }

        // La actividad también pide un botón para regresar al inicio.
        TextButton(
            onClick = alInicio
        ) {
            Text(
                text = "Volver al inicio",
                fontSize = 12.sp,
                color = MoradoClinica
            )
        }
    }
}