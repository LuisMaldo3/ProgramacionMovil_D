package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Resumen clínico completo: sin recortar diagnóstico ni tratamiento. Mantiene su API anterior. */
@Composable
fun HistorialItem(
    fecha: String,
    servicio: String,
    diagnostico: String,
    peso: Double?,
    tratamiento: String? = null
) {
    val fechaLegible = runCatching {
        LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es", "PE")))
            .replaceFirstChar { it.uppercaseChar() }
    }.getOrDefault(fecha)
    val verde = MaterialTheme.colorScheme.primary
    val subtitulo = MaterialTheme.colorScheme.onSurfaceVariant
    val icono = when {
        servicio.contains("vacun", ignoreCase = true) -> Icons.Filled.Vaccines
        servicio.contains("bañ", ignoreCase = true) || servicio.contains("corte", ignoreCase = true) -> Icons.Filled.ContentCut
        servicio.contains("desparasit", ignoreCase = true) -> Icons.Filled.Healing
        else -> Icons.Filled.MedicalServices
    }
    Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(11.dp)) {
            Surface(color = verde.copy(alpha = .11f), shape = RoundedCornerShape(13.dp)) {
                Icon(icono, null, tint = verde,
                    modifier = Modifier.padding(11.dp).size(25.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(servicio, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                Text(fechaLegible, fontSize = 13.sp, color = subtitulo)
            }
            Surface(color = Color(0xFFDCFCE7), shape = RoundedCornerShape(50.dp)) {
                Text("ATENDIDA", modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                    fontSize = 11.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha=.55f))
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("DIAGNÓSTICO", fontSize = 12.sp, color = subtitulo,
                fontWeight = FontWeight.Bold, letterSpacing = .7.sp)
            Text(diagnostico.ifBlank { "Atención registrada sin diagnóstico." },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface, lineHeight = 22.sp)
        }
        if (!tratamiento.isNullOrBlank()) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("TRATAMIENTO", fontSize = 12.sp, color = subtitulo,
                    fontWeight = FontWeight.Bold, letterSpacing = .7.sp)
                Text(tratamiento, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface, lineHeight = 22.sp)
            }
        }
        if (peso != null) {
            Surface(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha=.50f),
                shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Filled.MonitorWeight, "Peso registrado", tint = verde,
                        modifier = Modifier.size(22.dp))
                    Text("Peso en esta atención", fontSize = 13.sp, color = subtitulo,
                        modifier = Modifier.weight(1f))
                    Text("${"%.1f".format(Locale.US, peso)} kg", fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold, color = verde)
                }
            }
        }
    }
}
