package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    //LazyVerticalGrid con horariosDisponibles(medicoId, fecha) y selección.
    PantallaEnConstruccion(
        nombre = "Fecha y hora",
        acciones = listOf(
            "Elegir fecha y hora de ejemplo" to { navController.navigate(Rutas.confirmar(medicoId, "2026-10-15", "09:00")) }
        )
    )
}
