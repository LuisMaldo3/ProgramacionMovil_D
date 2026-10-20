package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistorialItem(fecha: String, servicio: String, diagnostico: String, peso: Double?) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text("●", color = MaterialTheme.colorScheme.primary)
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(fecha, style = MaterialTheme.typography.labelMedium)
            Text(servicio, style = MaterialTheme.typography.titleMedium)
            Text(diagnostico)
            if (peso != null) Text("Peso: $peso kg", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(6.dp))
        }
    }
}
