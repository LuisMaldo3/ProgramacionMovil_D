package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import com.tecsup.huellitasvet.ui.components.DialogoExitoAnimado
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

private val Verde = Color(0xFF087F6D)
private val Fondo = Color(0xFFF4F8F6)
private val Oscuro = Color(0xFF172B3A)
private val Gris = Color(0xFF808B96)
private val Borde = Color(0xFFDFE8E3)

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

    // Cargar los valores existentes solo cuando se abre una mascota para editar.
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
    var mostrarGuardado by remember { mutableStateOf(false) }
    LaunchedEffect(vm) {
        vm.guardado.collect { mostrarGuardado = true }
    }
    if (mostrarGuardado) {
        DialogoExitoAnimado(
            titulo = if (editando) "¡Mascota actualizada!" else "¡Mascota registrada!",
            descripcion = "Los datos de tu mascota se guardaron correctamente en HuellitasVet.",
            detalle = nombre.ifBlank { null },
            boton = "Continuar",
            onContinuar = { mostrarGuardado = false; onGuardado() }
        )
    }

    // Separación adaptable sin cortar formularios en pantallas pequeñas.
    val gapPantalla = ((LocalConfiguration.current.screenHeightDp - 650) / 11).coerceIn(12, 18).dp

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        BarraSuperiorMascota(if (editando) "Editar mascota" else "Nueva mascota", onVolver)
    }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(gapPantalla)
        ) {
            // Avatar y botón + fieles a la pantalla guía.
            Box(modifier = Modifier.fillMaxWidth().height(104.dp), contentAlignment = Alignment.TopCenter) {
                Box(modifier = Modifier.size(88.dp), contentAlignment = Alignment.Center) {
                    MascotaAvatar(
                        especie = runCatching { Especie.valueOf(especie) }.getOrDefault(Especie.OTRO),
                        tamano = 82.dp
                    )
                    Box(
                        modifier = Modifier.align(Alignment.BottomEnd).size(25.dp)
                            .clip(CircleShape).background(Verde),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Avatar según especie", tint = Color.White,
                            modifier = Modifier.size(15.dp))
                    }
                }
            }

            CampoMascota(
                etiqueta = "Nombre", valor = nombre, placeholder = "Nombre de la mascota",
                onCambio = { nombre = it; vm.limpiarErrores() }, error = errores["nombre"]
            )
            Text("Especie", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                listOf("PERRO" to "Perro", "GATO" to "Gato", "OTRO" to "Otro").forEach { (valor, titulo) ->
                    ChipMascota(
                        titulo = titulo, seleccionado = especie == valor,
                        onClick = { especie = valor; vm.limpiarErrores() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            errores["especie"]?.let { TextoError(it) }

            CampoMascota(
                etiqueta = "Raza", valor = raza, placeholder = "Ej. Labrador",
                onCambio = { raza = it }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoMascota("Año de nacimiento", anio, "2021", { anio = it; vm.limpiarErrores() },
                        errores["anioNacimiento"], KeyboardType.Number)
                }
                Box(Modifier.weight(1f)) {
                    CampoMascota("Peso (kg)", peso, "24.5", { peso = it; vm.limpiarErrores() },
                        errores["peso"], KeyboardType.Decimal)
                }
            }
            Text("Sexo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("MACHO" to "Macho", "HEMBRA" to "Hembra").forEach { (valor, titulo) ->
                    ChipMascota(
                        titulo = titulo, seleccionado = sexo == valor,
                        onClick = { sexo = valor; vm.limpiarErrores() },
                        modifier = Modifier.weight(1f), resaltadoSuave = true
                    )
                }
            }
            errores["sexo"]?.let { TextoError(it) }
            mensaje?.let { TextoError(it) }
            Spacer(Modifier.height(4.dp))

            Button(
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
                enabled = !procesando && (!editando || mascota != null),
                modifier = Modifier.fillMaxWidth().height(49.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) { Text(if (editando) "Actualizar mascota" else "Guardar mascota", fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(9.dp),
                border = BorderStroke(1.2.dp, Verde),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Verde)
            ) { Text("Cancelar", fontWeight = FontWeight.SemiBold) }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
internal fun BarraSuperiorMascota(titulo: String, onVolver: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().statusBarsPadding().height(48.dp)) {
        IconButton(onClick = onVolver, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Volver",
                modifier = Modifier.size(17.dp), tint = Oscuro)
        }
        Text(titulo, modifier = Modifier.align(Alignment.Center), fontSize = 16.sp,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun CampoMascota(
    etiqueta: String,
    valor: String,
    placeholder: String,
    onCambio: (String) -> Unit,
    error: String? = null,
    teclado: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(54.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(1.dp, if (error != null) Color(0xFFDC2626) else Borde,
                    RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                Text(etiqueta, color = Gris, fontSize = 13.sp)
                BasicTextField(
                    value = valor,
                    onValueChange = onCambio,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = teclado),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth().padding(top = 1.dp),
                    decorationBox = { campo ->
                        Box {
                            if (valor.isEmpty()) Text(placeholder, color = Gris, fontSize = 14.sp)
                            campo()
                        }
                    }
                )
            }
        }
        if (error != null) TextoError(error)
    }
}

@Composable
private fun ChipMascota(
    titulo: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    resaltadoSuave: Boolean = false
) {
    val relleno = when {
        seleccionado && resaltadoSuave -> Color(0xFFDDF7EF)
        seleccionado -> Verde
        else -> Color.White
    }
    Surface(
        onClick = onClick,
        modifier = modifier.height(38.dp),
        color = relleno,
        shape = RoundedCornerShape(11.dp),
        border = BorderStroke(if (seleccionado) 1.2.dp else 1.dp,
            if (seleccionado && resaltadoSuave) Verde else if (seleccionado) Verde else Borde)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                color = if (seleccionado && !resaltadoSuave) Color.White else if (seleccionado) Verde else Gris)
        }
    }
}

@Composable
private fun TextoError(mensaje: String) {
    Text(mensaje, color = Color(0xFFDC2626), fontSize = 13.sp)
}
