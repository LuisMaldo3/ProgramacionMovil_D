package com.maldonado.ejerciciob

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

@Composable
fun ConfirmacionScreen(
    reserva: Reserva,
    alVerReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // La marca verde indica que la reserva se registró.
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(VerdeClaroFit, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.size(38.dp)) {
                drawLine(
                    color = Color(0xFF16A582),
                    start = Offset(
                        size.width * 0.16f,
                        size.height * 0.52f
                    ),
                    end = Offset(
                        size.width * 0.40f,
                        size.height * 0.76f
                    ),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = Color(0xFF16A582),
                    start = Offset(
                        size.width * 0.40f,
                        size.height * 0.76f
                    ),
                    end = Offset(
                        size.width * 0.85f,
                        size.height * 0.20f
                    ),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "¡Cupo reservado!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextoFit,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        // Mostramos los datos de la reserva recibida.
        Text(
            text = reserva.clase.nombre,
            fontSize = 14.sp,
            color = TextoSecundarioFit,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = "${reserva.horario.dia}, ${reserva.horario.hora}" +
                    " · ${reserva.horario.sala}",
            fontSize = 13.sp,
            color = TextoSecundarioFit,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(34.dp))

        Button(
            onClick = alVerReservas,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FondoTarjetaFit,
                contentColor = TextoFit
            )
        ) {
            Text(
                text = "Ver mis reservas",
                fontSize = 13.sp
            )
        }
    }
}