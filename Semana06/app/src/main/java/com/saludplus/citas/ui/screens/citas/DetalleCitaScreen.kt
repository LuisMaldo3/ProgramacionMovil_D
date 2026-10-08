package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs

@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    val e = rememberEscala()
    val cita = Repositorio.obtenerCita(citaId)
    var mostrarDialogo by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { BarraSuperior("Detalle de cita", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding()
                .padding(e.d(16))
        ) {
            if (cita == null) {
                Text("No se encontró la cita", color = TextoSecundario, fontSize = e.s(18))
            } else {
                val medico = Repositorio.obtenerMedico(cita.medicoId)
                val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(e.d(16)))
                        .background(RellenoTarjetaMedico)
                        .padding(e.d(16))
                ) {
                    FilaDato("Médico", medico?.nombre ?: "-")
                    FilaDato("Especialidad", especialidad?.nombre ?: "-")
                    FilaDato("Fecha", FechasEs.fechaLarga(cita.fecha))
                    FilaDato("Hora", FechasEs.rangoHora(cita.hora))
                }
                Spacer(Modifier.height(e.d(24)))
                Button(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(e.d(62)),
                    shape = RoundedCornerShape(e.d(14)),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRojo)
                ) {
                    Text("Cancelar cita", fontSize = e.s(20), fontWeight = FontWeight.SemiBold)
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
