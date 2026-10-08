package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun EspecialidadesScreen(navController: NavController) {
    //LazyColumn + búsqueda con buscarEspecialidades().
    PantallaEnConstruccion(
        nombre = "Especialidades",
        acciones = listOf(
            "Elegir especialidad 1" to { navController.navigate(Rutas.medicos(1)) }
        )
    )
}
