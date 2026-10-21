package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

private val azulFormulario = Color(0xFF2458DE)

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

    // Separación adaptable sin cortar formularios en pantallas pequeñas.
    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 670) / 11).coerceIn(12, 27).dp

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.fillMaxWidth().height(62.dp).padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(if (servicioId == null) "Nuevo servicio" else "Editar servicio",
                        fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(48.dp))
            }
        }
    ) { inner ->
        if (servicioId != null && original == null) {
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                Modifier.fillMaxSize().padding(inner).imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(gapPantalla)
            ) {
                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text("Nombre del servicio") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    shape = RoundedCornerShape(13.dp)
                )
                Text("Duración", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(15, 30, 45, 60).forEach { min ->
                        val seleccionado = duracion == min
                        Surface(
                            onClick = { duracion = min }, modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(30.dp),
                            color = if (seleccionado) azulFormulario else MaterialTheme.colorScheme.surface,
                            contentColor = if (seleccionado) Color.White else MaterialTheme.colorScheme.onSurface,
                            border = if (seleccionado) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Box(Modifier.height(37.dp), contentAlignment = Alignment.Center) {
                                Text("$min min", fontSize = 13.sp)
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = precio, onValueChange = { precio = it },
                    label = { Text("Precio (S/)") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true, shape = RoundedCornerShape(13.dp)
                )
                OutlinedTextField(
                    value = descripcion, onValueChange = { descripcion = it },
                    label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(),
                    minLines = 3, maxLines = 4, shape = RoundedCornerShape(13.dp)
                )
                Text("Ícono", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    listOf("heart", "syringe", "scissors").forEach { op ->
                        val seleccionado = icono == op
                        Surface(
                            onClick = { icono = op },
                            modifier = Modifier.weight(1f).height(55.dp),
                            color = if (seleccionado) Color(0xFFDBEAFE) else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(if (seleccionado) 1.5.dp else 1.dp,
                                if (seleccionado) azulFormulario else MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(iconoServicio(op), contentDescription = op, tint = azulFormulario,
                                    modifier = Modifier.size(26.dp))
                            }
                        }
                    }
                }
                Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Servicio activo", fontWeight = FontWeight.Bold)
                            Text("Los dueños podrán pedirlo", fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = activo, onCheckedChange = { activo = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = azulFormulario))
                    }
                }
                if (!mensaje.isNullOrBlank()) {
                    Text(mensaje.orEmpty(), color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }
                Button(
                    onClick = {
                        vm.guardar(Servicio(id = original?.id ?: 0, nombre = nombre,
                            descripcion = descripcion, duracionMin = duracion,
                            precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
                            activo = activo, icono = icono))
                    },
                    enabled = !procesando,
                    modifier = Modifier.fillMaxWidth().height(51.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = azulFormulario),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Guardar servicio", fontWeight = FontWeight.Bold) }
                OutlinedButton(onClick = onVolver,
                    modifier = Modifier.fillMaxWidth().height(49.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, azulFormulario)) {
                    Text("Cancelar", color = azulFormulario)
                }
                if (original != null) {
                    TextButton(onClick = { confirmarEliminar = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Eliminar servicio", color = Color(0xFFDC2626))
                    }
                }
            }
        }
    }
    if (confirmarEliminar && original != null) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar servicio") },
            text = { Text("Si tiene citas, no se eliminará; quedará desactivado. ¿Continuar?") },
            confirmButton = {
                TextButton(onClick = { confirmarEliminar = false; vm.eliminar(original) }) {
                    Text("Continuar")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminar = false }) { Text("Volver") }
            }
        )
    }
}
