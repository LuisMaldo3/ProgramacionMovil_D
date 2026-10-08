package com.saludplus.citas.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun HomeScreen(navController: NavController) {
    //Scaffold, NavigationBar (Inicio, Citas, Resultados, Perfil) y LazyRow de destacadas.
    PantallaEnConstruccion(
        nombre = "Inicio",
        acciones = listOf(
            "Agendar cita" to { navController.navigate(Rutas.ESPECIALIDADES) },
            "Mis citas" to { navController.navigate(Rutas.MIS_CITAS) },
            "Perfil" to { navController.navigate(Rutas.PERFIL) },
            "Resultados" to { navController.navigate(Rutas.RESULTADOS) },
            "Notificaciones" to { navController.navigate(Rutas.NOTIFICACIONES) }
        )
    )
}
