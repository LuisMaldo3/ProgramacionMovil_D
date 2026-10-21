package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.VetEmptyState
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onVolver: () -> Unit,
    onPedirCita: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val cancelandoId by vm.cancelandoId.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var pestana by rememberSaveable { mutableIntStateOf(0) }
    var confirmar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        if (!mensaje.isNullOrBlank()) {
            snackbar.showSnackbar(mensaje.orEmpty())
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = "Mis citas",
                onVolver = onVolver
            )
        },
        bottomBar = {
            BarraNavegacionDueno(
                seleccionado = Rutas.MIS_CITAS,
                onSeleccionar = onSeleccionarTab
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPedirCita,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pedir cita"
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PESTAÑAS PRÓXIMAS / HISTORIAL
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Próximas", "Historial").forEachIndexed { indice, nombre ->
                        val activo = pestana == indice
                        Surface(
                            onClick = { pestana = indice },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (activo) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (activo) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = nombre,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            val visibles = if (pestana == 0) proximas else historial

            if (visibles.isEmpty()) {
                item {
                    VetEmptyState(
                        titulo = if (pestana == 0) "Sin citas próximas" else "Historial vacío",
                        descripcion = if (pestana == 0)
                            "No tienes citas agendadas actualmente. ¡Reserva una consulta para tu mascota!"
                        else "Aquí aparecerán las citas que hayan sido completadas o atendidas.",
                        icono = Icons.Default.EventNote,
                        accionTexto = if (pestana == 0) "Pedir una cita" else null,
                        onAccion = if (pestana == 0) onPedirCita else null,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
            } else {
                items(
                    items = visibles,
                    key = { it.cita.id }
                ) { detalle ->
                    CitaCard(
                        cita = detalle,
                        mostrarCancelar = pestana == 0 && vm.puedeCancelar(detalle),
                        cancelando = cancelandoId == detalle.cita.id,
                        onCancelar = { confirmar = detalle }
                    )
                }
            }

            if (pestana == 0 && visibles.isNotEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFFFF7ED),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFEDD5))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFFC2410C),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Puedes cancelar hasta 2 horas antes de la hora pactada.",
                                color = Color(0xFF9A3412),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(60.dp)) }
        }
    }

    confirmar?.let { detalle ->
        AlertDialog(
            onDismissRequest = { confirmar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text(
                    "¿Deseas cancelar la cita de ${detalle.mascota.mascota.nombre} para ${detalle.servicio.nombre}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmar = null
                        vm.cancelar(detalle)
                    }
                ) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = null }) {
                    Text("Volver")
                }
            }
        )
    }
}
