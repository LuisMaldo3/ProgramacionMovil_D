package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CampoBusqueda
import com.saludplus.citas.ui.components.TarjetaEspecialidad
import com.saludplus.citas.ui.theme.Divisor
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun EspecialidadesScreen(navController: NavController) {
    val e = rememberEscala()
    var busqueda by remember { mutableStateOf("") }
    // Se recalcula sola cada vez que cambia el texto
    val resultados = Repositorio.buscarEspecialidades(busqueda.trim())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior("Especialidades", onAtras = { navController.popBackStack() })
        Spacer(Modifier.height(e.d(4)))
        CampoBusqueda(
            valor = busqueda,
            onCambio = { busqueda = it },
            placeholder = "Buscar especialidad...",
            modifier = Modifier.padding(horizontal = e.d(16))
        )
        Spacer(Modifier.height(e.d(12)))

        if (resultados.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text(
                    "No se encontraron especialidades",
                    modifier = Modifier.padding(top = e.d(40)),
                    color = TextoSecundario,
                    fontSize = e.s(18)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(resultados, key = { it.id }) { especialidad ->
                    Column {
                        TarjetaEspecialidad(
                            especialidad = especialidad,
                            onClick = { navController.navigate(Rutas.medicos(especialidad.id)) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = e.d(97)),
                            color = Divisor
                        )
                    }
                }
            }
        }
    }
}
