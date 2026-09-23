package com.maldonado.ejerciciob

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RutinasScreen() {
    // Datos de demostración para presentar las categorías de entrenamiento.
    val rutinas = listOf(
        "Movilidad" to
                "Movilidad de hombros, cadera y tobillos.",

        "Fuerza" to
                "Ejercicios de fuerza y control corporal.",

        "Resistencia" to
                "Actividades de bicicleta y entrenamiento cardiovascular."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 24.dp,
            top = 12.dp,
            end = 24.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cada elemento muestra el nombre y la descripción de una rutina.
        items(
            items = rutinas,
            key = { it.first }
        ) { (nombre, descripcion) ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FondoTarjetaFit,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoFit
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = descripcion,
                        fontSize = 13.sp,
                        color = TextoSecundarioFit
                    )
                }
            }
        }
    }
}