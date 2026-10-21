package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int?,
    onVolver: () -> Unit,
    onGuardado: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val guardado by vm.guardado.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val original = servicios.firstOrNull { it.id == servicioId }
    var inicializado by rememberSaveable(servicioId) { mutableStateOf(false) }
    var nombre by rememberSaveable(servicioId) { mutableStateOf("") }
    var descripcion by rememberSaveable(servicioId) { mutableStateOf("") }
    var precio by rememberSaveable(servicioId) { mutableStateOf("") }
    var duracion by rememberSaveable(servicioId) { mutableIntStateOf(30) }
    var icono by rememberSaveable(servicioId) { mutableStateOf("heart") }
    var activo by rememberSaveable(servicioId) { mutableStateOf(true) }
    var confirmarEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(original?.id) {
        if (!inicializado && original != null) {
            nombre = original.nombre
            descripcion = original.descripcion
            precio = original.precio.toString()
            duracion = original.duracionMin
            icono = original.icono
            activo = original.activo
            inicializado = true
        }
    }
    LaunchedEffect(guardado) {
        if (guardado) { vm.limpiarGuardado(); onGuardado() }
    }
    DisposableEffect(vm) {
        onDispose { vm.limpiarMensaje(); vm.limpiarGuardado() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                titulo = if (servicioId == null) "Nuevo servicio" else "Editar servicio",
                onVolver = onVolver
            )
        }
    ) { inner ->
        if (servicioId != null && original == null) {
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                        VetTextField(
                            valor = nombre,
                            onValorCambio = { nombre = it },
                            etiqueta = "Nombre del servicio"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Duración estimada",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                listOf(15, 30, 45, 60).forEach { min ->
                                    val seleccionado = duracion == min
                                    FilterChip(
                                        selected = seleccionado,
                                        onClick = { duracion = min },
                                        label = { Text("$min min", fontWeight = FontWeight.SemiBold) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        VetTextField(
                            valor = precio,
                            onValorCambio = { precio = it },
                            etiqueta = "Precio (S/)",
                            tipoTeclado = KeyboardType.Decimal
                        )

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 4,
                            shape = RoundedCornerShape(14.dp)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Icono representativo",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                listOf("heart", "syringe", "scissors").forEach { op ->
                                    val seleccionado = icono == op
                                    Surface(
                                        onClick = { icono = op },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp),
                                        color = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(14.dp),
                                        border = BorderStroke(
                                            if (seleccionado) 2.dp else 1.dp,
                                            if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                        )
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = iconoServicio(op),
                                                contentDescription = op,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = "Servicio activo",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Disponible para reservas públicas de dueños",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = activo,
                                    onCheckedChange = { activo = it },
                                    colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                                )
                            }
                        }

                        if (!mensaje.isNullOrBlank()) {
                            Text(
                                text = mensaje.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        VetButton(
                            texto = if (servicioId == null) "Guardar servicio" else "Actualizar servicio",
                            onClick = {
                                vm.guardar(
                                    Servicio(
                                        id = original?.id ?: 0,
                                        nombre = nombre,
                                        descripcion = descripcion,
                                        duracionMin = duracion,
                                        precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
                                        activo = activo,
                                        icono = icono
                                    )
                                )
                            },
                            habilitado = !procesando
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

                        if (original != null) {
                            TextButton(
                                onClick = { confirmarEliminar = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Eliminar servicio",
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (confirmarEliminar && original != null) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar servicio") },
            text = { Text("Si tiene citas asociadas, no se eliminará; se marcará como inactivo. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = { confirmarEliminar = false; vm.eliminar(original) }) {
                    Text("Continuar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminar = false }) { Text("Volver") }
            }
        )
    }
}
