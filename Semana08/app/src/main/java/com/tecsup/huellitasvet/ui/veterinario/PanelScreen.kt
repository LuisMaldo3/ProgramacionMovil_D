package com.tecsup.huellitasvet.ui.veterinario

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

private val Azul = Color(0xFF1D4ED8)
private val FondoVet = Color(0xFFF7F9FF)
private val TextoVet = Color(0xFF1F2937)
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
    val hoy = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE"))
    ).replaceFirstChar { it.uppercase() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoVet,
        bottomBar = {
            BarraNavegacionVeterinario(Rutas.PANEL, onSeleccionarTab)
        }
    ) { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Panel de la clínica", fontWeight = FontWeight.Bold,
                        color = TextoVet, fontSize = 22.sp)
                    Text("Hoy · $hoy", fontSize = 12.sp, color = GrisVet)
                }
                IconButton(onClick = onVerAgenda) {
                    Icon(Icons.Filled.Notifications, "Ver agenda", tint = TextoVet)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                ContadorVet("Citas hoy", citasHoy, Color(0xFFDCEAFE), Azul, Modifier.weight(1f))
                ContadorVet("Pendientes", pendientes, Color(0xFFFFF2C6), Color(0xFFD97706), Modifier.weight(1f))
                ContadorVet("Atendidas", atendidas, Color(0xFFE2E8F0), Color(0xFF475569), Modifier.weight(1f))
            }

            Surface(
                color = Azul, shape = RoundedCornerShape(17.dp),
                modifier = Modifier.fillMaxWidth().height(70.dp)
            ) {
                Row(
                    Modifier.padding(horizontal = 17.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Ingresos del mes", color = Color.White.copy(alpha = .75f), fontSize = 12.sp)
                        Text(formato, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }
                    Text("$atendidas atenciones", color = Color.White, fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 5.dp))
                }
            }

            Text("Citas por estado", color = TextoVet, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Surface(
                color = Color.White, shape = RoundedCornerShape(15.dp),
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
                                fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextoVet)
                            Text("citas", fontSize = 10.sp, color = GrisVet)
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
                                Text(item.etiqueta, fontSize = 11.sp, modifier = Modifier.weight(1f),
                                    color = TextoVet, maxLines = 1)
                                Text(item.valor.toInt().toString(), fontSize = 11.sp,
                                    color = TextoVet, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Text("Citas por servicio", color = TextoVet, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Surface(
                color = Color.White, shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, BordeVet), modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (porServicio.isEmpty()) {
                        Text("Todavía no hay citas", color = GrisVet, fontSize = 12.sp)
                    } else {
                        // Se reutiliza el Canvas que trae el proyecto base.
                        GraficoBarras(porServicio, Modifier.height(95.dp))
                        Spacer(Modifier.height(5.dp))
                        Row(Modifier.fillMaxWidth()) {
                            porServicio.forEach { servicio ->
                                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(servicio.valor.toInt().toString(), fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold, color = Azul)
                                    Text(servicio.etiqueta.substringBefore(' '), fontSize = 10.sp,
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
                            Text("$porConfirmar citas por confirmar", fontSize = 12.sp,
                                fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                            Text("para hoy y mañana", fontSize = 11.sp, color = Color(0xFF92400E))
                        }
                        Button(
                            onClick = onVerAgenda,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            contentPadding = PaddingValues(horizontal = 18.dp),
                            modifier = Modifier.height(35.dp)
                        ) { Text("Ver", fontSize = 12.sp) }
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
    titulo: String, valor: Int, fondo: Color, textoColor: Color, modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.height(75.dp), color = fondo, shape = RoundedCornerShape(16.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor.toString(), color = textoColor, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Text(titulo, color = textoColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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
    Surface(color = Color.White, tonalElevation = 0.dp, shadowElevation = 2.dp) {
        Row(
            Modifier.fillMaxWidth().navigationBarsPadding().height(63.dp)
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            pestañas.forEach { (ruta, nombre, icono) ->
                val activo = ruta == seleccionada
                Column(
                    Modifier.weight(1f).fillMaxHeight().clickable { onSeleccionar(ruta) },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icono, nombre, tint = if (activo) Azul else Color(0xFF98A2B3),
                        modifier = Modifier.size(23.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(nombre, fontSize = 10.sp, color = if (activo) Azul else Color(0xFF98A2B3),
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
    }
}
