
package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.Year
import java.util.Locale

private val AzulAtencion = Color(0xFF1D4ED8)
private val FondoAtencion = Color(0xFFF7F9FF)
private val TextoAtencion = Color(0xFF1F2937)
private val GrisAtencion = Color(0xFF8792A4)

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
        containerColor = FondoAtencion,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextoAtencion
                    )
                }
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Atender cita",
                        color = TextoAtencion,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(48.dp))
            }
        }
    ) { insets ->

        val actual = detalle

        if (actual == null) {
            Box(
                Modifier.fillMaxSize().padding(insets),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulAtencion)
            }
        } else {

            val mascota = actual.mascota.mascota
            val duenio = actual.mascota.dueno

            val disponible = actual.cita.estado in setOf(
                "PENDIENTE", "CONFIRMADA"
            )

            val especie = runCatching {
                Especie.valueOf(mascota.especie)
            }.getOrDefault(Especie.OTRO)

            val edad = (Year.now().value -
                    mascota.anioNacimiento).coerceAtLeast(0)

            val sexo = if (mascota.sexo == "HEMBRA")
                "Hembra" else "Macho"

            val pesoNuevo = peso.replace(
                ',', '.'
            ).toDoubleOrNull()

            val nombreCorto = duenio.nombre
                .trim().split(" ").let { partes ->
                    partes.firstOrNull().orEmpty() +
                            (partes.getOrNull(1)
                                ?.firstOrNull()
                                ?.let { " $it." } ?: "")
                }

            Column(
                Modifier.fillMaxSize()
                    .padding(insets)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 22.dp,
                        vertical = 14.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(
                    15.dp
                )
            ) {

                Surface(
                    shape = RoundedCornerShape(17.dp),
                    color = Color.White,
                    border = BorderStroke(
                        1.dp,
                        Color(0xFFE2E8F0)
                    )
                ) {
                    Row(
                        Modifier.fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MascotaAvatar(
                            especie,
                            tamano = 68.dp
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                "${mascota.nombre} · ${mascota.raza}",
                                fontWeight = FontWeight.Bold,
                                color = TextoAtencion,
                                fontSize = 16.sp
                            )

                            Text(
                                "$edad años · $sexo · Dueña/o: $nombreCorto",
                                color = GrisAtencion,
                                fontSize = 12.sp
                            )

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Vaccines,
                                    null,
                                    tint = AzulAtencion,
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(
                                    "${actual.servicio.nombre} · " +
                                            "${actual.cita.fecha} · " +
                                            actual.cita.hora,
                                    color = AzulAtencion,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Text(
                    "Registro clínico",
                    fontSize = 17.sp,
                    color = TextoAtencion,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = peso,
                        onValueChange = { peso = it },
                        label = { Text("Peso (kg)") },
                        singleLine = true,
                        isError = errorPeso != null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        modifier = Modifier.weight(1f)
                            .height(65.dp),
                        color = Color(0xFFEEF5FF),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFD8E7FF)
                        )
                    ) {
                        Column(
                            Modifier.padding(11.dp),
                            verticalArrangement =
                                Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    null,
                                    tint = Color(0xFFEF476F),
                                    modifier = Modifier.size(17.dp)
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(
                                    "Peso anterior",
                                    fontSize = 12.sp,
                                    color = GrisAtencion
                                )
                            }

                            Text(
                                "${"%.1f".format(
                                    Locale.US,
                                    mascota.peso
                                )} kg",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoAtencion
                            )
                        }
                    }
                }

                if (errorPeso != null) {
                    Text(
                        errorPeso.orEmpty(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = diagnostico,
                    onValueChange = { diagnostico = it },
                    label = { Text("Diagnóstico") },
                    minLines = 3,
                    maxLines = 5,
                    isError = errorDiagnostico != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                if (errorDiagnostico != null) {
                    Text(
                        errorDiagnostico.orEmpty(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = tratamiento,
                    onValueChange = { tratamiento = it },
                    label = { Text("Tratamiento") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Surface(
                    color = Color(0xFFEEF5FF),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            null,
                            tint = AzulAtencion,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            if (pesoNuevo != null && pesoNuevo > 0.0)
                                "Se actualizará el peso de ${mascota.nombre} a " +
                                        "${"%.1f".format(Locale.US, pesoNuevo)} kg"
                            else
                                "Al registrar se actualizará el peso de ${mascota.nombre}",
                            fontSize = 12.sp,
                            color = GrisAtencion
                        )
                    }
                }

                if (!disponible) {
                    Text(
                        "La cita ya está ${actual.cita.estado.lowercase()}.",
                        color = GrisAtencion,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = {
                        vm.registrar(
                            diagnostico,
                            tratamiento,
                            peso
                        )
                    },
                    enabled = disponible && !procesando,
                    modifier = Modifier.fillMaxWidth()
                        .height(53.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulAtencion
                    ),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text(
                        "Registrar atención",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { pedirCancelar = true },
                    enabled = disponible && !procesando,
                    modifier = Modifier.fillMaxWidth()
                        .height(51.dp),
                    border = BorderStroke(
                        1.5.dp,
                        Color(0xFFDC2626)
                    ),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text(
                        "Cancelar cita",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626),
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }

    if (pedirCancelar) {
        AlertDialog(
            onDismissRequest = {
                pedirCancelar = false
            },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Seguro que deseas cancelar esta cita?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pedirCancelar = false
                        vm.cancelar()
                    }
                ) {
                    Text(
                        "Sí, cancelar",
                        color = Color(0xFFDC2626)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { pedirCancelar = false }
                ) {
                    Text("Volver")
                }
            }
        )
    }

    if (!mensaje.isNullOrBlank()) {
        AlertDialog(
            onDismissRequest = vm::limpiarMensaje,
            title = { Text("HuellitasVet") },
            text = { Text(mensaje.orEmpty()) },
            confirmButton = {
                TextButton(
                    onClick = vm::limpiarMensaje
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}
