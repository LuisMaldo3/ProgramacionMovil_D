package com.maldonado.ejercicioa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Recibimos el médico seleccionado y la acción del botón Agendar cita.
@Composable
fun MedicoScreen(
    medico: Medico,
    alAgendar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {

        // La información ocupa el espacio superior y puede desplazarse.
        // El botón permanece separado en la parte inferior.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(18.dp))

            // Ampliamos la misma cruz que utilizamos en las tarjetas.
            SimboloMedico(tamano = 90.dp)

            Spacer(Modifier.height(12.dp))

            Text(
                text = medico.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF292929),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "${medico.especialidad} · ${medico.experiencia}",
                fontSize = 12.sp,
                color = Color(0xFF817C86),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(6.dp))

            // La estrella se muestra dorada y la calificación en gris.
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★",
                    fontSize = 17.sp,
                    color = Color(0xFFD4A000)
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    text = medico.calificacion,
                    fontSize = 12.sp,
                    color = Color(0xFF817C86)
                )

                // La referencia muestra este dato para Ana Torres.
                if (medico.id == 1) {
                    Text(
                        text = " (128 reseñas)",
                        fontSize = 12.sp,
                        color = Color(0xFF817C86)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // La descripción queda alineada a la izquierda, como en el modelo.
            Text(
                text = medico.descripcion,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF55515B)
            )

            Spacer(Modifier.height(20.dp))
        }

        // El botón ocupa el ancho disponible y conserva las esquinas del modelo.
        Button(
            onClick = alAgendar,
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
                text = "Agendar cita",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}