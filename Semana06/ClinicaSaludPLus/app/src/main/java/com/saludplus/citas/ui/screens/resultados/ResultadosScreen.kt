package com.saludplus.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun ResultadosScreen(navController: NavController) {
    //Modelo propio y lista fija (reto extra).
    PantallaEnConstruccion(
        nombre = "Resultados",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}
