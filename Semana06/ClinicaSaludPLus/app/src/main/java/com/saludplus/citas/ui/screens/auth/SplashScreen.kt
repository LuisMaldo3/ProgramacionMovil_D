package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun SplashScreen(navController: NavController) {
    //Image, Column y botones (Registro / Iniciar sesión).
    PantallaEnConstruccion(
        nombre = "Splash",
        acciones = listOf(
            "Ir a Registro" to { navController.navigate(Rutas.REGISTRO) },
            "Ir a Iniciar sesión" to { navController.navigate(Rutas.LOGIN) }
        )
    )
}
