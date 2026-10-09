package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.ChipEstado
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.TarjetaMedicoInfo
import com.saludplus.citas.ui.theme.ChipDisponibleFondo
import com.saludplus.citas.ui.theme.ChipDisponibleTexto
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Building
import compose.icons.fontawesomeicons.solid.CalendarAlt
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.MapMarkerAlt
import compose.icons.fontawesomeicons.solid.TimesCircle

@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    val e = rememberEscala()
    val cita = Repositorio.obtenerCita(citaId)
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior("Detalle de cita", onAtras = { navController.popBackStack() })

        if (cita == null) {
            Text(
                "No se encontró la cita",
                modifier = Modifier.padding(e.d(20)),
                color = TextoSecundario,
                fontSize = e.s(18)
            )
        } else {
            val medico = Repositorio.obtenerMedico(cita.medicoId)
            val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
            val local = Repositorio.obtenerLocal(cita.localId)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = e.d(4))
            ) {
                if (medico != null) {
                    TarjetaMedicoInfo(
                        medico = medico,
                        especialidad = especialidad,
                        modifier = Modifier.padding(horizontal = e.d(16))
                    )
                }
                Spacer(Modifier.height(e.d(12)))
                ChipEstado(
                    texto = "Cita confirmada",
                    fondo = ChipDisponibleFondo,
                    colorTexto = ChipDisponibleTexto,
                    modifier = Modifier.padding(start = e.d(26))
                )
                Spacer(Modifier.height(e.d(10)))
                FilaInfo(FontAwesomeIcons.Solid.Building, "Sede / Local", "Local ${local?.nombre ?: "-"}")
                FilaInfo(FontAwesomeIcons.Solid.MapMarkerAlt, "Dirección", local?.direccion ?: "-")
                FilaInfo(FontAwesomeIcons.Solid.CalendarAlt, "Fecha", FechasEs.fechaLarga(cita.fecha))
                FilaInfo(FontAwesomeIcons.Solid.Clock, "Hora", FechasEs.rangoHora(cita.hora))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = e.d(17), end = e.d(17), top = e.d(8), bottom = e.d(20))
            ) {
                OutlinedButton(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(e.d(62)),
                    shape = RoundedCornerShape(e.d(14)),
                    border = BorderStroke(1.dp, ErrorRojo),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRojo)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.TimesCircle,
                        contentDescription = null,
                        modifier = Modifier.size(e.d(20))
                    )
                    Spacer(Modifier.width(e.d(10)))
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
                    Text("Sí, cancelar", color = ErrorRojo)
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
