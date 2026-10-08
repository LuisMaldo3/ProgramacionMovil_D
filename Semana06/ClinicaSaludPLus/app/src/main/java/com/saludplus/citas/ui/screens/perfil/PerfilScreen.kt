package com.saludplus.citas.ui.screens.perfil

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun PerfilScreen(navController: NavController) {
    //Mostrar usuarioActual y cerrarSesion() con popUpTo.
    PantallaEnConstruccion(
        nombre = "Perfil",
        acciones = listOf(
            "Cerrar sesión" to { navController.navigate(Rutas.SPLASH) { popUpTo(0) } }
        )
    )
}
