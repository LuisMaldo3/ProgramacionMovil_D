package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun CitaExitosaScreen(navController: NavController) {
    //Resumen de la cita agendada y botón a Mis citas.
    PantallaEnConstruccion(
        nombre = "Cita agendada",
        acciones = listOf(
            "Ver mis citas" to { navController.navigate(Rutas.MIS_CITAS) }
        )
    )
}
