package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onAcceso: (Rol) -> Unit
) {
    var rolNombre by rememberSaveable { mutableStateOf(Rol.CLIENTE.name) }
    val rol = Rol.valueOf(rolNombre)

    var correo by rememberSaveable { mutableStateOf("ana@correo.test") }
    var clave by rememberSaveable { mutableStateOf("1234") }
    var aviso by rememberSaveable { mutableStateOf<String?>(null) }

    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    LaunchedEffect(usuario?.id) {
        val rolUsuario = usuario?.rol?.let { valor ->
            Rol.entries.firstOrNull { it.name == valor }
        }
        if (rolUsuario != null) {
            onAcceso(rolUsuario)
        }
    }

    VetTheme(rol = rol, oscuro = oscuro) {
        val primario = MaterialTheme.colorScheme.primary

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeDrawingPadding()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Logo/Header Card
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🐾",
                            fontSize = 36.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "HuellitasVet",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Selecciona tu perfil e ingresa tus credenciales",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Login Form Card
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Selector Dueño / Veterinario
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                Rol.CLIENTE to "Dueño",
                                Rol.ADMIN to "Veterinario"
                            ).forEach { (opcion, etiqueta) ->
                                val elegido = rol == opcion

                                Surface(
                                    onClick = {
                                        rolNombre = opcion.name
                                        vm.limpiarError()
                                        if (opcion == Rol.CLIENTE) {
                                            correo = "ana@correo.test"
                                            clave = "1234"
                                        } else {
                                            correo = "vet@huellitas.test"
                                            clave = "admin"
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (elegido) primario else Color.Transparent,
                                    contentColor = if (elegido) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = etiqueta,
                                            fontSize = 14.sp,
                                            fontWeight = if (elegido) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        // Correo
                        VetTextField(
                            valor = correo,
                            onValorCambio = {
                                correo = it
                                vm.limpiarError()
                            },
                            etiqueta = "Correo electrónico",
                            icono = Icons.Default.Email,
                            tipoTeclado = KeyboardType.Email
                        )

                        // Contraseña
                        VetTextField(
                            valor = clave,
                            onValorCambio = {
                                clave = it
                                vm.limpiarError()
                            },
                            etiqueta = "Contraseña",
                            icono = Icons.Default.Lock,
                            ocultar = true,
                            tipoTeclado = KeyboardType.Password
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(
                                onClick = {
                                    aviso = "La recuperación de contraseña no está configurada."
                                }
                            ) {
                                Text(
                                    text = "¿Olvidaste tu contraseña?",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = primario
                                )
                            }
                        }

                        if (!error.isNullOrEmpty()) {
                            Text(
                                text = error.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        VetButton(
                            texto = if (cargando) "Ingresando..." else "Ingresar",
                            onClick = { vm.login(correo, clave, rol) },
                            habilitado = !cargando
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(
                        onClick = {
                            aviso = "El registro de nuevas cuentas estará disponible próximamente."
                        }
                    ) {
                        Text(
                            text = "Regístrate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = primario,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (aviso != null) {
            AlertDialog(
                onDismissRequest = { aviso = null },
                title = { Text("HuellitasVet") },
                text = { Text(aviso.orEmpty()) },
                confirmButton = {
                    TextButton(onClick = { aviso = null }) {
                        Text("Entendido")
                    }
                }
            )
        }
    }
}
