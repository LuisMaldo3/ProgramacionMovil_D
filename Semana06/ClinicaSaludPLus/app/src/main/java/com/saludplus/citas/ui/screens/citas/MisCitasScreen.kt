package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.TarjetaCita

@Composable
fun MisCitasScreen(navController: NavController) {
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { BarraSuperior("Mis citas") },
        bottomBar = { BarraInferior(Rutas.MIS_CITAS, navController) }
    ) { padding ->
        if (citas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes citas agendadas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
                    TarjetaCita(
                        medico = medico?.nombre ?: "-",
                        especialidad = especialidad?.nombre ?: "-",
                        fecha = cita.fecha,
                        hora = cita.hora,
                        onClick = { navController.navigate(Rutas.detalleCita(cita.id)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}