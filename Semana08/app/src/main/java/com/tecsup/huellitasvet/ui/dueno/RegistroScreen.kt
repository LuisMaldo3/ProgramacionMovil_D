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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
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
fun RegistroScreen(
    vm: SesionViewModel,
    onVolverLogin: () -> Unit,
    onRegistroExitoso: () -> Unit = onVolverLogin
) {
    var rolNombre by rememberSaveable { mutableStateOf(Rol.CLIENTE.name) }
    val rol = Rol.valueOf(rolNombre)

    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmacionClave by rememberSaveable { mutableStateOf("") }

    var visibleClave by rememberSaveable { mutableStateOf(false) }
    var visibleConfirmacion by rememberSaveable { mutableStateOf(false) }

    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val registroExitoso by vm.registroExitoso.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    DisposableEffect(Unit) {
        onDispose { vm.resetearEstadosAuxiliares() }
    }

    VetTheme(rol = rol, oscuro = oscuro) {
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
                // Banner superior decorativo
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

                    // Botón Volver arriba a la izquierda
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

                    // Logo y Marca central
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
                                Icons.Filled.PersonAdd,
                                contentDescription = "Registro de usuario",
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
                            "Crea tu cuenta en segundos",
                            color = Color(0xFF3D6961),
                            fontSize = 13.sp
                        )
                    }
                }

                // Card Principal de Formulario
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
                        verticalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        Text(
                            "Registro de Usuario",
                            color = texto,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "Completa tus datos para empezar a utilizar HuellitasVet",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )

                        // Selector de Perfil (Dueño / Veterinario)
                        Text(
                            "Selecciona tu perfil:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = texto
                        )

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(Rol.CLIENTE to "Dueño de Mascota", Rol.ADMIN to "Veterinario").forEach { (opcion, etiqueta) ->
                                val activo = opcion == rol
                                Surface(
                                    onClick = {
                                        rolNombre = opcion.name
                                        vm.limpiarError()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(43.dp),
                                    color = if (activo) primario else Color.Transparent,
                                    contentColor = if (activo) Color.White else texto,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            etiqueta,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Campo Nombre Completo
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it; vm.limpiarError() },
                            label = { Text("Nombre completo") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // Campo Correo Electrónico
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it; vm.limpiarError() },
                            label = { Text("Correo electrónico") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // Campo Contraseña
                        OutlinedTextField(
                            value = clave,
                            onValueChange = { clave = it; vm.limpiarError() },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { visibleClave = !visibleClave }) {
                                    Icon(
                                        if (visibleClave) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (visibleClave) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            visualTransformation = if (visibleClave) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // Campo Confirmar Contraseña
                        OutlinedTextField(
                            value = confirmacionClave,
                            onValueChange = { confirmacionClave = it; vm.limpiarError() },
                            label = { Text("Confirmar contraseña") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { visibleConfirmacion = !visibleConfirmacion }) {
                                    Icon(
                                        if (visibleConfirmacion) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (visibleConfirmacion) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            visualTransformation = if (visibleConfirmacion) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // Mensaje de Error
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

                        Spacer(Modifier.height(4.dp))

                        // Botón Crear Cuenta
                        Button(
                            onClick = {
                                vm.registrar(
                                    nombre = nombre,
                                    correo = correo,
                                    clave = clave,
                                    confirmacionClave = confirmacionClave,
                                    rolSeleccionado = rol,
                                    onExito = {
                                        // Éxito registrado en ViewModel
                                    }
                                )
                            },
                            enabled = !cargando && nombre.isNotBlank() && correo.isNotBlank() && clave.isNotBlank() && confirmacionClave.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(53.dp),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            if (cargando) {
                                CircularProgressIndicator(
                                    Modifier.size(21.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Crear mi cuenta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }

                        // Enlace para volver a Login
                        TextButton(
                            onClick = {
                                vm.limpiarError()
                                onVolverLogin()
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("¿Ya tienes una cuenta? Inicia sesión", fontSize = 13.sp)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            // Modal/Dialog de Éxito al registrar
            AnimatedVisibility(
                visible = registroExitoso,
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
                            "¡Registro Exitoso!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Text(
                            "Tu cuenta para $nombre ha sido creada correctamente. Ahora puedes iniciar sesión con tu correo y contraseña.",
                            fontSize = 14.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onRegistroExitoso()
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ir a Iniciar Sesión")
                        }
                    }
                )
            }
        }
    }
}
