package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.Divisor
import com.saludplus.citas.ui.theme.GrisClaro
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun BarraInferior(rutaActual: String, navController: NavController) {
    val e = rememberEscala()
    val destinos = listOf(
        Triple(Rutas.HOME, "Inicio", Icons.Filled.Home),
        Triple(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
        Triple(Rutas.RESULTADOS, "Resultados", Icons.Filled.Description),
        Triple(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(color = Divisor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(e.d(72))
        ) {
            destinos.forEach { (ruta, etiqueta, icono) ->
                val activo = rutaActual == ruta
                val color = if (activo) Azul else GrisClaro
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            if (rutaActual != ruta) {
                                navController.navigate(ruta) {
                                    popUpTo(Rutas.HOME)
                                    launchSingleTop = true
                                }
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Icon(
                        icono,
                        contentDescription = etiqueta,
                        tint = color,
                        modifier = Modifier.size(e.d(32))
                    )
                    Text(
                        etiqueta,
                        fontSize = e.s(16),
                        color = color,
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
