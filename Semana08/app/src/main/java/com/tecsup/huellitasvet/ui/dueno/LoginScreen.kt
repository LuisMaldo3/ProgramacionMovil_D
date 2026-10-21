package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.R
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun LoginScreen(vm: SesionViewModel, onAcceso: (Rol) -> Unit) {
    var rolNombre by rememberSaveable { mutableStateOf(Rol.CLIENTE.name) }
    val rol = Rol.valueOf(rolNombre)
    var correo by rememberSaveable { mutableStateOf("ana@correo.test") }
    var clave by rememberSaveable { mutableStateOf("1234") }
    var visible by rememberSaveable { mutableStateOf(false) }
    var aviso by remember { mutableStateOf<String?>(null) }
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    LaunchedEffect(usuario?.id) {
        val sesionRol = usuario?.rol?.let { valor -> Rol.entries.firstOrNull { it.name == valor } }
        if (sesionRol != null) onAcceso(sesionRol)
    }
    VetTheme(rol = rol, oscuro = oscuro) {
        val primario = MaterialTheme.colorScheme.primary
        val fondo = MaterialTheme.colorScheme.background
        val superficie = MaterialTheme.colorScheme.surface
        val texto = MaterialTheme.colorScheme.onSurface
        Box(Modifier.fillMaxSize().background(fondo).safeDrawingPadding().imePadding()) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.fillMaxWidth().height(264.dp)) {
                    Image(painter = painterResource(R.drawable.splash_fondo),
                        contentDescription = "Mascotas de HuellitasVet", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize())
                    Box(Modifier.matchParentSize().background(
                        Brush.verticalGradient(listOf(Color(0xFF082C2B).copy(alpha = .15f),
                            fondo.copy(alpha = .08f), fondo))))
                    Column(
                        Modifier.align(Alignment.TopCenter).padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(shape = RoundedCornerShape(17.dp), color = primario) {
                            Icon(Icons.Filled.Pets, "Logotipo de HuellitasVet", tint = Color.White,
                                modifier = Modifier.padding(11.dp).size(28.dp))
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("HuellitasVet", color = Color(0xFF113D37),
                            fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Cuidado veterinario con cariño", color = Color(0xFF3D6961), fontSize = 13.sp)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 17.dp)
                        .offset(y = (-22).dp),
                    color = superficie,
                    shadowElevation = 6.dp,
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Column(Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(13.dp)) {
                        Text("Bienvenido", color = texto, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Accede para cuidar a tus mascotas", color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp)
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(Rol.CLIENTE to "Dueño", Rol.ADMIN to "Veterinario").forEach { (opcion, etiqueta) ->
                                val activo = opcion == rol
                                Surface(
                                    onClick = {
                                        rolNombre = opcion.name
                                        correo = if (opcion == Rol.CLIENTE) "ana@correo.test" else "vet@huellitas.test"
                                        clave = if (opcion == Rol.CLIENTE) "1234" else "admin"
                                        vm.limpiarError()
                                    },
                                    modifier = Modifier.weight(1f).height(43.dp),
                                    color = if (activo) primario else Color.Transparent,
                                    contentColor = if (activo) Color.White else texto,
                                    shape = RoundedCornerShape(12.dp)
                                ) { Box(contentAlignment = Alignment.Center) {
                                    Text(etiqueta, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                } }
                            }
                        }
                        OutlinedTextField(
                            value = correo, onValueChange = { correo = it; vm.limpiarError() },
                            label = { Text("Correo electrónico") }, singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Email, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        OutlinedTextField(
                            value = clave, onValueChange = { clave = it; vm.limpiarError() },
                            label = { Text("Contraseña") }, singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Lock, null) },
                            trailingIcon = { IconButton(onClick = { visible = !visible }) {
                                Icon(if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    if (visible) "Ocultar contraseña" else "Mostrar contraseña")
                            } },
                            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        if (!error.isNullOrBlank()) Text(error.orEmpty(), color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = { aviso = "La recuperación de contraseña no está incluida en este laboratorio." },
                            modifier = Modifier.align(Alignment.End)) {
                            Text("¿Olvidaste tu contraseña?", fontSize = 13.sp)
                        }
                        Button(onClick = { vm.login(correo.trim(), clave, rol) },
                            enabled = !cargando && correo.isNotBlank() && clave.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().height(53.dp),
                            shape = RoundedCornerShape(15.dp)) {
                            if (cargando) CircularProgressIndicator(Modifier.size(21.dp), strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary)
                            else Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        }
                        TextButton(onClick = { aviso = "El registro de cuentas no forma parte de la práctica." },
                            modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text("¿No tienes cuenta? Regístrate", fontSize = 13.sp)
                        }
                    }
                }
                Spacer(Modifier.height(5.dp))
            }
        }
        if (aviso != null) AlertDialog(onDismissRequest = { aviso = null },
            title = { Text("HuellitasVet") }, text = { Text(aviso.orEmpty()) },
            confirmButton = { TextButton(onClick = { aviso = null }) { Text("Entendido") } })
    }
}
