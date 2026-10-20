package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import java.util.Locale

/** [ENTREGADO] Ejemplo MVVM funcional, con datos observados de Room por Flow. */
@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit) {
    val servicios by vm.servicios.collectAsStateWithLifecycle()
    Scaffold(topBar = { AppTopBar("Servicios veterinarios") }) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .padding(innerPadding).safeDrawingPadding(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cuidando cada huellita", style = MaterialTheme.typography.headlineMedium)
                    Text("Administra los servicios de la clínica", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ResumenServicio("Servicios", "${servicios.size}", Modifier.weight(1f))
                        ResumenServicio("Activos", "${servicios.count { it.activo }}", Modifier.weight(1f))
                    }
                }
            }
            item {
                Text("Catálogo de servicios", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 12.dp))
            }
            items(servicios, key = { it.id }) { servicio ->
                ServicioItem(
                    servicio = servicio,
                    onToggle = { vm.alternar(servicio) },
                    onEditar = { onEditar(servicio.id) }
                )
            }
            item {
                ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Esta es la pantalla de ejemplo del Paso 0. Login, mascotas, citas y formularios se completan en los próximos pasos del PDF.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenServicio(etiqueta: String, valor: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(valor, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineMedium)
            Text(etiqueta, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ServicioItem(servicio: Servicio, onToggle: () -> Unit, onEditar: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(iconoDe(servicio.icono), null, tint = MaterialTheme.colorScheme.primary) }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(servicio.nombre, style = MaterialTheme.typography.titleMedium)
                Text(servicio.descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${servicio.duracionMin} min  ·  S/ ${String.format(Locale.US, "%.2f", servicio.precio)}",
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
            Switch(checked = servicio.activo, onCheckedChange = { onToggle() })
        }
    }
}

private fun iconoDe(icono: String): ImageVector = when (icono) {
    "heart" -> Icons.Default.Favorite
    "syringe" -> Icons.Default.CheckCircle
    "scissors" -> Icons.Default.Star
    else -> Icons.Default.Favorite
}
