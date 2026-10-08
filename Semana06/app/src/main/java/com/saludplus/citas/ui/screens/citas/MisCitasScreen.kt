package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.TarjetaCita
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs

@Composable
fun MisCitasScreen(navController: NavController) {
    val e = rememberEscala()
    val citas = Repositorio.citasDelUsuario()
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { BarraSuperior("Mis citas") },
        bottomBar = { BarraInferior(Rutas.MIS_CITAS, navController) }
    ) { padding ->
        if (citas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes citas agendadas", color = TextoSecundario, fontSize = e.s(18))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(e.d(16)),
                verticalArrangement = Arrangement.spacedBy(e.d(12))
            ) {
                items(citas, key = { it.id }) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
                    TarjetaCita(
                        medico = medico?.nombre ?: "-",
                        especialidad = especialidad?.nombre ?: "-",
                        fecha = FechasEs.fechaCorta(cita.fecha),
                        hora = cita.hora,
                        onClick = { navController.navigate(Rutas.detalleCita(cita.id)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
