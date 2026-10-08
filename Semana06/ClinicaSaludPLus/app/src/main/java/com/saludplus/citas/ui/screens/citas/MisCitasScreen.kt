package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun MisCitasScreen(navController: NavController) {
    //LazyColumn con citasDelUsuario() y estado de lista vacía.
    PantallaEnConstruccion(
        nombre = "Mis citas",
        acciones = listOf(
            "Detalle de la cita 1" to { navController.navigate(Rutas.detalleCita(1)) }
        )
    )
}
