package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import com.tecsup.huellitasvet.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Verde = Color(0xFF087F6D)
private val Fondo = Color(0xFFF4F8F6)

/** Mantiene exactamente la firma usada por NavVet del Paso 7. */
@Composable
fun InicioScreen(
    vm: InicioViewModel,
    nombreDueno: String,
    onVerMascotas: () -> Unit,
    onMascota: (Int) -> Unit,
    onAgregarMascota: () -> Unit,
    onPedirCita: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val cita by vm.proximaCita.collectAsState()
    var mostrarContenido by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mostrarContenido = true }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraNavegacionDueno(
                seleccionado = Rutas.INICIO,
                onSeleccionar = onSeleccionarTab
            )
        }
    ) { insets ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
                .background(Brush.verticalGradient(listOf(Color(0xFFEAF6F1), Fondo, Fondo)))
        ) {
            AnimatedVisibility(
                visible = mostrarContenido,
                modifier = Modifier.fillMaxSize(),
                enter = fadeIn(tween(500)) + slideInVertically(
                    animationSpec = tween(500), initialOffsetY = { it / 14 }
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                        .padding(start = 22.dp, end = 22.dp, top = 12.dp, bottom = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(62.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("HOLA DE NUEVO", fontSize = 10.sp, letterSpacing = 1.6.sp,
                                fontWeight = FontWeight.ExtraBold, color = Verde)
                            Text(
                                text = "¡Hola, ${nombreDueno.ifBlank { "bienvenido" }}!",
                                fontSize = 23.sp, fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF173A36), maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            onClick = { onSeleccionarTab(Rutas.MIS_CITAS) },
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF173A36)
                        ) {
                            Box(Modifier.size(46.dp), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Notifications, "Ver mis citas",
                                    modifier = Modifier.size(21.dp), tint = Color.White)
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(194.dp),
                        shape = RoundedCornerShape(26.dp), color = Color(0xFF123E3A),
                        shadowElevation = 8.dp
                    ) {
                        Box {
                            Image(painter = painterResource(R.drawable.inicio_banner),
                                contentDescription = "Perro y gato de HuellitasVet",
                                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(
                                Color(0xFF103A35).copy(alpha = .97f),
                                Color(0xFF103A35).copy(alpha = .83f),
                                Color(0xFF103A35).copy(alpha = .06f)
                            ))))
                            Column(
                                Modifier.align(Alignment.CenterStart).padding(start = 18.dp, end = 110.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(color = Color.White.copy(alpha = .16f), shape = RoundedCornerShape(50.dp)) {
                                    Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Filled.Pets, null, tint = Color(0xFFFFC38F),
                                            modifier = Modifier.size(14.dp))
                                        Text("CUIDADO CON AMOR", color = Color.White,
                                            fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = .8.sp)
                                    }
                                }
                                Text("Su salud,\nnuestra prioridad", fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 24.sp)
                                Text("Un equipo listo para cuidar de ellos.", fontSize = 12.sp,
                                    color = Color.White.copy(alpha = .87f), maxLines = 2)
                                Button(
                                    onClick = onPedirCita,
                                    contentPadding = PaddingValues(horizontal = 13.dp, vertical = 0.dp),
                                    shape = RoundedCornerShape(13.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFF145A4E)
                                    )
                                ) {
                                    Text("Reservar cita", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.width(5.dp))
                                    Icon(Icons.Filled.ArrowForward, null, modifier = Modifier.size(15.dp))
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    TarjetaCitaFiel(cita)
                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("TU FAMILIA", fontSize = 10.sp, letterSpacing = 1.4.sp,
                                fontWeight = FontWeight.Bold, color = Verde)
                            Text("Mis mascotas", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF173A36))
                        }
                        TextButton(onClick = onVerMascotas, contentPadding = PaddingValues(0.dp)) {
                            Text("Ver todas  →", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Verde)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        val visibles = mascotas.take(2)
                        visibles.forEachIndexed { index, mascota ->
                            AnimatedVisibility(
                                visible = true,
                                modifier = Modifier.weight(1f),
                                enter = fadeIn(tween(350, delayMillis = 80 * index)) +
                                    slideInVertically(
                                        animationSpec = tween(350, delayMillis = 80 * index),
                                        initialOffsetY = { it / 5 }
                                    )
                            ) {
                                TarjetaMascotaFiel(mascota, Modifier.fillMaxWidth()) {
                                    onMascota(mascota.id)
                                }
                            }
                        }
                        repeat((2 - visibles.size).coerceAtLeast(0)) { Spacer(Modifier.weight(1f)) }
                        Surface(
                            onClick = onAgregarMascota,
                            modifier = Modifier.weight(1f).height(132.dp),
                            color = Color(0xFFDDF1E9), shape = RoundedCornerShape(21.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                Surface(shape = RoundedCornerShape(15.dp), color = Verde) {
                                    Icon(Icons.Filled.Add, "Agregar mascota", tint = Color.White,
                                        modifier = Modifier.padding(8.dp).size(20.dp))
                                }
                                Spacer(Modifier.height(9.dp))
                                Text("Añadir", color = Color(0xFF145A4E), fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(Modifier.height(25.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("PARA SU BIENESTAR", fontSize = 10.sp, letterSpacing = 1.4.sp,
                            fontWeight = FontWeight.Bold, color = Verde)
                        Text("Servicios", fontSize = 19.sp, color = Color(0xFF173A36),
                            fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        ServicioFiel("Consulta", Icons.Filled.Favorite, Color(0xFF0B806D),
                            Color(0xFFDDF4EC), Modifier.weight(1f), onPedirCita)
                        ServicioFiel("Vacunas", Icons.Filled.Vaccines, Color(0xFF5B61C9),
                            Color(0xFFE9E8FF), Modifier.weight(1f), onPedirCita)
                        ServicioFiel("Baño", Icons.Filled.ContentCut, Color(0xFFDA7954),
                            Color(0xFFFFE8DE), Modifier.weight(1f), onPedirCita)
                    }

                    Spacer(Modifier.height(22.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFFE9DD), shape = RoundedCornerShape(19.dp)
                    ) {
                        Row(Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(13.dp), color = Color(0xFFFFD2BC)) {
                                Icon(Icons.Filled.Notifications, null, tint = Color(0xFFB65E3D),
                                    modifier = Modifier.padding(9.dp).size(19.dp))
                            }
                            Spacer(Modifier.width(11.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("Un pequeño recordatorio", fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold, color = Color(0xFF713D2A))
                                Text("Mantén al día sus controles y vacunas.", fontSize = 11.sp,
                                    color = Color(0xFF875D4D), maxLines = 2)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun TarjetaCitaFiel(detalle: CitaConDetalle?) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(126.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .background(Brush.linearGradient(listOf(Color(0xFF126F60), Color(0xFF0A5148))))
                .padding(horizontal = 17.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("AGENDA", color = Color(0xFFB9E7D8), fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold, letterSpacing = 1.3.sp)
                Spacer(Modifier.height(4.dp))
                if (detalle == null) {
                    Text("Un día para consentirle", color = Color.White,
                        fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    Spacer(Modifier.height(3.dp))
                    Text("Agenda su próxima visita en segundos.", color = Color.White.copy(alpha = .84f),
                        fontSize = 11.sp, maxLines = 2)
                } else {
                    val c = detalle.cita
                    Text("${detalle.mascota.mascota.nombre} · ${detalle.servicio.nombre}",
                        fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 15.sp,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(6.dp))
                    val fecha = runCatching {
                        LocalDate.parse(c.fecha).format(
                            DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE"))
                        ).replaceFirstChar { it.titlecase(Locale("es", "PE")) }
                    }.getOrDefault(c.fecha)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, null, tint = Color(0xFFFFC38F),
                            modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("$fecha · ${c.hora}", fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold, color = Color.White)
                    }
                    Spacer(Modifier.height(6.dp))
                    Surface(color = Color.White.copy(alpha = .17f), shape = RoundedCornerShape(50.dp)) {
                        Text(if (c.estado == "CONFIRMADA") "Confirmada" else "Pendiente",
                            color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                    }
                }
            }
            Box(
                modifier = Modifier.size(54.dp).clip(RoundedCornerShape(17.dp))
                    .background(Color.White.copy(alpha = .16f)),
                contentAlignment = Alignment.Center
            ) {
                if (detalle == null) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null,
                        tint = Color(0xFFFFC38F), modifier = Modifier.size(29.dp))
                } else {
                    MascotaAvatar(
                        especie = especieDe(detalle.mascota.mascota.especie),
                        tamano = 46.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaMascotaFiel(mascota: Mascota, modifier: Modifier, onClick: () -> Unit) {
    val tonoMascota = when (mascota.especie.uppercase()) {
        "PERRO" -> Color(0xFFFFF0E5)
        "GATO" -> Color(0xFFEAF0FF)
        else -> Color(0xFFE9F4ED)
    }
    Surface(
        onClick = onClick,
        modifier = modifier.height(132.dp),
        color = tonoMascota,
        shape = RoundedCornerShape(21.dp),
        border = BorderStroke(1.dp, tonoMascota.copy(alpha = .9f)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MascotaAvatar(especieDe(mascota.especie), tamano = 58.dp)
            Spacer(Modifier.height(6.dp))
            Text(mascota.nombre, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF243746),
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Ver perfil  ›", fontSize = 10.sp, color = Color(0xFF607282),
                fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ServicioFiel(
    titulo: String,
    icono: ImageVector,
    colorIcono: Color,
    colorFondo: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick, modifier = modifier.height(91.dp),
        color = colorFondo, shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, colorFondo.copy(alpha = .9f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Surface(shape = RoundedCornerShape(13.dp), color = Color.White.copy(alpha = .75f)) {
                Icon(icono, contentDescription = titulo, tint = colorIcono,
                    modifier = Modifier.padding(8.dp).size(21.dp))
            }
            Spacer(Modifier.height(7.dp))
            Text(titulo, fontSize = 11.sp, color = Color(0xFF283B49),
                fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

/** Barra compartida con indicador compacto para mantener despejada la navegación. */
@Composable
fun BarraNavegacionDueno(seleccionado: String, onSeleccionar: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                .height(66.dp).padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val opciones = listOf(
                Triple(Rutas.INICIO, "Inicio", Icons.Filled.Home),
                Triple(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
                Triple(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
                Triple(Rutas.PERFIL, "Perfil", Icons.Filled.PersonOutline)
            )
            opciones.forEach { (ruta, etiqueta, icono) ->
                val activo = seleccionado == ruta
                val colorActivo by animateColorAsState(
                    targetValue = if (activo) MaterialTheme.colorScheme.primary else Color(0xFF87949D),
                    animationSpec = tween(180), label = "color_tab_$ruta"
                )
                val fondoActivo by animateColorAsState(
                    targetValue = if (activo) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    animationSpec = tween(180), label = "fondo_tab_$ruta"
                )
                val tamanoIcono by animateDpAsState(
                    targetValue = if (activo) 23.dp else 20.dp,
                    animationSpec = tween(180), label = "icono_tab_$ruta"
                )
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .clickable { onSeleccionar(ruta) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(width = 38.dp, height = 30.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(fondoActivo),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icono, contentDescription = etiqueta, modifier = Modifier.size(tamanoIcono),
                            tint = colorActivo)
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(etiqueta, fontSize = 11.sp,
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
                        color = colorActivo,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// También se utiliza en MascotasScreen.kt; debe estar definido solo aquí.
internal fun especieDe(valor: String): Especie =
    runCatching { Especie.valueOf(valor) }.getOrDefault(Especie.OTRO)
