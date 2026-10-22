package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.DialogoExitoAnimado
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.Year
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Azul = Color(0xFF2458DE)
private val Fondo = Color(0xFFF3F6FC)
private val Texto = Color(0xFF172B3A)
private val Gris = Color(0xFF8792A4)

@Composable
fun AtenderCitaScreen(
    vm: AtenderCitaViewModel,
    onVolver: () -> Unit,
    onFinalizada: () -> Unit
) {
    val detalle by vm.cita.collectAsState()
    val errorDiagnostico by vm.errorDiagnostico.collectAsState()
    val errorPeso by vm.errorPeso.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val finalizada by vm.finalizada.collectAsState()

    var peso by rememberSaveable { mutableStateOf("") }
    var diagnostico by rememberSaveable { mutableStateOf("") }
    var tratamiento by rememberSaveable { mutableStateOf("") }
    var pedirCancelar by remember { mutableStateOf(false) }
    var fueCancelacion by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    LaunchedEffect(finalizada) { if (finalizada) mostrarResultado = true }
    if (mostrarResultado) {
        DialogoExitoAnimado(
            titulo = if (fueCancelacion) "Cita cancelada" else "¡Atención registrada!",
            descripcion = if (fueCancelacion) "El estado de la cita se actualizó en la agenda."
                else "Diagnóstico, tratamiento y peso registrados correctamente en el historial clínico.",
            boton = "Volver a la agenda",
            onContinuar = { mostrarResultado = false; onFinalizada() }
        )
    }

    // Separación adaptable sin cortar formularios en pantallas pequeñas.
    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 685) / 11).coerceIn(13, 27).dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Texto)
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Atender cita", color = Texto, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(48.dp))
            }
        }
    ) { insets ->
        val actual = detalle
        if (actual == null) {
            Box(Modifier.fillMaxSize().padding(insets), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Azul)
            }
        } else {
            val mascota = actual.mascota.mascota
            val duenio = actual.mascota.dueno
            val disponible = actual.cita.estado in setOf("PENDIENTE", "CONFIRMADA")
            val especie = runCatching { Especie.valueOf(mascota.especie) }.getOrDefault(Especie.OTRO)
            val edad = (Year.now().value - mascota.anioNacimiento).coerceAtLeast(0)
            val sexo = if (mascota.sexo == "HEMBRA") "Hembra" else "Macho"
            val pesoNuevo = peso.replace(',', '.').toDoubleOrNull()
            val nombreCorto = duenio.nombre.trim().split(" ").let { partes ->
                partes.firstOrNull().orEmpty() +
                        (partes.getOrNull(1)?.firstOrNull()?.let { " $it." } ?: "")
            }

            // El laboratorio solo registra dos dueños de prueba. No se infiere género por el nombre.
            val etiquetaDueno = when (duenio.correo.trim().lowercase(Locale.ROOT)) {
                "ana@correo.test" -> "Dueña"
                "luis@correo.test" -> "Dueño"
                else -> "Dueño" // Etiqueta genérica hasta que Usuario tenga una preferencia explícita
            }

            // Fecha ISO de Room -> día de semana legible, por ejemplo: "Mié 21 oct".
            val fechaLegible = remember(actual.cita.fecha) {
                runCatching {
                    LocalDate.parse(actual.cita.fecha)
                        .format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE")))
                        .replaceFirstChar { it.uppercaseChar() }
                }.getOrDefault(actual.cita.fecha)
            }

            Column(
                Modifier.fillMaxSize().padding(insets).imePadding().verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(gapPantalla)
            ) {
                Surface(
                    shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shadowElevation = 3.dp
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MascotaAvatar(especie, tamano = 68.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("${mascota.nombre} · ${mascota.raza}", fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp)
                            Text("$edad años · $sexo · $etiquetaDueno: $nombreCorto",
                                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Vaccines, null, tint = Azul, modifier = Modifier.size(17.dp))
                                Spacer(Modifier.width(5.dp))
                                Text("${actual.servicio.nombre} · $fechaLegible · ${actual.cita.hora}",
                                    color = Azul, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                Text("Registro clínico", fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = peso, onValueChange = { peso = it },
                        label = { Text("Peso (kg)") }, singleLine = true,
                        isError = errorPeso != null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        modifier = Modifier.weight(1f).height(65.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .48f), shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFD8E7FF))
                    ) {
                        Column(Modifier.padding(11.dp), verticalArrangement = Arrangement.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Favorite, null, tint = Color(0xFFEF476F),
                                    modifier = Modifier.size(17.dp))
                                Spacer(Modifier.width(5.dp))
                                Text("Peso anterior", fontSize = 13.sp, color = Gris)
                            }
                            Text("${"%.1f".format(Locale.US, mascota.peso)} kg", fontSize = 17.sp,
                                fontWeight = FontWeight.Bold, color = Texto)
                        }
                    }
                }
                if (errorPeso != null) {
                    Text(errorPeso.orEmpty(), fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                }

                OutlinedTextField(
                    value = diagnostico, onValueChange = { diagnostico = it },
                    label = { Text("Diagnóstico") },
                    minLines = 3, maxLines = 5,
                    isError = errorDiagnostico != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
                if (errorDiagnostico != null) {
                    Text(errorDiagnostico.orEmpty(), fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                }

                OutlinedTextField(
                    value = tratamiento, onValueChange = { tratamiento = it },
                    label = { Text("Tratamiento") },
                    minLines = 3, maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Surface(color = Color(0xFFEEF5FF), shape = RoundedCornerShape(14.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Check, null, tint = Azul, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            if (pesoNuevo != null && pesoNuevo > 0.0)
                                "Se actualizará el peso de ${mascota.nombre} a ${"%.1f".format(Locale.US, pesoNuevo)} kg"
                            else "Al registrar se actualizará el peso de ${mascota.nombre}",
                            fontSize = 13.sp, color = Gris
                        )
                    }
                }

                if (!disponible) {
                    Text("La cita ya está ${actual.cita.estado.lowercase()}.", color = Gris, fontSize = 15.sp)
                }

                Button(
                    onClick = { fueCancelacion = false; vm.registrar(diagnostico, tratamiento, peso) },
                    enabled = disponible && !procesando,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text("Registrar atención", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { pedirCancelar = true },
                    enabled = disponible && !procesando,
                    modifier = Modifier.fillMaxWidth().height(51.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFDC2626)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text("Cancelar cita", fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626), fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }

    if (pedirCancelar) {
        AlertDialog(
            onDismissRequest = { pedirCancelar = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar esta cita?") },
            confirmButton = {
                TextButton(onClick = { pedirCancelar = false; fueCancelacion = true; vm.cancelar() }) {
                    Text("Sí, cancelar", color = Color(0xFFDC2626))
                }
            },
            dismissButton = { TextButton(onClick = { pedirCancelar = false }) { Text("Volver") } }
        )
    }
    if (!mensaje.isNullOrBlank()) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("HuellitasVet") },
            text = { Text(mensaje.orEmpty()) },
            confirmButton = {
                TextButton(onClick = vm::limpiarMensaje) { Text("Entendido") }
            }
        )
    }
}
