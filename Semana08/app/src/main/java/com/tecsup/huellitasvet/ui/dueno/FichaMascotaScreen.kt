package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onEditar: () -> Unit,
    onPedirCita: () -> Unit,
    onSeleccionarTab: (String) -> Unit,
    onEliminada: () -> Unit
) {
    val mascota by vm.mascota.collectAsState()
    val historialFlow = remember(vm, mascotaId) { vm.historial(mascotaId) }
    val historial by historialFlow.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val procesando by vm.procesando.collectAsState()
    var tab by rememberSaveable { mutableIntStateOf(1) }
    var confirmarEliminacion by remember { mutableStateOf(false) }

    LaunchedEffect(vm) { vm.eliminado.collect { onEliminada() } }

    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            title = { Text("Eliminar mascota") },
            text = { Text("Solo se podrá eliminar si no tiene citas pendientes o activas. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminacion = false
                    mascota?.let { vm.eliminar(it) }
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminacion = false }) { Text("Cancelar") }
            }
        )
    }

    if (mensaje != null) {
        AlertDialog(
            onDismissRequest = { vm.limpiarMensaje() },
            title = { Text("HuellitasVet") },
            text = { Text(mensaje.orEmpty()) },
            confirmButton = {
                TextButton(onClick = { vm.limpiarMensaje() }) { Text("Aceptar") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = mascota?.nombre ?: "Ficha de mascota",
                onVolver = onVolver
            )
        },
        bottomBar = {
            BarraNavegacionDueno(seleccionado = Rutas.MASCOTAS, onSeleccionar = onSeleccionarTab)
        }
    ) { padding ->
        val actual = mascota
        if (actual == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                CabeceraFicha(actual, historial)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onPedirCita,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.EventAvailable, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Pedir cita", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onEditar,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.Edit, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Editar", fontWeight = FontWeight.Bold)
                    }
                }

                // Selector Datos / Historial
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(4.dp)
                ) {
                    listOf("Datos generales", "Historial clínico").forEachIndexed { index, titulo ->
                        val activo = tab == index
                        Surface(
                            onClick = { tab = index },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (activo) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (activo) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = titulo,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                if (tab == 0) {
                    DatosFicha(actual)
                    OutlinedButton(
                        onClick = { confirmarEliminacion = true },
                        enabled = !procesando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.DeleteOutline, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar mascota", fontWeight = FontWeight.Bold)
                    }
                } else {
                    if (historial.isEmpty()) {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.Pets,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Text(
                                    text = "Aún no tiene atenciones registradas",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        historial.forEachIndexed { index, cita ->
                            LineaHistorial(cita, ultima = index == historial.lastIndex)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CabeceraFicha(m: Mascota, historial: List<CitaConDetalle>) {
    val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
    val especieTexto = when (m.especie.uppercase()) { "GATO" -> "Gato"; "PERRO" -> "Perro"; else -> "Mascota" }
    val sexoTexto = if (m.sexo.uppercase() == "HEMBRA") "Hembra" else "Macho"
    val vacunas = historial.any { it.servicio.nombre.contains("vacun", ignoreCase = true) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MascotaAvatar(
                especie = runCatching { Especie.valueOf(m.especie.uppercase()) }.getOrDefault(Especie.OTRO),
                tamano = 72.dp
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = m.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$especieTexto ${m.raza} · $edad años",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$sexoTexto · ${"%.1f".format(Locale.US, m.peso)} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (vacunas) "Vacunas al día" else "Ficha registrada",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DatosFicha(m: Mascota) {
    val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            DatoFila("Nombre", m.nombre)
            DatoFila("Especie", m.especie.lowercase().replaceFirstChar { it.uppercase() })
            DatoFila("Raza", m.raza.ifBlank { "Sin especificar" })
            DatoFila("Edad", "$edad ${if (edad == 1) "año" else "años"}")
            DatoFila("Sexo", m.sexo.lowercase().replaceFirstChar { it.uppercase() })
            DatoFila("Peso actual", "${"%.1f".format(Locale.US, m.peso)} kg")
        }
    }
}

@Composable
private fun DatoFila(nombre: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun LineaHistorial(detalle: CitaConDetalle, ultima: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(20.dp),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (!ultima) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(90.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(Modifier.padding(14.dp)) {
                HistorialItem(
                    fecha = detalle.cita.fecha,
                    servicio = detalle.servicio.nombre,
                    diagnostico = detalle.cita.diagnostico.orEmpty(),
                    peso = detalle.cita.pesoRegistrado
                )
            }
        }
    }
}
