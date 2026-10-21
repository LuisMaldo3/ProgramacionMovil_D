package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import com.tecsup.huellitasvet.ui.components.DialogoExitoAnimado
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.viewmodel.ReprogramarCitaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReprogramarCitaScreen(
    vm: ReprogramarCitaViewModel,
    onVolver: () -> Unit,
    onGuardada: () -> Unit
) {
    val cita by vm.cita.collectAsState()
    val cargado by vm.cargado.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupados by vm.ocupados.collectAsState()
    val guardando by vm.guardando.collectAsState()
    val guardada by vm.guardado.collectAsState()
    val error by vm.error.collectAsState()
    val verde = MaterialTheme.colorScheme.primary
    val fondo = MaterialTheme.colorScheme.background
    val superficies = MaterialTheme.colorScheme.surface
    val formato = remember { DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "PE")) }
    val formatoChip = remember { DateTimeFormatter.ofPattern("EEE", Locale("es", "PE")) }
    val detalle = cita
    val puedeCambiar = vm.puedeReprogramar(detalle)
    val destinoValido = hora != null && vm.disponible(hora!!, ocupados) &&
        (fecha.toString() != detalle?.cita?.fecha || hora != detalle?.cita?.hora)

    var mostrarReprogramada by remember { mutableStateOf(false) }
    LaunchedEffect(guardada) { if (guardada) mostrarReprogramada = true }
    if (mostrarReprogramada) {
        DialogoExitoAnimado(
            titulo = "¡Cita reprogramada!",
            descripcion = "El nuevo horario se guardó correctamente y está actualizado en tu agenda.",
            boton = "Ver mis citas",
            onContinuar = { mostrarReprogramada = false; onGuardada() }
        )
    }
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(error) {
        if (!error.isNullOrBlank()) {
            snackbar.showSnackbar(error.orEmpty())
            vm.limpiarError()
        }
    }

    Scaffold(
        containerColor = fondo,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Reprogramar cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = fondo)
            )
        },
        bottomBar = {
            Surface(color = superficies, shadowElevation = 8.dp) {
                Column(
                    Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = vm::confirmar,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        enabled = puedeCambiar && destinoValido && !guardando,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (guardando) {
                            CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                            Spacer(Modifier.width(10.dp))
                        } else {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Spacer(Modifier.width(9.dp))
                        }
                        Text(if (guardando) "Guardando..." else "Confirmar nueva fecha", fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        "Solo es posible cambiar citas con más de 2 horas de anticipación",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        if (detalle == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (cargado) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No se encontró una cita válida en tu cuenta")
                        TextButton(onClick = onVolver) { Text("Volver a Mis citas") }
                    }
                } else CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = superficies)
                    ) {
                        Column(Modifier.padding(19.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("CITA ACTUAL", style = MaterialTheme.typography.labelMedium, color = verde, fontWeight = FontWeight.Bold)
                            Text(detalle.servicio.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Mascota: ${detalle.mascota.mascota.nombre}", style = MaterialTheme.typography.bodyMedium)
                            Text("${runCatching { LocalDate.parse(detalle.cita.fecha).format(formato) }.getOrDefault(detalle.cita.fecha)} · ${detalle.cita.hora} · S/ %.2f".format(Locale.US, detalle.cita.precio),
                                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            AssistChip(onClick = {}, label = { Text(detalle.cita.estado) }, leadingIcon = {
                                Icon(Icons.Filled.Event, contentDescription = null, Modifier.size(17.dp))
                            })
                        }
                    }
                }
                if (!puedeCambiar) {
                    item {
                        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.errorContainer) {
                            Text(
                                "Esta cita no puede reprogramarse: debe estar pendiente o confirmada y faltar más de 2 horas.",
                                modifier = Modifier.padding(17.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                } else {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.CalendarMonth, null, tint = verde)
                            Text("Elige otro día", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            items((0L..7L).map { LocalDate.now().plusDays(it) }) { dia ->
                                val activo = dia == fecha
                                Surface(
                                    onClick = { vm.elegirFecha(dia) },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (activo) verde else superficies,
                                    border = if (activo) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.width(79.dp).height(88.dp)
                                ) {
                                    Column(
                                        Modifier.padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(dia.format(formatoChip).replaceFirstChar { it.uppercaseChar() },
                                            color = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.labelMedium)
                                        Text(dia.dayOfMonth.toString(),
                                            color = if (activo) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.Schedule, null, tint = verde)
                            Column {
                                Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(fecha.format(formato).replaceFirstChar { it.uppercaseChar() },
                                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    items(Horarios.todos.chunked(3)) { fila ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            fila.forEach { turno ->
                                val disponible = vm.disponible(turno, ocupados)
                                val activo = turno == hora
                                Surface(
                                    onClick = { vm.elegirHora(turno) },
                                    enabled = disponible,
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    color = when {
                                        activo -> verde
                                        disponible -> superficies
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    border = if (!activo && disponible) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(turno,
                                            color = when {
                                                activo -> MaterialTheme.colorScheme.onPrimary
                                                disponible -> MaterialTheme.colorScheme.onSurface
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                                            },
                                            fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                            repeat(3 - fila.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                    item {
                        Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Resumen del cambio", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("Antes: ${runCatching { LocalDate.parse(detalle.cita.fecha).format(formato) }.getOrDefault(detalle.cita.fecha)} · ${detalle.cita.hora}")
                                Text("Ahora: ${fecha.format(formato)} · ${hora ?: "Elige una hora"}")
                                Text("El precio y los datos del servicio se mantienen", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
