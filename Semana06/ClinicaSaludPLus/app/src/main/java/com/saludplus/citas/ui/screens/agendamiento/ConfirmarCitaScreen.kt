package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaDato

@Composable
fun ConfirmarCitaScreen(
    navController: NavController,
    medicoId: Int,
    fecha: String,
    hora: String
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { BarraSuperior("Confirmar cita", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Revisa los datos de tu cita", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDato("Médico", medico?.nombre ?: "-")
                    FilaDato("Especialidad", especialidad?.nombre ?: "-")
                    FilaDato("Fecha", fecha)
                    FilaDato("Hora", hora)
                }
            }

            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            BotonPrincipal("Confirmar cita", onClick = {
                if (Repositorio.agendarCita(medicoId, fecha, hora)) {
                    navController.navigate(Rutas.CITA_EXITOSA) {
                        popUpTo(Rutas.HOME)
                    }
                } else {
                    error = "No se pudo agendar: el horario ya fue ocupado o no hay sesión iniciada"
                }
            })
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar horario")
            }
        }
    }
}