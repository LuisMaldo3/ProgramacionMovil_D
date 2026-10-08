package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    //AlertDialog y cancelarCita() (reto extra).
    PantallaEnConstruccion(
        nombre = "Detalle de cita",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}
