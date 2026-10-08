package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun ConfirmarCitaScreen(navController: NavController, medicoId: Int, fecha: String, hora: String) {
    // Mostrar resumen y llamar agendarCita(); luego popUpTo(HOME).
    PantallaEnConstruccion(
        nombre = "Confirmar cita",
        acciones = listOf(
            "Confirmar" to { navController.navigate(Rutas.CITA_EXITOSA) { popUpTo(Rutas.HOME) } }
        )
    )
}
