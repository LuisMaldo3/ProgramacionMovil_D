package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetEmptyState
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onVolver: () -> Unit,
    onAtender: (Int) -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val procesando by vm.procesandoId.collectAsState()
    var citaCancelar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            snackbar.showSnackbar(mensaje.orEmpty())
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        bottomBar = { BarraNavegacionVeterinario(Rutas.AGENDA, onSeleccionarTab) },
        topBar = {
            AppTopBar(
                titulo = "Agenda médica",
                onVolver = onVolver
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // DATE CHIPS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0L..6L).forEach { dia ->
                    val fechaChip = LocalDate.now().plusDays(dia)
                    val activo = fecha == fechaChip
                    val etiqueta = if (dia == 0L) "Hoy ${fechaChip.dayOfMonth}"
                    else fechaChip.format(DateTimeFormatter.ofPattern("EEE d", Locale("es", "PE")))
                        .replaceFirstChar { it.uppercase() }

                    FilterChip(
                        selected = activo,
                        onClick = { vm.seleccionarFecha(fechaChip) },
                        label = { Text(etiqueta, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            if (citas.isEmpty()) {
                VetEmptyState(
                    titulo = "Sin citas agendadas",
                    descripcion = "No hay consultas ni servicios programados para este día.",
                    icono = Icons.Default.CalendarMonth,
                    modifier = Modifier.padding(top = 40.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(citas, key = { it.cita.id }) { detalle ->
                        TarjetaAgendaCita(
                            detalle = detalle,
                            procesando = procesando == detalle.cita.id,
                            onConfirmar = { vm.confirmar(detalle) },
                            onAtender = { onAtender(detalle.cita.id) },
                            onCancelar = { citaCancelar = detalle }
                        )
                    }
                }
            }
        }
    }

    citaCancelar?.let { detalle ->
        AlertDialog(
            onDismissRequest = { citaCancelar = null },
            title = { Text("Cancelar cita") },
            text = { Text("¿Deseas cancelar la cita de ${detalle.mascota.mascota.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    val target = citaCancelar
                    citaCancelar = null
                    target?.let { vm.cancelar(it) }
                }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { citaCancelar = null }) {
                    Text("Volver")
                }
            }
        )
    }
}

@Composable
private fun TarjetaAgendaCita(
    detalle: CitaConDetalle,
    procesando: Boolean,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit,
    onCancelar: () -> Unit
) {
    val estadoEnum = runCatching { EstadoCita.valueOf(detalle.cita.estado) }.getOrDefault(EstadoCita.PENDIENTE)
    val especieEnum = runCatching { Especie.valueOf(detalle.mascota.mascota.especie.uppercase()) }.getOrDefault(Especie.OTRO)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MascotaAvatar(especie = especieEnum, tamano = 56.dp)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${detalle.mascota.mascota.nombre} · ${detalle.servicio.nombre}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Dueño: ${detalle.mascota.dueno.nombre} · ${detalle.cita.hora}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                EstadoCitaChip(estadoEnum)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (detalle.cita.estado == "PENDIENTE") {
                    OutlinedButton(
                        onClick = onCancelar,
                        enabled = !procesando,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirmar,
                        enabled = !procesando,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Confirmar", style = MaterialTheme.typography.labelLarge)
                    }
                } else if (detalle.cita.estado == "CONFIRMADA") {
                    Button(
                        onClick = onAtender,
                        enabled = !procesando,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Atender cita", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
