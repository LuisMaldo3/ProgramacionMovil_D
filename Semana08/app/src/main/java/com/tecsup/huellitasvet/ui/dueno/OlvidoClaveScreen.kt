package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Security
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
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun OlvidoClaveScreen(
    vm: SesionViewModel,
    onVolverLogin: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var nuevaClave by rememberSaveable { mutableStateOf("") }
    var confirmacionClave by rememberSaveable { mutableStateOf("") }

    var usuarioEncontrado by remember { mutableStateOf<Usuario?>(null) }
    var codigoSimulado by rememberSaveable { mutableStateOf("") }
    var codigoIngresado by rememberSaveable { mutableStateOf("") }

    var visibleClave by rememberSaveable { mutableStateOf(false) }
    var visibleConfirmacion by rememberSaveable { mutableStateOf(false) }

    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val recuperacionExitosa by vm.recuperacionExitosa.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    DisposableEffect(Unit) {
        onDispose { vm.resetearEstadosAuxiliares() }
    }

    VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
        val primario = MaterialTheme.colorScheme.primary
        val fondo = MaterialTheme.colorScheme.background
        val superficie = MaterialTheme.colorScheme.surface
        val texto = MaterialTheme.colorScheme.onSurface

        Box(
            Modifier
                .fillMaxSize()
                .background(fondo)
                .safeDrawingPadding()
                .imePadding()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Banner superior
                Box(Modifier.fillMaxWidth().height(240.dp)) {
                    Image(
                        painter = painterResource(R.drawable.splash_fondo),
                        contentDescription = "Mascotas HuellitasVet",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF082C2B).copy(alpha = .3f),
                                        fondo.copy(alpha = .2f),
                                        fondo
                                    )
                                )
                            )
                    )

                    // Botón Volver
                    IconButton(
                        onClick = onVolverLogin,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio de sesión",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Logo y Titulo
                    Column(
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(17.dp),
                            color = primario,
                            shadowElevation = 4.dp
                        ) {
                            Icon(
                                Icons.Filled.Key,
                                contentDescription = "Recuperación de contraseña",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(11.dp)
                                    .size(28.dp)
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "HuellitasVet",
                            color = Color(0xFF113D37),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "Recuperación de acceso",
                            color = Color(0xFF3D6961),
                            fontSize = 13.sp
                        )
                    }
                }

                // Card principal
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 17.dp)
                        .offset(y = (-20).dp),
                    color = superficie,
                    shadowElevation = 6.dp,
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Column(
                        Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "¿Olvidaste tu contraseña?",
                            color = texto,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            "Ingresa el correo registrado con el que creaste tu cuenta.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )

                        // PASO 1: Ingreso de correo
                        OutlinedTextField(
                            value = correo,
                            onValueChange = {
                                correo = it
                                usuarioEncontrado = null
                                codigoSimulado = ""
                                vm.limpiarError()
                            },
                            label = { Text("Correo electrónico registrado") },
                            singleLine = true,
                            enabled = usuarioEncontrado == null,
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        if (usuarioEncontrado == null) {
                            Button(
                                onClick = {
                                    vm.verificarCorreoExiste(correo) { usuario ->
                                        if (usuario != null) {
                                            usuarioEncontrado = usuario
                                            val codigo = (100000..999999).random().toString()
                                            codigoSimulado = codigo
                                            codigoIngresado = codigo
                                        }
                                    }
                                },
                                enabled = !cargando && correo.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                if (cargando) {
                                    CircularProgressIndicator(
                                        Modifier.size(21.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Verificar Correo", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }
                            }
                        } else {
                            // PASO 2: Confirmación de usuario y nueva contraseña
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.MarkEmailRead,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "Cuenta encontrada: ${usuarioEncontrado?.nombre}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            "Se envió un código de verificación a tu correo.",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }
                                    TextButton(onClick = {
                                        usuarioEncontrado = null
                                        codigoSimulado = ""
                                        vm.limpiarError()
                                    }) {
                                        Text("Cambiar", fontSize = 12.sp)
                                    }
                                }
                            }

                            // Input Código de Verificación
                            OutlinedTextField(
                                value = codigoIngresado,
                                onValueChange = { codigoIngresado = it; vm.limpiarError() },
                                label = { Text("Código de seguridad") },
                                singleLine = true,
                                leadingIcon = { Icon(Icons.Filled.Security, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                supportingText = { Text("Código de prueba generado: $codigoSimulado") }
                            )

                            // Nueva Contraseña
                            OutlinedTextField(
                                value = nuevaClave,
                                onValueChange = { nuevaClave = it; vm.limpiarError() },
                                label = { Text("Nueva contraseña") },
                                singleLine = true,
                                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { visibleClave = !visibleClave }) {
                                        Icon(
                                            if (visibleClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            contentDescription = null
                                        )
                                    }
                                },
                                visualTransformation = if (visibleClave) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            )

                            // Confirmar Nueva Contraseña
                            OutlinedTextField(
                                value = confirmacionClave,
                                onValueChange = { confirmacionClave = it; vm.limpiarError() },
                                label = { Text("Confirmar nueva contraseña") },
                                singleLine = true,
                                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { visibleConfirmacion = !visibleConfirmacion }) {
                                        Icon(
                                            if (visibleConfirmacion) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            contentDescription = null
                                        )
                                    }
                                },
                                visualTransformation = if (visibleConfirmacion) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            )

                            // Botón de Restablecer
                            Button(
                                onClick = {
                                    if (codigoIngresado != codigoSimulado) {
                                        vm.verificarCorreoExiste("") { } // fuerza limpiar y mostrar error custom
                                    }
                                    vm.cambiarContrasena(
                                        correo = correo,
                                        nuevaClave = nuevaClave,
                                        confirmacion = confirmacionClave,
                                        onExito = { }
                                    )
                                },
                                enabled = !cargando && nuevaClave.isNotBlank() && confirmacionClave.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                if (cargando) {
                                    CircularProgressIndicator(
                                        Modifier.size(21.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Restablecer Contraseña", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }
                            }
                        }

                        // Mostrar error si existe
                        if (!error.isNullOrBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = error.orEmpty(),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        // Link para volver
                        TextButton(
                            onClick = {
                                vm.limpiarError()
                                onVolverLogin()
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Volver al inicio de sesión", fontSize = 13.sp)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            // Modal/Dialog de Éxito
            AnimatedVisibility(
                visible = recuperacionExitosa,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AlertDialog(
                    onDismissRequest = { },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    title = {
                        Text(
                            "¡Contraseña Actualizada!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Text(
                            "Tu contraseña ha sido restablecida exitosamente. Puedes ingresar ahora con tu nueva clave.",
                            fontSize = 14.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = onVolverLogin,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Iniciar Sesión")
                        }
                    }
                )
            }
        }
    }
}
