
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onAcceso: (Rol) -> Unit
) {

    var rolNombre by rememberSaveable {
        mutableStateOf(Rol.CLIENTE.name)
    }

    val rol = Rol.valueOf(rolNombre)

    var correo by rememberSaveable {
        mutableStateOf("ana@correo.test")
    }

    var clave by rememberSaveable {
        mutableStateOf("1234")
    }

    var aviso by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    // Navegar cuando el login sea correcto
    LaunchedEffect(usuario?.id) {

        val rolUsuario = usuario?.rol?.let { valor ->
            Rol.entries.firstOrNull {
                it.name == valor
            }
        }

        if (rolUsuario != null) {
            onAcceso(rolUsuario)
        }
    }

    VetTheme(
        rol = rol,
        oscuro = oscuro
    ) {

        val primario = MaterialTheme.colorScheme.primary

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface
                )
                .safeDrawingPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 34.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Bienvenido",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Ingresa para continuar",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(10.dp))

            // Selector Dueño / Veterinario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF2F4F6),
                        RoundedCornerShape(13.dp)
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
                            .height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = if (elegido)
                            primario
                        else
                            Color.Transparent,
                        contentColor = if (elegido)
                            Color.White
                        else
                            Color(0xFF6B7280)
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = etiqueta,
                                fontSize = 13.sp,
                                fontWeight = if (elegido)
                                    FontWeight.SemiBold
                                else
                                    FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Correo
            VetTextField(
                valor = correo,
                onValorCambio = {
                    correo = it
                    vm.limpiarError()
                },
                etiqueta = "Correo",
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
                        aviso = "La recuperación de contraseña no forma parte de esta práctica."
                    }
                ) {
                    Text(
                        "¿Olvidaste tu contraseña?",
                        fontSize = 11.sp,
                        color = primario
                    )
                }
            }

            // Mensajes de error
            if (!error.isNullOrEmpty()) {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(6.dp))

            // Botón Ingresar
            Button(
                onClick = {
                    vm.login(correo, clave, rol)
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primario
                )
            ) {

                if (cargando) {

                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        "Ingresar",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            // Registro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "¿No tienes cuenta?",
                    fontSize = 12.sp
                )

                TextButton(
                    onClick = {
                        aviso = "El registro de cuentas no está incluido en el Paso 6."
                    }
                ) {
                    Text(
                        "Regístrate",
                        fontSize = 12.sp,
                        color = primario,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (aviso != null) {
            AlertDialog(
                onDismissRequest = {
                    aviso = null
                },
                title = {
                    Text("HuellitasVet")
                },
                text = {
                    Text(aviso.orEmpty())
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            aviso = null
                        }
                    ) {
                        Text("Entendido")
                    }
                }
            )
        }
    }
}
