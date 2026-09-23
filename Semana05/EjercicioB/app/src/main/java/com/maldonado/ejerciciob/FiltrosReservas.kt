package com.maldonado.ejerciciob

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FiltrosReservasScreen(
    reservas: List<Reserva>,
    alCancelar: (Int) -> Unit
) {
    // El filtro afecta únicamente lo que mostramos, no la lista original.
    var filtroSeleccionado by remember {
        mutableStateOf("Todas")
    }

    val filtros = listOf(
        "Todas",
        "Confirmada",
        "Completada",
        "Cancelada"
    )

    val reservasVisibles = if (filtroSeleccionado == "Todas") {
        reservas
    } else {
        reservas.filter {
            it.estado == filtroSeleccionado
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // La fila se puede desplazar cuando las opciones no caben.
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = filtros,
                key = { it }
            ) { filtro ->
                FilterChip(
                    selected = filtroSeleccionado == filtro,
                    onClick = {
                        // Solo puede quedar un filtro seleccionado.
                        filtroSeleccionado = filtro
                    },
                    label = {
                        Text(
                            text = when (filtro) {
                                "Confirmada" -> "Confirmadas"
                                "Completada" -> "Completadas"
                                "Cancelada" -> "Canceladas"
                                else -> "Todas"
                            }
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = FondoTarjetaFit,
                        labelColor = TextoFit,
                        selectedContainerColor = VerdeFit,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Reutilizamos el listado y su diálogo de cancelación.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ReservasScreen(
                reservas = reservasVisibles,
                alCancelar = alCancelar
            )
        }
    }
}