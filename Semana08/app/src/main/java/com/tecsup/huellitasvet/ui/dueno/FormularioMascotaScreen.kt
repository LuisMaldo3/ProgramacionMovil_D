package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    onVolver: () -> Unit,
    onGuardado: () -> Unit
) {
    val mascota by vm.mascota.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val editando = vm.mascotaId != null

    var nombre by rememberSaveable { mutableStateOf("") }
    var raza by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf("PERRO") }
    var anio by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf("MACHO") }

    LaunchedEffect(mascota?.id) {
        mascota?.let {
            nombre = it.nombre
            raza = it.raza
            especie = it.especie
            anio = it.anioNacimiento.toString()
            peso = it.peso.toString()
            sexo = it.sexo
        }
    }

    LaunchedEffect(vm) {
        vm.guardado.collect { onGuardado() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = if (editando) "Editar mascota" else "Nueva mascota",
                onVolver = onVolver
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar Preview Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MascotaAvatar(
                        especie = runCatching { Especie.valueOf(especie) }.getOrDefault(Especie.OTRO),
                        tamano = 96.dp
                    )

                    Text(
                        text = if (nombre.isBlank()) "Nombre de la mascota" else nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Datos Form Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    VetTextField(
                        valor = nombre,
                        onValorCambio = {
                            nombre = it
                            vm.limpiarErrores()
                        },
                        etiqueta = "Nombre",
                        error = errores["nombre"]
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Especie",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("PERRO" to "Perro", "GATO" to "Gato", "OTRO" to "Otro").forEach { (valor, titulo) ->
                                val seleccionado = especie == valor
                                FilterChip(
                                    selected = seleccionado,
                                    onClick = {
                                        especie = valor
                                        vm.limpiarErrores()
                                    },
                                    label = { Text(titulo, fontWeight = FontWeight.SemiBold) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        errores["especie"]?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    VetTextField(
                        valor = raza,
                        onValorCambio = { raza = it },
                        etiqueta = "Raza"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.weight(1f)) {
                            VetTextField(
                                valor = anio,
                                onValorCambio = {
                                    anio = it
                                    vm.limpiarErrores()
                                },
                                etiqueta = "Año nacimiento",
                                error = errores["anioNacimiento"],
                                tipoTeclado = KeyboardType.Number
                            )
                        }
                        Box(Modifier.weight(1f)) {
                            VetTextField(
                                valor = peso,
                                onValorCambio = {
                                    peso = it
                                    vm.limpiarErrores()
                                },
                                etiqueta = "Peso (kg)",
                                error = errores["peso"],
                                tipoTeclado = KeyboardType.Decimal
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Sexo",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("MACHO" to "Macho", "HEMBRA" to "Hembra").forEach { (valor, titulo) ->
                                val seleccionado = sexo == valor
                                FilterChip(
                                    selected = seleccionado,
                                    onClick = {
                                        sexo = valor
                                        vm.limpiarErrores()
                                    },
                                    label = { Text(titulo, fontWeight = FontWeight.SemiBold) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        errores["sexo"]?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    mensaje?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    VetButton(
                        texto = if (editando) "Actualizar mascota" else "Guardar mascota",
                        onClick = {
                            vm.guardar(
                                Mascota(
                                    id = mascota?.id ?: 0,
                                    duenoId = vm.duenoId,
                                    nombre = nombre,
                                    especie = especie,
                                    raza = raza,
                                    anioNacimiento = anio.toIntOrNull() ?: 0,
                                    peso = peso.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                    sexo = sexo
                                )
                            )
                        },
                        habilitado = !procesando && (!editando || mascota != null)
                    )

                    OutlinedButton(
                        onClick = onVolver,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
