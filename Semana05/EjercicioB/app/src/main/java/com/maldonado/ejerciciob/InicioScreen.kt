package com.maldonado.ejerciciob

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InicioScreen(
    alElegirClase: (ClaseGimnasio) -> Unit
) {
    // Conservamos una sola categoría seleccionada.
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    val filtros = listOf("Hoy", "Esta semana")

    // Hoy muestra únicamente clases con algún horario para ese día.
    val clasesVisibles = if (filtroSeleccionado == "Hoy") {
        clasesEjemplo.filter { clase ->
            clase.horarios.any { it.dia == "Hoy" }
        }
    } else {
        clasesEjemplo
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Las categorías se pueden desplazar horizontalmente.
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            contentPadding = PaddingValues(
                horizontal = 18.dp,
                vertical = 14.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtros, key = { it }) { filtro ->
                val seleccionado = filtroSeleccionado == filtro

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (seleccionado) VerdeFit
                            else FondoTarjetaFit
                        )
                        .selectable(
                            selected = seleccionado,
                            role = Role.RadioButton,
                            onClick = {
                                filtroSeleccionado = filtro
                            }
                        )
                        .padding(
                            horizontal = 15.dp,
                            vertical = 10.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filtro,
                        fontSize = 12.sp,
                        color = if (seleccionado) Color.White
                        else TextoFit
                    )
                }
            }
        }

        Text(
            text = "Clases disponibles",
            modifier = Modifier.padding(horizontal = 18.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextoFit
        )

        Spacer(Modifier.height(12.dp))

        // Cada tarjeta conserva su identidad mediante el identificador de la clase.
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = clasesVisibles,
                key = { it.id }
            ) { clase ->
                // Elegimos un horario representativo para mostrar en la tarjeta.
                val horario = if (filtroSeleccionado == "Hoy") {
                    clase.horarios.firstOrNull { it.dia == "Hoy" }
                } else {
                    clase.horarios.firstOrNull()
                }

                if (horario != null) {
                    TarjetaClase(
                        clase = clase,
                        horario = horario,
                        mostrarDia = filtroSeleccionado == "Esta semana",
                        alPulsar = {
                            alElegirClase(clase)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaClase(
    clase: ClaseGimnasio,
    horario: HorarioClase,
    mostrarDia: Boolean,
    alPulsar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(FondoTarjetaFit)
            .clickable(
                role = Role.Button,
                onClickLabel = "Ver detalle de ${clase.nombre}",
                onClick = alPulsar
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(VerdeClaroFit),
            contentAlignment = Alignment.Center
        ) {
            IconoPesa(
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = clase.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoFit
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (mostrarDia) {
                    "${horario.dia}, ${horario.hora} · ${horario.sala}"
                } else {
                    "${horario.hora} · ${horario.sala}"
                },
                fontSize = 12.sp,
                color = TextoSecundarioFit
            )
        }
    }
}

// Dibujamos la pesa con proporciones para reutilizarla en distintos tamaños.
@Composable
fun IconoPesa(
    modifier: Modifier = Modifier,
    color: Color = VerdeFit
) {
    Canvas(modifier = modifier) {
        // Barra que une ambos extremos.
        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = size.width * 0.20f,
                y = size.height * 0.43f
            ),
            size = Size(
                width = size.width * 0.60f,
                height = size.height * 0.14f
            ),
            cornerRadius = CornerRadius(size.minDimension * 0.025f)
        )

        // Discos principales de la pesa.
        listOf(0.16f, 0.70f).forEach { posicion ->
            drawRoundRect(
                color = color,
                topLeft = Offset(
                    x = size.width * posicion,
                    y = size.height * 0.24f
                ),
                size = Size(
                    width = size.width * 0.14f,
                    height = size.height * 0.52f
                ),
                cornerRadius = CornerRadius(size.minDimension * 0.035f)
            )
        }

        // Extremos pequeños a los lados.
        listOf(0.07f, 0.86f).forEach { posicion ->
            drawRoundRect(
                color = color,
                topLeft = Offset(
                    x = size.width * posicion,
                    y = size.height * 0.34f
                ),
                size = Size(
                    width = size.width * 0.07f,
                    height = size.height * 0.32f
                ),
                cornerRadius = CornerRadius(size.minDimension * 0.02f)
            )
        }
    }
}