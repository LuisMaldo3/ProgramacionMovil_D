package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.theme.*

@Composable
fun EstadoCitaChip(estado: EstadoCita) {
    val color = when (estado) {
        EstadoCita.PENDIENTE -> AmbarPendiente
        EstadoCita.CONFIRMADA -> VerdeConfirmada
        EstadoCita.ATENDIDA -> GrisAtendida
        EstadoCita.CANCELADA -> RojoCancelada
    }
    Text(
        text = estado.name.lowercase().replaceFirstChar { it.uppercase() },
        color = color,
        modifier = Modifier.background(color.copy(alpha = 0.12f), RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
