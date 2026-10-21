package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    LaunchedEffect(finalizada) {
        if (finalizada) onFinalizada()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = "Atender consulta",
                onVolver = onVolver
            )
        }
    ) { insets ->
        val actual = detalle
        if (actual == null) {
            Box(Modifier.fillMaxSize().padding(insets), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val mascota = actual.mascota.mascota
            val duenio = actual.mascota.dueno
            val disponible = actual.cita.estado in setOf("PENDIENTE", "CONFIRMADA")
            val especie = runCatching { Especie.valueOf(mascota.especie.uppercase()) }.getOrDefault(Especie.OTRO)
            val edad = (Year.now().value - mascota.anioNacimiento).coerceAtLeast(0)
            val sexo = if (mascota.sexo.uppercase() == "HEMBRA") "Hembra" else "Macho"
            val pesoNuevo = peso.replace(',', '.').toDoubleOrNull()

            val fechaLegible = remember(actual.cita.fecha) {
                runCatching {
                    LocalDate.parse(actual.cita.fecha)
                        .format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "PE")))
                        .replaceFirstChar { it.uppercaseChar() }
                }.getOrDefault(actual.cita.fecha)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(insets)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // PET & APPOINTMENT INFO CARD
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MascotaAvatar(especie = especie, tamano = 68.dp)

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${mascota.nombre} · ${mascota.raza}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$edad años · $sexo · Dueño: ${duenio.nombre}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Vaccines,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "${actual.servicio.nombre} · $fechaLegible · ${actual.cita.hora}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // CLINICAL RECORD FORM CARD
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Registro clínico",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                VetTextField(
                                    valor = peso,
                                    onValorCambio = { peso = it },
                                    etiqueta = "Peso nuevo (kg)",
                                    error = errorPeso,
                                    tipoTeclado = KeyboardType.Decimal
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = "Peso anterior",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = "${"%.1f".format(Locale.US, mascota.peso)} kg",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = diagnostico,
                            onValueChange = { diagnostico = it },
                            label = { Text("Diagnóstico médico") },
                            minLines = 3,
                            maxLines = 5,
                            isError = errorDiagnostico != null,
                            supportingText = if (errorDiagnostico != null) {
                                { Text(errorDiagnostico.orEmpty()) }
                            } else null,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        OutlinedTextField(
                            value = tratamiento,
                            onValueChange = { tratamiento = it },
                            label = { Text("Tratamiento o receta") },
                            minLines = 3,
                            maxLines = 5,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = if (pesoNuevo != null && pesoNuevo > 0.0)
                                        "Se actualizará el peso de ${mascota.nombre} a ${"%.1f".format(Locale.US, pesoNuevo)} kg"
                                    else "Al registrar se actualizará el peso de ${mascota.nombre}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        VetButton(
                            texto = if (procesando) "Registrando..." else "Registrar atención",
                            onClick = { vm.registrar(diagnostico, tratamiento, peso) },
                            habilitado = disponible && !procesando
                        )

                        OutlinedButton(
                            onClick = { pedirCancelar = true },
                            enabled = disponible && !procesando,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(
                                text = "Cancelar cita",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    if (pedirCancelar) {
        AlertDialog(
            onDismissRequest = { pedirCancelar = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar esta cita?") },
            confirmButton = {
                TextButton(onClick = { pedirCancelar = false; vm.cancelar() }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pedirCancelar = false }) { Text("Volver") }
            }
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
