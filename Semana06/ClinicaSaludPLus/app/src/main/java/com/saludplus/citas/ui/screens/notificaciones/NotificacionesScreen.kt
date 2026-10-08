package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.EstadoVacio
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.RellenoIconoConfirmar
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bell
import compose.icons.fontawesomeicons.solid.BellSlash

@Composable
fun NotificacionesScreen(navController: NavController) {
    val e = rememberEscala()
    val mensajes = Repositorio.citasDelUsuario().map { cita ->
        val medico = Repositorio.obtenerMedico(cita.medicoId)?.nombre ?: "tu médico"
        "Tienes cita con $medico el ${FechasEs.fechaLarga(cita.fecha)} a las ${cita.hora}."
    }
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { BarraSuperior("Notificaciones", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        if (mensajes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                EstadoVacio(
                    icono = FontAwesomeIcons.Solid.BellSlash,
                    titulo = "No tienes notificaciones",
                    detalle = "Aquí verás los recordatorios de tus citas."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = e.d(16), end = e.d(16), top = e.d(4), bottom = e.d(16)),
                verticalArrangement = Arrangement.spacedBy(e.d(12))
            ) {
                items(mensajes) { mensaje ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(e.d(16)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BordeTarjeta)
                    ) {
                        Row(
                            modifier = Modifier.padding(e.d(14)),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(e.d(44))
                                    .clip(RoundedCornerShape(e.d(12)))
                                    .background(RellenoIconoConfirmar),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = FontAwesomeIcons.Solid.Bell,
                                    contentDescription = null,
                                    tint = Azul,
                                    modifier = Modifier.size(e.d(22))
                                )
                            }
                            Spacer(Modifier.width(e.d(14)))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Recordatorio de cita",
                                    fontSize = e.s(19),
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal
                                )
                                Text(
                                    mensaje,
                                    fontSize = e.s(16),
                                    lineHeight = e.s(22),
                                    color = TextoSecundario
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
