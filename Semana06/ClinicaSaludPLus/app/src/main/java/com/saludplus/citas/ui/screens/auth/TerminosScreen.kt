package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun TerminosScreen(navController: NavController) {
    //Scroll o AlertDialog (reto extra).
    PantallaEnConstruccion(
        nombre = "Términos y condiciones",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}
