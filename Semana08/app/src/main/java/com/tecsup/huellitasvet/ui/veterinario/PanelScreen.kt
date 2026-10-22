package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.DatoGrafico
import com.tecsup.huellitasvet.ui.components.graficos.GraficoBarras
import com.tecsup.huellitasvet.ui.components.graficos.GraficoDona
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Azul = Color(0xFF2458DE)
private val FondoVet = Color(0xFFF3F6FC)
private val TextoVet = Color(0xFF172B3A)
private val GrisVet = Color(0xFF8792A4)
private val AmarilloVet = Color(0xFFFFF2C6)
private val BordeVet = Color(0xFFE2E8F0)

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerAgenda: () -> Unit,
    onSeleccionarTab: (String) -> Unit
) {
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresosMes by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val porConfirmar by vm.porConfirmarHoyManana.collectAsState()
    val formato = rememberFormatoMoneda(ingresosMes)
    val altoDp = LocalConfiguration.current.screenHeightDp
    val espacio = ((altoDp - 660) / 12).coerceIn(13, 19).dp
    val hoy = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE"))
    ).replaceFirstChar { it.uppercase() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraNavegacionVeterinario(Rutas.PANEL, onSeleccionarTab)
        }
    ) { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(espacio)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Surface(color = Color(0xFFDDE8FF), shape = RoundedCornerShape(50.dp)) {
                        Text("  ✦  RESUMEN DE HOY  ", fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF284DA5), fontSize = 9.sp,
                            letterSpacing = 1.sp, modifier = Modifier.padding(vertical = 5.dp))
                    }
                    Spacer(Modifier.height(5.dp))
                    Text("Panel de la clínica", fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface, fontSize = 23.sp)
                    Text(hoy, fontSize = 13.sp, color = GrisVet)
                }
                Surface(onClick = onVerAgenda, shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF2458DE)) {
                    Box(Modifier.size(46.dp), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Notifications, "Ver agenda", tint = Color.White)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                ContadorVet("Citas hoy", citasHoy, Color(0xFFDCE8FF), Azul,
                    Icons.Filled.CalendarMonth, Modifier.weight(1f))
                ContadorVet("Pendientes", pendientes, Color(0xFFFFE7C2), Color(0xFFAD5D00),
                    Icons.Filled.WarningAmber, Modifier.weight(1f))
                ContadorVet("Atendidas", atendidas, Color(0xFFDDF3E9), Color(0xFF137451),
                    Icons.Filled.CheckCircle, Modifier.weight(1f))
            }

            Surface(
                color = Color.Transparent, shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth().height(88.dp), shadowElevation = 5.dp
            ) {
                Row(
                    Modifier.fillMaxSize()
                        .background(Brush.linearGradient(listOf(Color(0xFF173A9B), Color(0xFF3978E8))))
                        .padding(horizontal = 17.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("INGRESOS DEL MES", color = Color.White.copy(alpha = .78f),
                            fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.1.sp)
                        Text(formato, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    }
                    Surface(color = Color.White.copy(alpha = .15f), shape = RoundedCornerShape(14.dp)) {
                        Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(atendidas.toString(), color = Color.White,
                                fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text("atenciones", color = Color.White.copy(alpha = .85f), fontSize = 9.sp)
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth().height(132.dp),
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFF214EAE), shadowElevation = 4.dp
            ) {
                Box {
                    Image(painter = painterResource(R.drawable.veterinario_banner),
                        contentDescription = "Imagen de la clínica veterinaria",
                        contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(
                        Color(0xFF153A86).copy(alpha = .97f),
                        Color(0xFF153A86).copy(alpha = .75f), Color.Transparent))))
                    Column(Modifier.align(Alignment.CenterStart).padding(16.dp)) {
                        Text("CLÍNICA HUELLITASVET", fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold, letterSpacing = 1.2.sp,
                            color = Color(0xFFBFD3FF))
                        Spacer(Modifier.height(5.dp))
                        Text("Cada atención\ncuenta", fontSize = 20.sp,
                            lineHeight = 21.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Spacer(Modifier.height(4.dp))
                        Text("Tu equipo, tus pacientes, en un solo lugar.", fontSize = 11.sp,
                            color = Color.White.copy(alpha = .85f))
                    }
                }
            }

            Text("Citas por estado", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Surface(
                color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, BordeVet), modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(12.dp).height(102.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(17.dp)
                ) {
                    Box(Modifier.size(102.dp), contentAlignment = Alignment.Center) {
                        GraficoDona(porEstado, Modifier.size(99.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(porEstado.sumOf { it.valor.toInt() }.toString(),
                                fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurface)
                            Text("citas", fontSize = 13.sp, color = GrisVet)
                        }
                    }
                    Column(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val colores = listOf(
                            Color(0xFFD97706), Color(0xFF16A34A),
                            Color(0xFF475569), Color(0xFFDC2626)
                        )
                        porEstado.forEachIndexed { i, item ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(11.dp).background(colores[i % colores.size], RoundedCornerShape(2.dp)))
                                Spacer(Modifier.width(7.dp))
                                Text(item.etiqueta, fontSize = 13.sp, modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                                Text(item.valor.toInt().toString(), fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Text("Citas por servicio", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Surface(
                color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, BordeVet), modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (porServicio.isEmpty()) {
                        Text("Todavía no hay citas", color = GrisVet, fontSize = 13.sp)
                    } else {
                        // Se reutiliza el Canvas que trae el proyecto base.
                        GraficoBarras(porServicio, Modifier.height(95.dp))
                        Spacer(Modifier.height(5.dp))
                        Row(Modifier.fillMaxWidth()) {
                            porServicio.forEach { servicio ->
                                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(servicio.valor.toInt().toString(), fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold, color = Azul)
                                    Text(servicio.etiqueta.substringBefore(' '), fontSize = 13.sp,
                                        color = GrisVet, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }

            if (porConfirmar > 0) {
                Surface(
                    color = AmarilloVet, shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.WarningAmber, null, tint = Color(0xFFD97706))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("$porConfirmar citas por confirmar", fontSize = 13.sp,
                                fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                            Text("para hoy y mañana", fontSize = 13.sp, color = Color(0xFF92400E))
                        }
                        Button(
                            onClick = onVerAgenda,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            modifier = Modifier.height(35.dp)
                        ) { Text("Ver", fontSize = 13.sp) }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberFormatoMoneda(ingresos: Double): String {
    return "S/ " + NumberFormat.getNumberInstance(Locale("es", "PE"))
        .apply { minimumFractionDigits = 2; maximumFractionDigits = 2 }
        .format(ingresos)
}

@Composable
private fun ContadorVet(
    titulo: String,
    valor: Int,
    fondo: Color,
    textoColor: Color,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    val valorVisible by animateIntAsState(
        targetValue = valor, animationSpec = tween(700), label = "contador_$titulo"
    )
    Surface(
        modifier = modifier.height(88.dp), color = fondo,
        shape = RoundedCornerShape(19.dp), shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 7.dp, horizontal = 3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Icon(icono, null, tint = textoColor, modifier = Modifier.size(17.dp))
                Spacer(Modifier.width(5.dp))
                Text(valorVisible.toString(), color = textoColor,
                    fontWeight = FontWeight.ExtraBold, fontSize = 23.sp)
            }
            Spacer(Modifier.height(2.dp))
            Text(titulo, color = textoColor, fontSize = 10.sp,
                fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
fun BarraNavegacionVeterinario(
    seleccionada: String, onSeleccionar: (String) -> Unit
) {
    val pestañas = listOf(
        Triple(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
        Triple(Rutas.AGENDA, "Agenda", Icons.Filled.CalendarMonth),
        Triple(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
        Triple(Rutas.SERVICIOS, "Servicios", Icons.Filled.LocalOffer),
        Triple(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings)
    )
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            Modifier.fillMaxWidth().navigationBarsPadding().height(66.dp)
                .padding(horizontal = 4.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            pestañas.forEach { (ruta, nombre, icono) ->
                val activo = ruta == seleccionada
                val colorActivo by animateColorAsState(
                    targetValue = if (activo) MaterialTheme.colorScheme.primary else Color(0xFF8A96A6),
                    animationSpec = tween(180), label = "color_tab_$ruta"
                )
                val fondoActivo by animateColorAsState(
                    targetValue = if (activo) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    animationSpec = tween(180), label = "fondo_tab_$ruta"
                )
                val tamanoIcono by animateDpAsState(
                    targetValue = if (activo) 22.dp else 19.dp,
                    animationSpec = tween(180), label = "icono_tab_$ruta"
                )
                Column(
                    Modifier.weight(1f).fillMaxHeight().clickable { onSeleccionar(ruta) },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(width = 36.dp, height = 29.dp)
                            .background(
                                fondoActivo,
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icono, nombre, tint = colorActivo,
                            modifier = Modifier.size(tamanoIcono))
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(nombre, fontSize = 10.sp,
                        color = colorActivo,
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1)
                }
            }
        }
    }
}
