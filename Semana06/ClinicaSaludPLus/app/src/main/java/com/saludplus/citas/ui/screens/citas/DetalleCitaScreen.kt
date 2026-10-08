package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaDato

@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    val cita = Repositorio.obtenerCita(citaId)
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { BarraSuperior("Detalle de cita", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (cita == null) {
                Text("No se encontró la cita")
            } else {
                val medico = Repositorio.obtenerMedico(cita.medicoId)
                val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FilaDato("Médico", medico?.nombre ?: "-")
                        FilaDato("Especialidad", especialidad?.nombre ?: "-")
                        FilaDato("Fecha", cita.fecha)
                        FilaDato("Hora", cita.hora)
                    }
                }

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar cita")
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar esta cita?") },
            confirmButton = {
                TextButton(onClick = {
                    Repositorio.cancelarCita(citaId)
                    mostrarDialogo = false
                    navController.popBackStack()
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No")
                }
            }
        )
    }
}