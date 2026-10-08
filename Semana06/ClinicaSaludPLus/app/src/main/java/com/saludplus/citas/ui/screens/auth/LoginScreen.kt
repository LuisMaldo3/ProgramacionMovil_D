package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun LoginScreen(navController: NavController) {
    // Campos correo/contraseña y iniciarSesion() (usa find).
    PantallaEnConstruccion(
        nombre = "Iniciar sesión",
        acciones = listOf(
            "Entrar a Inicio" to { navController.navigate(Rutas.HOME) }
        )
    )
}
