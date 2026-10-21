package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AzulAgenda = Color(0xFF1D4ED8)
private val FondoAgenda = Color(0xFFF7F9FF)
private val TextoAgenda = Color(0xFF1F2937)
private val GrisAgenda = Color(0xFF8390A0)

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
        containerColor = FondoAgenda,
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        bottomBar = { BarraNavegacionVeterinario(Rutas.AGENDA, onSeleccionarTab) },
        topBar = {
            Row(
                Modifier.fillMaxWidth().height(61.dp).padding(horizontal = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = TextoAgenda)
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Agenda", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoAgenda)
                }
                Spacer(Modifier.width(48.dp))
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0L..6L).forEach { dia ->
                    val fechaChip = LocalDate.now().plusDays(dia)
                    val activo = fecha == fechaChip
                    val etiqueta = if (dia == 0L) "Hoy ${fechaChip.dayOfMonth}"
                    else fechaChip.format(DateTimeFormatter.ofPattern("EEE d", Locale("es", "PE")))
                        .replaceFirstChar { it.uppercase() }
                    Surface(
                        onClick = { vm.seleccionarFecha(fechaChip) },
                        color = if (activo) AzulAgenda else Color.White,
                        contentColor = if (activo) Color.White else GrisAgenda,
                        border = if (activo) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(etiqueta, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp))
                    }
                }
            }

            if (citas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay citas para esta fecha", color = GrisAgenda, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 18.dp, end = 20.dp, top = 3.dp, bottom = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(citas, key = { it.cita.id }) { item ->
                        FilaAgenda(
                            item = item,
                            procesando = procesando == item.cita.id,
                            onConfirmar = { vm.confirmar(item) },
                            onAtender = { onAtender(item.cita.id) },
                            onCancelar = { citaCancelar = item }
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
                    citaCancelar = null
                    vm.cancelar(detalle)
                }) { Text("Sí, cancelar", color = Color(0xFFDC2626)) }
            },
            dismissButton = {
                TextButton(onClick = { citaCancelar = null }) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun FilaAgenda(
    item: CitaConDetalle,
    procesando: Boolean,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit,
    onCancelar: () -> Unit
) {
    val cita = item.cita
    val estado = cita.estado
    Row(Modifier.fillMaxWidth().heightIn(min = 110.dp)) {
        Column(
            Modifier.width(65.dp).height(108.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(cita.hora, color = AzulAgenda, fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 12.dp))
            Spacer(Modifier.height(9.dp))
            Box(Modifier.padding(start = 51.dp).width(2.dp).height(72.dp)
                .background(Color(0xFFDBEAFE)))
        }
        Spacer(Modifier.width(5.dp))
        Surface(
            modifier = Modifier.weight(1f).heightIn(min = 108.dp),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(Modifier.padding(11.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    val especie = runCatching {
                        Especie.valueOf(item.mascota.mascota.especie)
                    }.getOrDefault(Especie.OTRO)
                    MascotaAvatar(especie = especie, tamano = 44.dp)
                    Spacer(Modifier.width(9.dp))
                    Column(Modifier.weight(1f)) {
                        Text("${item.mascota.mascota.nombre} · ${item.servicio.nombre}",
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextoAgenda)
                        Spacer(Modifier.height(4.dp))
                        val duenio = item.mascota.dueno.nombre.trim().split(" ")
                        val nombreBreve = duenio.firstOrNull().orEmpty() + " " +
                            (duenio.getOrNull(1)?.firstOrNull()?.let { "$it." } ?: "")
                        Text("Dueño: $nombreBreve", fontSize = 11.sp, color = GrisAgenda,
                            maxLines = 1)
                    }
                    if (estado == "PENDIENTE" || estado == "CONFIRMADA") {
                        var expandido by remember { mutableStateOf(false) }
                        Box {
                            IconButton(onClick = { expandido = true }, modifier = Modifier.size(25.dp)) {
                                Icon(Icons.Filled.MoreVert, "Opciones", tint = GrisAgenda,
                                    modifier = Modifier.size(18.dp))
                            }
                            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                                DropdownMenuItem(text = { Text("Cancelar cita") }, onClick = {
                                    expandido = false
                                    onCancelar()
                                })
                            }
                        }
                    }
                }
                Spacer(Modifier.height(7.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (titulo, fondo, letra) = when (estado) {
                        "CONFIRMADA" -> Triple("Confirmada", Color(0xFFDCFCE7), Color(0xFF16A34A))
                        "PENDIENTE" -> Triple("Pendiente", Color(0xFFFFF2C6), Color(0xFFD97706))
                        "ATENDIDA" -> Triple("Atendida", Color(0xFFE2E8F0), Color(0xFF475569))
                        else -> Triple("Cancelada", Color(0xFFFEE2E2), Color(0xFFDC2626))
                    }
                    Surface(color = fondo, shape = RoundedCornerShape(30.dp)) {
                        Text(titulo, fontSize = 10.sp, color = letra,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    if (estado == "PENDIENTE" || estado == "CONFIRMADA") {
                        Button(
                            onClick = if (estado == "PENDIENTE") onConfirmar else onAtender,
                            enabled = !procesando,
                            modifier = Modifier.height(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulAgenda),
                            contentPadding = PaddingValues(horizontal = 15.dp),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(if (estado == "PENDIENTE") "Confirmar" else "Atender", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
