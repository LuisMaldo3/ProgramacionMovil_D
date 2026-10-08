package com.saludplus.citas.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun RegistroScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var acepta by remember { mutableStateOf(false) }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }
    var errorTerminos by remember { mutableStateOf(false) }

    fun validar(): Boolean {
        errorNombre = if (nombre.isBlank()) "Ingresa tu nombre" else null
        errorCorreo = if (!Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) "Correo no válido" else null
        errorContrasena = if (contrasena.length < 6) "Mínimo 6 caracteres" else null
        errorConfirmar = if (confirmar != contrasena) "Las contraseñas no coinciden" else null
        errorTerminos = !acepta
        return errorNombre == null && errorCorreo == null &&
                errorContrasena == null && errorConfirmar == null && !errorTerminos
    }

    Scaffold(
        topBar = { BarraSuperior("Crear cuenta", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            CampoTexto(nombre, { nombre = it }, "Nombre completo", error = errorNombre)
            Spacer(Modifier.height(8.dp))
            CampoTexto(
                correo, { correo = it }, "Correo electrónico",
                error = errorCorreo, teclado = KeyboardType.Email
            )
            Spacer(Modifier.height(8.dp))
            CampoTexto(
                contrasena, { contrasena = it }, "Contraseña",
                error = errorContrasena, esPassword = true
            )
            Spacer(Modifier.height(8.dp))
            CampoTexto(
                confirmar, { confirmar = it }, "Confirmar contraseña",
                error = errorConfirmar, esPassword = true
            )
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = acepta, onCheckedChange = { acepta = it })
                Text("Acepto los ")
                TextButton(onClick = { navController.navigate(Rutas.TERMINOS) }) {
                    Text("términos y condiciones")
                }
            }
            if (errorTerminos) {
                Text(
                    "Debes aceptar los términos",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(16.dp))

            BotonPrincipal("Crear cuenta", onClick = {
                if (validar()) {
                    val usuario = Usuario(
                        Repositorio.siguienteIdUsuario(),
                        nombre.trim(),
                        correo.trim(),
                        contrasena
                    )
                    if (Repositorio.registrarUsuario(usuario)) {
                        Repositorio.iniciarSesion(usuario.correo, usuario.contrasena)
                        navController.navigate(Rutas.HOME) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    } else {
                        errorCorreo = "Este correo ya está registrado"
                    }
                }
            })
        }
    }
}