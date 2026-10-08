package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaDato

@Composable
fun CitaExitosaScreen(navController: NavController) {
    val cita = Repositorio.ultimaCita
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(88.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "¡Cita agendada!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        if (cita != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDato("Médico", medico?.nombre ?: "-")
                    FilaDato("Especialidad", especialidad?.nombre ?: "-")
                    FilaDato("Fecha", cita.fecha)
                    FilaDato("Hora", cita.hora)
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        BotonPrincipal("Ver mis citas", onClick = {
            navController.navigate(Rutas.MIS_CITAS) {
                popUpTo(Rutas.HOME)
            }
        })
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                navController.navigate(Rutas.HOME) {
                    popUpTo(Rutas.HOME) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}