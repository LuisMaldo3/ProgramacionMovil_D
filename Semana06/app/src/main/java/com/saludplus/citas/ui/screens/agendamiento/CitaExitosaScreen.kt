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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TarjetaCitasFondo
import com.saludplus.citas.ui.theme.TarjetaCitasTexto
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs

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
            .navigationBarsPadding()
            .padding(horizontal = e.d(17)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(e.d(90)))
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
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(e.d(24)))
        if (cita != null) {
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
        }
        Spacer(Modifier.weight(1f))
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
        Spacer(Modifier.height(e.d(20)))
    }
}
