package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.EstadoCita
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Compatible con el componente de ejemplo del laboratorio. */
@Composable
fun CitaCard(titulo: String, detalle: String, estado: EstadoCita, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(detalle, style = MaterialTheme.typography.bodyMedium)
            EstadoCitaChip(estado)
        }
    }
}

/** Tarjeta premium sin superposición de acciones (el Box original cubría parte del texto). */
@Composable
fun CitaCard(
    cita: CitaConDetalle,
    mostrarCancelar: Boolean,
    cancelando: Boolean,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarReprogramar: Boolean = false,
    onReprogramar: () -> Unit = {}
) {
    val verde = MaterialTheme.colorScheme.primary
    val servicio = cita.servicio.nombre
    val icono: ImageVector = when {
        servicio.contains("vacun", true) -> Icons.Filled.Vaccines
        servicio.contains("bañ", true) || servicio.contains("corte", true) -> Icons.Filled.ContentCut
        else -> Icons.Filled.Favorite
    }
    val fecha = runCatching {
        LocalDate.parse(cita.cita.fecha).format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE")))
            .replaceFirstChar { it.uppercaseChar() }
    }.getOrDefault(cita.cita.fecha)
    val estado = runCatching { EstadoCita.valueOf(cita.cita.estado) }.getOrDefault(EstadoCita.PENDIENTE)

    ElevatedCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(21.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
                Surface(shape = RoundedCornerShape(15.dp), color = verde.copy(alpha=.11f)) {
                    Icon(icono, null, tint = verde, modifier = Modifier.padding(13.dp).size(25.dp))
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(servicio, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Mascota: ${cita.mascota.mascota.nombre}", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(Icons.Filled.Schedule, null, tint = verde, modifier = Modifier.size(17.dp))
                        Text("$fecha · ${cita.cita.hora}", fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text("S/ ${"%.2f".format(Locale.US, cita.cita.precio)}", fontSize = 16.sp,
                        color = verde, fontWeight = FontWeight.Bold)
                    EstadoCitaChip(estado)
                }
            }
            if (mostrarCancelar || mostrarReprogramar) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                    if (mostrarReprogramar) {
                        TextButton(onClick = onReprogramar, enabled = !cancelando) {
                            Icon(Icons.Filled.EditCalendar, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(5.dp))
                            Text("Reprogramar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (mostrarCancelar) {
                        TextButton(onClick = onCancelar, enabled = !cancelando) {
                            Text(if (cancelando) "Cancelando…" else "Cancelar",
                                color = MaterialTheme.colorScheme.error, fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
