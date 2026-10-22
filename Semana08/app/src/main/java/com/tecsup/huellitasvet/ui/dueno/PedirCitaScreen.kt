package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.R
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.DialogoExitoAnimado
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val VerdeCita = Color(0xFF087F6D)
private val VerdeOscuroCita = Color(0xFF07584D)
private val FondoCita = Color(0xFFF3F7F5)
private val MentaCita = Color(0xFFE0F5EC)
private val TextoCita = Color(0xFF172B3A)
private val SecundarioCita = Color(0xFF73808B)
private val BordeCita = Color(0xFFE0E8E5)
private val GrisOcupado = Color(0xFFF0F2F4)

/** El formulario conserva los datos y acciones del ViewModel; aquí solo cambia la presentación. */
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
    val listo = mascota != null && servicio != null && hora != null && !vm.horaDeshabilitada(hora!!)
    val pasoActivo = when {
        mascota == null || servicio == null -> 1
        hora == null -> 2
        else -> 3
    }

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
            etiquetaEstado = "Pendiente de confirmación",
            onContinuar = { citaRegistrada = false; onConfirmada() }
        )
    }

    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 660) / 15).coerceIn(13, 18).dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoCita,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(FondoCita).height(66.dp)
                    .padding(start = 14.dp, end = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(onClick = onVolver, shape = CircleShape, color = Color.White,
                    border = BorderStroke(1.dp, BordeCita), modifier = Modifier.size(42.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.ArrowBack, "Volver", tint = TextoCita, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text("Nueva cita", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextoCita)
                    Text("Reserva un espacio para su cuidado", fontSize = 12.sp, color = SecundarioCita)
                }
                Surface(color = MentaCita, shape = CircleShape) {
                    Icon(Icons.Filled.Favorite, "Cuidado para tu mascota", tint = VerdeCita,
                        modifier = Modifier.padding(10.dp).size(19.dp))
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().background(Color.White)
                    .navigationBarsPadding().padding(start = 17.dp, end = 17.dp, top = 11.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(19.dp),
                    color = VerdeOscuroCita,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        Modifier.fillMaxWidth().background(
                            Brush.linearGradient(listOf(Color(0xFF07584D), Color(0xFF128C72), Color(0xFF20A68A)))
                        ).padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        Surface(shape = RoundedCornerShape(13.dp), color = Color.White.copy(alpha = .16f)) {
                            Icon(Icons.Filled.EventNote, null, tint = Color.White,
                                modifier = Modifier.padding(10.dp).size(21.dp))
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(
                                if (mascota != null && servicio != null) "${mascota.nombre} · ${servicio.nombre}"
                                else "Tu próxima visita",
                                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                            val dia = fecha.format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE")))
                            Text(
                                "${dia.replaceFirstChar { it.uppercase() }} · ${hora ?: "Elige una hora"}",
                                color = Color.White.copy(alpha = .84f), fontSize = 12.sp
                            )
                        }
                        Text(
                            servicio?.let { "S/ ${"%.2f".format(Locale.US, it.precio)}" } ?: "—",
                            color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                if (!aceptarCitas) {
                    Surface(color = Color(0xFFFFF1CB), shape = RoundedCornerShape(12.dp)) {
                        Text("La clínica no está recibiendo citas", color = Color(0xFF8B5600), fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp))
                    }
                }
                if (!error.isNullOrBlank()) {
                    Text(error.orEmpty(), color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }
                Button(
                    onClick = { vm.confirmar() },
                    enabled = listo && !procesando && aceptarCitas,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCita,
                        disabledContainerColor = Color(0xFFA7CFC4),
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    )
                ) {
                    if (procesando) {
                        CircularProgressIndicator(Modifier.size(21.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Confirmar cita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.ArrowForwardIos, null, modifier = Modifier.size(13.dp))
                    }
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState())
                .padding(start = 17.dp, end = 17.dp, top = 5.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(gapPantalla)
        ) {
            HeroCita()
            ProgresoCita(pasoActivo)

            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                EncabezadoPasoCita(1, "Elige a tu mascota", "¿Quién recibirá la atención?", pasoActivo >= 1)
                if (mascotas.isEmpty()) {
                    Surface(color = Color.White, shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, BordeCita)) {
                        Text("Primero registra una mascota desde la sección Mascotas.",
                            fontSize = 14.sp, color = SecundarioCita,
                            modifier = Modifier.fillMaxWidth().padding(17.dp))
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        mascotas.forEach { m ->
                            val seleccionado = m.id == mascotaId
                            val fondo by animateColorAsState(
                                if (seleccionado) MentaCita else Color.White,
                                label = "fondo de mascota"
                            )
                            Surface(
                                onClick = { vm.elegirMascota(m.id) },
                                color = fondo,
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(if (seleccionado) 1.7.dp else 1.dp,
                                    if (seleccionado) VerdeCita else BordeCita),
                                modifier = Modifier.width(135.dp).height(114.dp)
                            ) {
                                Box(Modifier.fillMaxSize().padding(10.dp)) {
                                    Column(
                                        Modifier.align(Alignment.Center),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        MascotaAvatar(
                                            especie = runCatching { Especie.valueOf(m.especie) }.getOrDefault(Especie.OTRO),
                                            tamano = 55.dp
                                        )
                                        Text(m.nombre, fontSize = 14.sp,
                                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (seleccionado) VerdeOscuroCita else TextoCita,
                                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                    if (seleccionado) {
                                        Icon(Icons.Filled.CheckCircle, null, tint = VerdeCita,
                                            modifier = Modifier.align(Alignment.TopEnd).size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                EncabezadoPasoCita(2, "Escoge un servicio", "Atención pensada para su bienestar", pasoActivo >= 2)
                if (servicios.isEmpty()) {
                    Text("No hay servicios activos disponibles.", fontSize = 14.sp, color = SecundarioCita)
                } else {
                    servicios.forEach { s ->
                        TarjetaServicioCita(
                            servicio = s,
                            seleccionado = servicioId == s.id,
                            onClick = { vm.elegirServicio(s.id) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                EncabezadoPasoCita(3, "Encuentra tu horario", "Elige el día y la hora que mejor te convenga", pasoActivo >= 3)
                Text("FECHA", fontSize = 11.sp, letterSpacing = 1.2.sp,
                    fontWeight = FontWeight.ExtraBold, color = SecundarioCita)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val hoy = LocalDate.now()
                    (0L..7L).forEach { dias ->
                        val dia = hoy.plusDays(dias)
                        val seleccionado = dia == fecha
                        val fondo by animateColorAsState(
                            if (seleccionado) VerdeCita else Color.White,
                            label = "fondo de fecha"
                        )
                        Surface(
                            onClick = { vm.elegirFecha(dia) },
                            color = fondo,
                            shape = RoundedCornerShape(17.dp),
                            border = if (seleccionado) null else BorderStroke(1.dp, BordeCita),
                            shadowElevation = if (seleccionado) 3.dp else 0.dp,
                            modifier = Modifier.width(61.dp).height(72.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                Text(
                                    if (dias == 0L) "HOY" else dia.format(DateTimeFormatter.ofPattern("EEE", Locale("es", "PE"))).uppercase(),
                                    color = if (seleccionado) Color.White.copy(alpha = .84f) else SecundarioCita,
                                    fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = .5.sp
                                )
                                Spacer(Modifier.height(3.dp))
                                Text("${dia.dayOfMonth}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                                    color = if (seleccionado) Color.White else TextoCita)
                            }
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("HORA", fontSize = 11.sp, letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.ExtraBold, color = SecundarioCita)
                    Spacer(Modifier.weight(1f))
                    val disponibles = Horarios.todos.count { it !in horasOcupadas && !vm.horaDeshabilitada(it) }
                    Surface(color = MentaCita, shape = CircleShape) {
                        Text("$disponibles disponibles", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                            color = VerdeOscuroCita, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                    }
                }
                Horarios.todos.chunked(3).forEach { fila ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { turno ->
                            val deshabilitado = turno in horasOcupadas || vm.horaDeshabilitada(turno)
                            val seleccionado = turno == hora && !deshabilitado
                            val fondo by animateColorAsState(
                                when {
                                    deshabilitado -> GrisOcupado
                                    seleccionado -> VerdeCita
                                    else -> Color.White
                                }, label = "fondo de hora"
                            )
                            Surface(
                                onClick = { vm.elegirHora(turno) },
                                enabled = !deshabilitado,
                                modifier = Modifier.weight(1f).height(46.dp),
                                color = fondo,
                                border = if (deshabilitado || seleccionado) null else BorderStroke(1.dp, BordeCita),
                                shape = RoundedCornerShape(14.dp),
                                shadowElevation = if (seleccionado) 2.dp else 0.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        turno,
                                        color = when {
                                            deshabilitado -> Color(0xFFB6BEC4)
                                            seleccionado -> Color.White
                                            else -> TextoCita
                                        },
                                        textDecoration = if (deshabilitado) TextDecoration.LineThrough else null,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        repeat(3 - fila.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroCita() {
    Box(Modifier.fillMaxWidth().height(148.dp).clip(RoundedCornerShape(24.dp))) {
        Image(
            painter = painterResource(R.drawable.servicio_consulta),
            contentDescription = "Perro y gato durante una consulta veterinaria",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier.fillMaxSize().background(
                Brush.horizontalGradient(
                    0f to Color(0xFF064C42).copy(alpha = .98f),
                    .57f to Color(0xFF07584D).copy(alpha = .89f),
                    1f to Color(0xFF07584D).copy(alpha = .15f)
                )
            )
        )
        Column(
            Modifier.align(Alignment.CenterStart).fillMaxWidth(.75f).padding(start = 17.dp, end = 5.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Surface(color = Color.White.copy(alpha = .17f), shape = CircleShape) {
                Text("HUELLITAS · CUIDADO CERCANO", color = Color.White,
                    fontSize = 9.sp, letterSpacing = .6.sp, fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
            }
            Text("Su bienestar\nva primero", color = Color.White, fontSize = 24.sp,
                lineHeight = 27.sp, fontWeight = FontWeight.ExtraBold)
            Text("Agenda su visita en unos pasos", color = Color.White.copy(alpha = .88f), fontSize = 12.sp)
        }
        Surface(
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 13.dp, bottom = 12.dp),
            color = Color.White.copy(alpha = .94f), shape = CircleShape
        ) {
            Row(Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Icon(Icons.Filled.CheckCircle, null, tint = VerdeCita, modifier = Modifier.size(14.dp))
                Text("Atención profesional", color = VerdeOscuroCita, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ProgresoCita(pasoActivo: Int) {
    Surface(color = Color.White, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, BordeCita)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mascota", "Servicio", "Horario").forEachIndexed { index, nombre ->
                val paso = index + 1
                val activo = paso == pasoActivo
                val completado = paso < pasoActivo
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = if (activo || completado) VerdeCita else Color(0xFFF0F3F2),
                        modifier = Modifier.size(23.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(if (completado) "✓" else "$paso", fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (activo || completado) Color.White else SecundarioCita)
                        }
                    }
                    Text(nombre, fontSize = 11.sp,
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
                        color = if (activo) VerdeOscuroCita else SecundarioCita)
                }
            }
        }
    }
}

@Composable
private fun EncabezadoPasoCita(numero: Int, titulo: String, ayuda: String, activo: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Surface(color = if (activo) VerdeCita else Color(0xFFE4EAE7), shape = RoundedCornerShape(12.dp)) {
            Text("0$numero", color = if (activo) Color.White else SecundarioCita,
                fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(titulo, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = TextoCita)
            Text(ayuda, fontSize = 12.sp, color = SecundarioCita)
        }
    }
}

@Composable
private fun TarjetaServicioCita(servicio: Servicio, seleccionado: Boolean, onClick: () -> Unit) {
    val icono: ImageVector = when {
        servicio.nombre.contains("vacun", true) -> Icons.Filled.Vaccines
        servicio.nombre.contains("baño", true) || servicio.nombre.contains("corte", true) -> Icons.Filled.ContentCut
        else -> Icons.Filled.Favorite
    }
    val recursoFoto = when {
        servicio.nombre.contains("vacun", true) -> R.drawable.servicio_vacunacion
        servicio.nombre.contains("baño", true) || servicio.nombre.contains("corte", true) -> R.drawable.servicio_bano
        servicio.nombre.contains("aliment", true) -> R.drawable.servicio_alimentacion
        else -> R.drawable.servicio_consulta
    }
    val borde = if (seleccionado) VerdeCita else BordeCita
    val fondo by animateColorAsState(if (seleccionado) MentaCita else Color.White, label = "fondo de servicio")
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        color = fondo,
        border = BorderStroke(if (seleccionado) 1.7.dp else 1.dp, borde),
        shadowElevation = if (seleccionado) 3.dp else 0.dp
    ) {
        Row(Modifier.fillMaxWidth().height(86.dp).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(11.dp)) {
            Box(Modifier.width(76.dp).fillMaxHeight().clip(RoundedCornerShape(13.dp))) {
                Image(painterResource(recursoFoto), contentDescription = null,
                    modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(Modifier.align(Alignment.BottomStart).padding(5.dp)
                    .clip(CircleShape).background(Color.White.copy(alpha = .94f)).padding(5.dp)) {
                    Icon(icono, null, tint = VerdeOscuroCita, modifier = Modifier.size(13.dp))
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(servicio.nombre, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold,
                    color = TextoCita, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Schedule, null, tint = SecundarioCita, modifier = Modifier.size(14.dp))
                    Text("${servicio.duracionMin} min", fontSize = 12.sp, color = SecundarioCita)
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("S/ ${"%.2f".format(Locale.US, servicio.precio)}", fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold, color = VerdeOscuroCita)
                if (seleccionado) {
                    Icon(Icons.Filled.CheckCircle, null, tint = VerdeCita, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
