package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun NotificacionesScreen(navController: NavController) {
    //map sobre las citas (reto extra).
    PantallaEnConstruccion(
        nombre = "Notificaciones",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}
