package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun MedicosScreen(navController: NavController, especialidadId: Int) {
    //Listar medicosPorEspecialidad(especialidadId) con filter + sortedByDescending.
    PantallaEnConstruccion(
        nombre = "Médicos",
        acciones = listOf(
            "Elegir médico 1" to { navController.navigate(Rutas.fechaHora(1)) }
        )
    )
}
