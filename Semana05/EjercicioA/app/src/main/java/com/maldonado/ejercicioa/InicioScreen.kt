package com.maldonado.ejercicioa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InicioScreen(
    alElegirMedico: (Medico) -> Unit
) {
    // Al entrar mostramos los tres médicos.
    // Pulsar una especialidad filtra; pulsarla otra vez recupera la lista.
    var especialidadSeleccionada by remember {
        mutableStateOf<String?>(null)
    }

    val especialidades = listOf("Cardiología", "Pediatría")

    val medicosVisibles = medicosEjemplo.filter {
        especialidadSeleccionada == null ||
                it.especialidad == especialidadSeleccionada
    }

    // El contenido empieza cerca del encabezado, como en la referencia.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(14.dp))

        // Construimos filtros pequeños y redondeados.
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(especialidades) { especialidad ->
                FiltroEspecialidad(
                    texto = especialidad,
                    seleccionado = especialidadSeleccionada == especialidad,
                    alPulsar = {
                        especialidadSeleccionada =
                            if (especialidadSeleccionada == especialidad) {
                                null
                            } else {
                                especialidad
                            }
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Médicos disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF292929)
        )

        Spacer(Modifier.height(10.dp))

        // Mostramos tarjetas compactas con separación uniforme.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = medicosVisibles,
                key = { it.id }
            ) { medico ->
                TarjetaMedico(
                    medico = medico,
                    alPulsar = {
                        alElegirMedico(medico)
                    }
                )
            }
        }
    }
}

// Dibujamos el filtro sin los bordes y espacios adicionales del chip estándar.
@Composable
private fun FiltroEspecialidad(
    texto: String,
    seleccionado: Boolean,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (seleccionado) MoradoClinica
                else Color(0xFFF3F0F7)
            )
            .clickable(onClick = alPulsar)
            .padding(horizontal = 15.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (seleccionado) Color.White
            else Color(0xFF55515B)
        )
    }
}

// La tarjeta contiene la cruz, los datos del médico y su calificación.
@Composable
private fun TarjetaMedico(
    medico: Medico,
    alPulsar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF3F0F7))
            .clickable(onClick = alPulsar)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimboloMedico()

        Spacer(Modifier.width(10.dp))

        // Los datos ocupan el espacio restante sin desplazar la calificación.
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = medico.nombre,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF292929),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = medico.especialidad,
                fontSize = 11.sp,
                color = Color(0xFF817C86)
            )
        }

        Spacer(Modifier.width(6.dp))

        // Solo la estrella es dorada; el número permanece gris.
        Text(
            text = "★",
            fontSize = 16.sp,
            color = Color(0xFFD4A000)
        )

        Spacer(Modifier.width(3.dp))

        Text(
            text = medico.calificacion,
            fontSize = 11.sp,
            color = Color(0xFF817C86)
        )
    }
}

// Dibujamos la cruz para que su forma no dependa de la fuente del teléfono.
// El tamaño se podrá ampliar cuando construyamos el perfil del médico.
@Composable
fun SimboloMedico(
    tamano: Dp = 44.dp
) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(Color(0xFFEEE5F7), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(0.52f)
        ) {
            drawLine(
                color = MoradoClinica,
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height),
                strokeWidth = size.width * 0.17f
            )

            drawLine(
                color = MoradoClinica,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = size.width * 0.17f
            )
        }
    }
}