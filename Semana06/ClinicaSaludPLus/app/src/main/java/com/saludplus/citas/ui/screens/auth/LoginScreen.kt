package com.saludplus.citas.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun LoginScreen(navController: NavController) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorGeneral by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        errorCorreo = if (!Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) "Correo no válido" else null
        errorContrasena = if (contrasena.isEmpty()) "Ingresa tu contraseña" else null
        return errorCorreo == null && errorContrasena == null
    }

    Scaffold(
        topBar = { BarraSuperior("Iniciar sesión", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            CampoTexto(
                correo, { correo = it }, "Correo electrónico",
                error = errorCorreo, teclado = KeyboardType.Email
            )
            Spacer(Modifier.height(8.dp))
            CampoTexto(
                contrasena, { contrasena = it }, "Contraseña",
                error = errorContrasena, esPassword = true
            )
            errorGeneral?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(16.dp))

            BotonPrincipal("Entrar", onClick = {
                errorGeneral = null
                if (validar()) {
                    if (Repositorio.iniciarSesion(correo.trim(), contrasena)) {
                        navController.navigate(Rutas.HOME) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    } else {
                        errorGeneral = "Correo o contraseña incorrectos"
                    }
                }
            })
            TextButton(onClick = { navController.navigate(Rutas.REGISTRO) }) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}