package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.tecsup.huellitasvet.ui.navigation.Rutas
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate
import java.util.Locale

private val VerdeFicha = Color(0xFF059669)
private val FondoFicha = Color(0xFFF6FDF9)
private val OscuroFicha = Color(0xFF1F2937)
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
        containerColor = FondoFicha,
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
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeFicha),
                        contentPadding = PaddingValues(horizontal = 9.dp)
                    ) {
                        Icon(Icons.Filled.EventAvailable, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Pedir cita", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = onEditar,
                        modifier = Modifier.weight(1f).height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, VerdeFicha),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeFicha),
                        contentPadding = PaddingValues(horizontal = 9.dp)
                    ) {
                        Icon(Icons.Filled.Edit, null, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Editar", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Selector Datos / Historial clínico sin indicador Material3
                Row(
                    modifier = Modifier.fillMaxWidth().height(39.dp)
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
                                Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                    color = if (tab == index) VerdeFicha else GrisFicha)
                            }
                        }
                    }
                }

                if (tab == 0) {
                    DatosFicha(actual)
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
                } else {
                    if (historial.isEmpty()) {
                        Surface(color = Color.White, shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, BordeFicha), modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.Pets, null, tint = VerdeFicha,
                                    modifier = Modifier.size(30.dp))
                                Text("Aún no tiene atenciones", color = GrisFicha, fontSize = 13.sp)
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
    val especieTexto = when (m.especie) { "GATO" -> "Gato"; "PERRO" -> "Perro"; else -> "Mascota" }
    val sexoTexto = if (m.sexo == "HEMBRA") "Hembra" else "Macho"
    val vacunas = historial.any { it.servicio.nombre.contains("vacun", ignoreCase = true) }

    Surface(
        color = Color.White, shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, BordeFicha), modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MascotaAvatar(
                especie = runCatching { Especie.valueOf(m.especie) }.getOrDefault(Especie.OTRO),
                tamano = 69.dp
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(m.nombre, color = OscuroFicha, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text("$especieTexto ${m.raza.lowercase()} · $edad años",
                    color = GrisFicha, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("$sexoTexto · ${"%.1f".format(Locale.US, m.peso)} kg", color = GrisFicha, fontSize = 12.sp)
                Surface(color = Color(0xFFDFF8E9), shape = RoundedCornerShape(4.dp)) {
                    Text(if (vacunas) "Vacunas registradas" else "Ficha registrada",
                        color = VerdeFicha, fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }
        }
    }
}

@Composable
private fun DatosFicha(m: Mascota) {
    val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
    Surface(color = Color.White, shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BordeFicha), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DatoFila("Nombre", m.nombre)
            DatoFila("Especie", m.especie.lowercase().replaceFirstChar { it.uppercase() })
            DatoFila("Raza", m.raza.ifBlank { "Sin especificar" })
            DatoFila("Edad", "$edad ${if (edad == 1) "año" else "años"}")
            DatoFila("Sexo", m.sexo.lowercase().replaceFirstChar { it.uppercase() })
            DatoFila("Peso actual", "${"%.1f".format(Locale.US, m.peso)} kg")
        }
    }
}

@Composable
private fun DatoFila(nombre: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(nombre, fontSize = 13.sp, color = GrisFicha)
        Text(valor, fontSize = 13.sp, color = OscuroFicha, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun LineaHistorial(detalle: CitaConDetalle, ultima: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // El diseño del timeline se completa con un punto verde y una línea vertical.
            Box(modifier = Modifier.size(17.dp).background(VerdeFicha, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center) {
                Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            if (!ultima) {
                Box(modifier = Modifier.width(2.dp).height(86.dp).background(Color(0xFFCCF2DE)))
            }
        }
        Surface(
            color = Color.White,
            modifier = Modifier.weight(1f).padding(bottom = 8.dp),
            border = BorderStroke(1.dp, BordeFicha),
            shape = RoundedCornerShape(13.dp)
        ) {
            Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                // Componente del proyecto base entregado [E]: HistorialItem.
                // Mantiene diagnóstico y peso observables desde Room.
                HistorialItem(
                    fecha = detalle.cita.fecha,
                    servicio = detalle.servicio.nombre,
                    diagnostico = detalle.cita.diagnostico.orEmpty(),
                    peso = detalle.cita.pesoRegistrado
                )
            }
        }
    }
}
