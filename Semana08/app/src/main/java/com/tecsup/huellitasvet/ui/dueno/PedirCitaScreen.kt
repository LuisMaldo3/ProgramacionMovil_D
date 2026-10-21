package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.DialogoExitoAnimado
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val VerdeCita = Color(0xFF087F6D)
private val FondoCita = Color(0xFFFAFBF9)
private val MentaCita = Color(0xFFDDF7EF)
private val TextoCita = Color(0xFF172B3A)
private val SecundarioCita = Color(0xFF73808B)
private val BordeCita = Color(0xFFE0E8E5)
private val GrisOcupado = Color(0xFFF2F3F5)

/** Paso 9: mismo diseño del mockup, datos de Room y horarios reactivos. */
@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onVolver: () -> Unit,
    onConfirmada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val horasOcupadas by vm.horasOcupadas.collectAsState()
    val error by vm.error.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val aceptarCitas by vm.aceptarCitas.collectAsState()

    val mascota = mascotas.firstOrNull { it.id == mascotaId }
    val servicio = servicios.firstOrNull { it.id == servicioId }
    val listo = mascota != null && servicio != null && hora != null &&
        !vm.horaDeshabilitada(hora!!)

    var citaRegistrada by remember { mutableStateOf(false) }
    var nombreMascotaExito by remember { mutableStateOf("") }
    var servicioExito by remember { mutableStateOf("") }
    var fechaExito by remember { mutableStateOf("") }
    val mascotaActual by rememberUpdatedState(mascota)
    val servicioActual by rememberUpdatedState(servicio)
    val fechaActual by rememberUpdatedState(fecha)
    val horaActual by rememberUpdatedState(hora)
    LaunchedEffect(vm) {
        vm.citaCreada.collect {
            nombreMascotaExito = mascotaActual?.nombre.orEmpty()
            servicioExito = servicioActual?.nombre.orEmpty()
            fechaExito = fechaActual.format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", Locale("es", "PE"))) +
                " · " + (horaActual ?: "")
            citaRegistrada = true
        }
    }
    if (citaRegistrada) {
        DialogoExitoAnimado(
            titulo = "¡Cita registrada!",
            descripcion = "Tu solicitud quedó pendiente de confirmación. Te avisaremos cuando sea confirmada.",
            detalle = "$nombreMascotaExito · $servicioExito\n$fechaExito",
            boton = "Ver mis citas",
            onContinuar = { citaRegistrada = false; onConfirmada() }
        )
    }

    // Separación adaptable sin cortar formularios en pantallas pequeñas.
    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 660) / 11).coerceIn(12, 18).dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp)
                    .background(FondoCita).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Filled.ArrowBack, "Volver", tint = TextoCita, modifier = Modifier.size(21.dp))
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Pedir cita", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(Modifier.width(42.dp))
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(FondoCita)
                    .navigationBarsPadding()
                    .padding(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(13.dp))
                        .background(Color(0xFFEAFBF3))
                        .padding(horizontal = 14.dp, vertical = 11.dp)
                ) {
                    Text(
                        if (mascota != null && servicio != null)
                            "${mascota.nombre} · ${servicio.nombre}"
                        else "Resumen de la cita",
                        color = VerdeCita, fontWeight = FontWeight.Bold,
                        fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(3.dp))
                    val dia = fecha.format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE")))
                    Text(
                        "${dia.replaceFirstChar { it.uppercase() }} · ${hora ?: "Elige hora"} · " +
                            (servicio?.let { "S/ ${"%.2f".format(Locale.US, it.precio)}" } ?: "Elige servicio"),
                        fontSize = 13.sp, color = SecundarioCita
                    )
                }
                if (!aceptarCitas) {
                    Surface(color = Color(0xFFFFF2C6), shape = RoundedCornerShape(12.dp)) {
                        Text("La clínica no está recibiendo citas",
                            color = Color(0xFF92400E), fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp))
                    }
                }
                if (!error.isNullOrBlank()) {
                    Text(error.orEmpty(), color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }
                Button(
                    onClick = { vm.confirmar() },
                    enabled = listo && !procesando && aceptarCitas,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCita,
                        disabledContainerColor = Color(0xFF9FCFC1),
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    )
                ) {
                    if (procesando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Confirmar cita", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier.fillMaxSize().padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 7.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(gapPantalla)
        ) {
            EtiquetaCita("¿Quién viene?")
            if (mascotas.isEmpty()) {
                Text("Primero registra una mascota desde la sección Mascotas.",
                    fontSize = 15.sp, color = SecundarioCita)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    mascotas.forEach { m ->
                        val seleccionado = m.id == mascotaId
                        Surface(
                            onClick = { vm.elegirMascota(m.id) },
                            color = if (seleccionado) MentaCita else Color.White,
                            shape = RoundedCornerShape(40.dp),
                            border = BorderStroke(if (seleccionado) 1.5.dp else 1.dp,
                                if (seleccionado) VerdeCita else BordeCita)
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 8.dp, end = 17.dp, top = 5.dp, bottom = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(9.dp)
                            ) {
                                MascotaAvatar(
                                    especie = runCatching { Especie.valueOf(m.especie) }.getOrDefault(Especie.OTRO),
                                    tamano = 38.dp
                                )
                                Text(m.nombre, fontSize = 15.sp,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                                    color = if (seleccionado) VerdeCita else SecundarioCita)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(2.dp))
            EtiquetaCita("Servicio")
            if (servicios.isEmpty()) {
                Text("No hay servicios activos disponibles.", fontSize = 15.sp, color = SecundarioCita)
            } else {
                servicios.forEach { s ->
                    TarjetaServicioCita(
                        servicio = s,
                        seleccionado = servicioId == s.id,
                        onClick = { vm.elegirServicio(s.id) }
                    )
                }
            }

            Spacer(Modifier.height(2.dp))
            EtiquetaCita("Fecha")
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                val hoy = LocalDate.now()
                (0L..7L).forEach { dias ->
                    val dia = hoy.plusDays(dias)
                    val seleccionado = dia == fecha
                    Surface(
                        onClick = { vm.elegirFecha(dia) },
                        color = if (seleccionado) VerdeCita else Color.White,
                        shape = RoundedCornerShape(13.dp),
                        border = if (seleccionado) null else BorderStroke(1.dp, BordeCita),
                        modifier = Modifier.width(57.dp).height(54.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(dia.format(DateTimeFormatter.ofPattern("EEE", Locale("es", "PE")))
                                .replaceFirstChar { it.uppercase() },
                                color = if (seleccionado) Color.White else SecundarioCita,
                                fontWeight = FontWeight.Medium, fontSize = 13.sp)
                            Text("${dia.dayOfMonth}", fontSize = 17.sp, fontWeight = FontWeight.Bold,
                                color = if (seleccionado) Color.White else TextoCita)
                        }
                    }
                }
            }

            Spacer(Modifier.height(2.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                EtiquetaCita("Hora")
                Text("Ocupado = gris", fontSize = 13.sp, color = SecundarioCita)
            }
            // Los 18 horarios del PDF se distribuyen en 4 columnas, y toda la pantalla se desplaza.
            Horarios.todos.chunked(4).forEach { fila ->
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    fila.forEach { turno ->
                        val deshabilitado = turno in horasOcupadas || vm.horaDeshabilitada(turno)
                        val seleccionado = turno == hora && !deshabilitado
                        Surface(
                            onClick = { vm.elegirHora(turno) },
                            enabled = !deshabilitado,
                            modifier = Modifier.weight(1f).height(43.dp),
                            color = when {
                                deshabilitado -> GrisOcupado
                                seleccionado -> VerdeCita
                                else -> Color.White
                            },
                            border = if (deshabilitado || seleccionado) null else BorderStroke(1.dp, BordeCita),
                            shape = RoundedCornerShape(11.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    turno,
                                    color = when {
                                        deshabilitado -> Color(0xFFC0C4C8)
                                        seleccionado -> Color.White
                                        else -> TextoCita
                                    },
                                    textDecoration = if (deshabilitado) TextDecoration.LineThrough else null,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun EtiquetaCita(texto: String) {
    Text(texto, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
}

@Composable
private fun TarjetaServicioCita(
    servicio: Servicio,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(59.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionado) MentaCita else Color.White,
        border = BorderStroke(if (seleccionado) 1.5.dp else 1.dp,
            if (seleccionado) VerdeCita else BordeCita)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val icono: ImageVector = when {
                servicio.nombre.contains("vacun", true) -> Icons.Filled.Vaccines
                servicio.nombre.contains("baño", true) || servicio.nombre.contains("corte", true) -> Icons.Filled.ContentCut
                else -> Icons.Filled.Favorite
            }
            Icon(icono, contentDescription = null,
                tint = if (seleccionado) VerdeCita else Color(0xFF78828D),
                modifier = Modifier.size(23.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(servicio.nombre, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${servicio.duracionMin} min", fontSize = 13.sp, color = SecundarioCita)
            }
            Text("S/ ${"%.0f".format(Locale.US, servicio.precio)}",
                fontSize = 15.sp, fontWeight = FontWeight.Bold, color = VerdeCita)
        }
    }
}
