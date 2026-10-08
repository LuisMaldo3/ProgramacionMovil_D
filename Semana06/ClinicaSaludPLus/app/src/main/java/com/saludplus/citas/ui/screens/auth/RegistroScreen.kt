package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun RegistroScreen(navController: NavController) {
    //Estados, OutlinedTextField, validaciones y registrarUsuario().
    PantallaEnConstruccion(
        nombre = "Registro",
        acciones = listOf(
            "Registrarme e ir a Inicio" to { navController.navigate(Rutas.HOME) },
            "Términos y condiciones" to { navController.navigate(Rutas.TERMINOS) }
        )
    )
}
