package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.TarjetaMedicoInfo
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.TarjetaCitasFondo
import com.saludplus.citas.ui.theme.TarjetaCitasTexto
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Building
import compose.icons.fontawesomeicons.solid.CalendarAlt
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.MapMarkerAlt

@Composable
fun CitaExitosaScreen(navController: NavController) {
    val e = rememberEscala()
    val cita = Repositorio.ultimaCita
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(e.d(36)))
            Box(
                modifier = Modifier
                    .size(e.d(120))
                    .clip(CircleShape)
                    .background(TarjetaCitasFondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = TarjetaCitasTexto,
                    modifier = Modifier.size(e.d(80))
                )
            }
            Spacer(Modifier.height(e.d(16)))
            Text(
                "¡Cita agendada!",
                fontSize = e.s(28),
                lineHeight = e.s(34),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                "Te esperamos en la clínica",
                fontSize = e.s(18),
                lineHeight = e.s(22),
                color = TextoSecundario
            )
            Spacer(Modifier.height(e.d(22)))
            if (cita != null && medico != null) {
                val local = Repositorio.obtenerLocal(cita.localId)
                TarjetaMedicoInfo(
                    medico = medico,
                    especialidad = especialidad,
                    modifier = Modifier.padding(horizontal = e.d(16))
                )
                Spacer(Modifier.height(e.d(12)))
                FilaInfo(
                    FontAwesomeIcons.Solid.Building, "Sede / Local", "Local ${local?.nombre ?: "-"}",
                    separacion = 14f
                )
                FilaInfo(
                    FontAwesomeIcons.Solid.MapMarkerAlt, "Dirección", local?.direccion ?: "-",
                    separacion = 14f
                )
                FilaInfo(
                    FontAwesomeIcons.Solid.CalendarAlt, "Fecha", FechasEs.fechaLarga(cita.fecha),
                    separacion = 14f
                )
                FilaInfo(
                    FontAwesomeIcons.Solid.Clock, "Hora", FechasEs.rangoHora(cita.hora),
                    separacion = 14f
                )
            }
        }

        Column(modifier = Modifier.padding(start = e.d(17), end = e.d(17), top = e.d(8), bottom = e.d(20))) {
            BotonPrincipal("Ver mis citas", onClick = {
                navController.navigate(Rutas.MIS_CITAS) {
                    popUpTo(Rutas.HOME)
                }
            })
            Spacer(Modifier.height(e.d(10)))
            OutlinedButton(
                onClick = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(e.d(62)),
                shape = RoundedCornerShape(e.d(14)),
                border = BorderStroke(1.dp, Azul),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Azul)
            ) {
                Text("Volver al inicio", fontSize = e.s(20), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
