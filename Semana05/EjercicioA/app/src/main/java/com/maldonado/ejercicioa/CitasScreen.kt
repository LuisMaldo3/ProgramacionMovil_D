package com.maldonado.ejercicioa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CitasScreen(
    citas: List<Cita>,
    esHistorial: Boolean = false
) {

    // Mostramos una explicación cuando la sección no tiene registros.
    if (citas.isEmpty()) {
        MensajeSeccion(
            titulo = if (esHistorial) {
                "Sin atenciones anteriores"
            } else {
                "Todavía no tienes citas"
            },
            descripcion = if (esHistorial) {
                "Tus atenciones completadas aparecerán aquí."
            } else {
                "Selecciona un médico en Inicio para agendar."
            }
        )
        return
    }

    // La lista permite consultar todas las citas mediante desplazamiento.
    // Las últimas registradas aparecen primero.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 24.dp,
            end = 24.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(
            items = citas.reversed(),
            key = { it.id }
        ) { cita ->
            TarjetaCita(cita = cita)
        }
    }
}

// Cada tarjeta presenta al médico, el horario y el estado de la reserva.
@Composable
private fun TarjetaCita(cita: Cita) {

    // Elegimos los colores según el estado, sin modificar los datos de la cita.
    val confirmada = cita.estado == "Confirmada"

    val fondoEstado = if (confirmada) {
        Color(0xFFE0F5ED)
    } else {
        Color(0xFFE7E7E7)
    }

    val colorEstado = if (confirmada) {
        Color(0xFF16856A)
    } else {
        Color(0xFF777777)
    }

    // Box permite colocar la franja sobre el borde izquierdo de la tarjeta.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF3F0F7))
    ) {

        // Solo las citas confirmadas tienen la franja morada.
        if (confirmada) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(end = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                        .background(MoradoClinica)
                )
            }
        }

        Column(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 16.dp
            )
        ) {
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

            // El texto permite reconocer el estado además de su color.
            Surface(
                color = fondoEstado,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = cita.estado,
                    fontSize = 12.sp,
                    color = colorEstado,
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 5.dp
                    )
                )
            }
        }
    }
}