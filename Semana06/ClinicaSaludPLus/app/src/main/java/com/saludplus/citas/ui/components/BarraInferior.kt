package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas

@Composable
fun BarraInferior(rutaActual: String, navController: NavController) {
    val destinos = listOf(
        Triple(Rutas.HOME, "Inicio", Icons.Filled.Home),
        Triple(Rutas.MIS_CITAS, "Citas", Icons.Filled.Event),
        Triple(Rutas.RESULTADOS, "Resultados", Icons.Filled.Description),
        Triple(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
    )

    NavigationBar {
        destinos.forEach { (ruta, etiqueta, icono) ->
            NavigationBarItem(
                selected = rutaActual == ruta,
                onClick = {
                    if (rutaActual != ruta) {
                        navController.navigate(ruta) {
                            popUpTo(Rutas.HOME)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(icono, contentDescription = etiqueta) },
                label = { Text(etiqueta) }
            )
        }
    }
}