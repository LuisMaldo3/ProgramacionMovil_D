package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Info
import com.tecsup.huellitasvet.ui.components.TituloSeccionPremium
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.graficos.GraficoLineas
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate
import java.util.Locale

private val VerdeFicha = Color(0xFF087F6D)
private val FondoFicha = Color(0xFFF4F8F6)
private val OscuroFicha = Color(0xFF172B3A)
private val GrisFicha = Color(0xFF87929E)
private val BordeFicha = Color(0xFFE1E9E4)

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
    val historialPesoFlow = remember(vm, mascotaId) { vm.historialPeso(mascotaId) }
    val historialPeso by historialPesoFlow.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val procesando by vm.procesando.collectAsState()
    var tab by rememberSaveable { mutableIntStateOf(1) } // Mockup: Historial clínico seleccionado
    var confirmarEliminacion by remember { mutableStateOf(false) }

    LaunchedEffect(vm) { vm.eliminado.collect { onEliminada() } }

    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            title = { Text("Eliminar mascota") },
            text = { Text("Solo se podrá eliminar si no tiene citas registradas. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminacion = false
                    mascota?.let { vm.eliminar(it) }
                }) { Text("Eliminar", color = Color(0xFFDC2626)) }
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

    // Separación adaptable sin cortar formularios en pantallas pequeñas.
    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 640) / 11).coerceIn(12, 25).dp

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraSuperiorMascota(mascota?.nombre ?: "Mascota", onVolver) },
        bottomBar = {
            BarraNavegacionDueno(seleccionado = Rutas.MASCOTAS, onSeleccionar = onSeleccionarTab)
        }
    ) { padding ->
        val actual = mascota
        if (actual == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = VerdeFicha)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 13.dp),
                verticalArrangement = Arrangement.spacedBy(gapPantalla)
            ) {
                CabeceraFicha(actual, historial)

                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Button(
                        onClick = onPedirCita,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeFicha),
                        contentPadding = PaddingValues(horizontal = 9.dp)
                    ) {
                        Icon(Icons.Filled.EventAvailable, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Pedir cita", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = onEditar,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, VerdeFicha),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeFicha),
                        contentPadding = PaddingValues(horizontal = 9.dp)
                    ) {
                        Icon(Icons.Filled.Edit, null, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Editar", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Selector Datos / Historial clínico sin indicador Material3
                Row(
                    modifier = Modifier.fillMaxWidth().height(51.dp)
                        .background(Color(0xFFE6F2ED), RoundedCornerShape(12.dp))
                        .padding(3.dp)
                ) {
                    listOf("Datos", "Historial clínico").forEachIndexed { index, titulo ->
                        Surface(
                            onClick = { tab = index },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            shape = RoundedCornerShape(10.dp),
                            color = if (tab == index) Color.White else Color.Transparent
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(titulo, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                    color = if (tab == index) VerdeFicha else GrisFicha)
                            }
                        }
                    }
                }

                if (tab == 0) {
                    TituloSeccionPremium("Información de ${actual.nombre}", "Datos de salud actualizados")
                    DatosFicha(actual)
                    Spacer(Modifier.height(10.dp))
                    GraficoLineas(puntos = historialPeso)
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { confirmarEliminacion = true }, enabled = !procesando,
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color(0xFFDC2626)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                    ) {
                        Icon(Icons.Default.DeleteOutline, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Eliminar mascota")
                    }
                }else {
                    TituloSeccionPremium("Historial clínico", "${historial.size} atenciones registradas")
                    if (historial.isEmpty()) {
                        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BordeFicha), modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.Pets, null, tint = VerdeFicha,
                                    modifier = Modifier.size(30.dp))
                                Text("Aún no tiene atenciones", color = GrisFicha, fontSize = 14.sp)
                            }
                        }
                    } else {
                        // El DAO devuelve solo ATENDIDAS, ordenadas de la más reciente a la antigua.
                        historial.forEachIndexed { index, cita ->
                            LineaHistorial(cita, ultima = index == historial.lastIndex)
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CabeceraFicha(m: Mascota, historial: List<CitaConDetalle>) {
    val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
    val especieTexto = when (m.especie) { "GATO" -> "Gato"; "PERRO" -> "Perro"; else -> "Otra especie" }
    val vacunas = historial.any { it.servicio.nombre.contains("vacun", ignoreCase = true) }
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(25.dp), shadowElevation = 5.dp) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().background(
                Brush.horizontalGradient(listOf(Color(0xFF086D5E), Color(0xFF18B69B))))
                .padding(horizontal = 18.dp, vertical = 23.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(17.dp)) {
                MascotaAvatar(
                    especie = runCatching { Especie.valueOf(m.especie) }.getOrDefault(Especie.OTRO),
                    tamano = 92.dp
                )
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("EXPEDIENTE VETERINARIO", color = Color.White.copy(alpha=.85f),
                        fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
                    Text(m.nombre, color = Color.White, fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold)
                    Text("$especieTexto · ${m.raza.ifBlank { "Sin raza especificada" }}",
                        color = Color.White.copy(alpha=.93f), fontSize = 14.sp)
                    Text("$edad ${if (edad == 1) "año" else "años"}",
                        color = Color.White.copy(alpha=.93f), fontSize = 14.sp)
                }
            }
            Row(Modifier.fillMaxWidth().padding(horizontal = 17.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = RoundedCornerShape(13.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        Icon(Icons.Filled.MonitorWeight, null, tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(19.dp))
                        Text("${"%.1f".format(Locale.US, m.peso)} kg", fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                    }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(if (vacunas) "Vacunas registradas" else "Ficha en seguimiento",
                        fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${historial.size} atenciones clínicas", fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun DatosFicha(m: Mascota) {
    val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
            DatoFila("Nombre", m.nombre)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DatoFila("Especie", m.especie.lowercase().replaceFirstChar { it.uppercase() })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DatoFila("Raza", m.raza.ifBlank { "Sin especificar" })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DatoFila("Edad", "$edad ${if (edad == 1) "año" else "años"}")
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DatoFila("Sexo", m.sexo.lowercase().replaceFirstChar { it.uppercase() })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            DatoFila("Peso actual", "${"%.1f".format(Locale.US, m.peso)} kg")
        }
    }
}

@Composable
private fun DatoFila(nombre: String, valor: String) {
    Row(Modifier.fillMaxWidth().heightIn(min = 55.dp).padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(nombre, modifier = Modifier.weight(.42f), fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, modifier = Modifier.weight(.58f), fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun LineaHistorial(detalle: CitaConDetalle, ultima: Boolean) {
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(shape = RoundedCornerShape(50.dp), color = VerdeFicha,
                modifier = Modifier.size(26.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.MedicalServices, contentDescription = null, tint = Color.White,
                        modifier = Modifier.size(15.dp))
                }
            }
            if (!ultima) Box(Modifier.width(3.dp).weight(1f)
                .background(MaterialTheme.colorScheme.primary.copy(alpha=.25f)))
        }
        ElevatedCard(modifier = Modifier.weight(1f).padding(bottom = 13.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            HistorialItem(fecha = detalle.cita.fecha,
                servicio = detalle.servicio.nombre,
                diagnostico = detalle.cita.diagnostico.orEmpty(),
                peso = detalle.cita.pesoRegistrado,
                tratamiento = detalle.cita.tratamiento)
        }
    }
}
