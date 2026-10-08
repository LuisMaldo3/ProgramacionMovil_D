package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.TarjetaAgendarFondo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.CalendarCheck
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.PhoneVolume
import compose.icons.fontawesomeicons.solid.SignOutAlt

@Composable
fun PerfilScreen(navController: NavController) {
    val e = rememberEscala()
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size
    val nombre = usuario?.nombre?.trim().orEmpty()
    val inicial = nombre.firstOrNull()?.uppercase() ?: "?"

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { BarraSuperior("Mis datos") },
        bottomBar = { BarraInferior(Rutas.PERFIL, navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(e.d(8)))
                Box(
                    modifier = Modifier
                        .size(e.d(96))
                        .clip(CircleShape)
                        .background(TarjetaAgendarFondo),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        inicial,
                        fontSize = e.s(42),
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                }
                Spacer(Modifier.height(e.d(12)))
                Text(
                    if (nombre.isEmpty()) "Sin sesión" else nombre,
                    fontSize = e.s(24),
                    lineHeight = e.s(30),
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Text("Paciente", fontSize = e.s(17), color = TextoSecundario)
                Spacer(Modifier.height(e.d(22)))

                Column(modifier = Modifier.fillMaxWidth()) {
                    FilaInfo(
                        FontAwesomeIcons.Solid.Envelope, "Correo",
                        usuario?.correo?.ifBlank { "No registrado" } ?: "No registrado"
                    )
                    FilaInfo(
                        FontAwesomeIcons.Solid.PhoneVolume, "Teléfono",
                        usuario?.telefono?.ifBlank { "No registrado" } ?: "No registrado"
                    )
                    FilaInfo(
                        FontAwesomeIcons.Solid.CalendarCheck, "Citas agendadas",
                        totalCitas.toString()
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    Repositorio.cerrarSesion()
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = e.d(17), end = e.d(17), top = e.d(8), bottom = e.d(16))
                    .height(e.d(62)),
                shape = RoundedCornerShape(e.d(14)),
                border = BorderStroke(1.dp, ErrorRojo),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRojo)
            ) {
                Icon(
                    FontAwesomeIcons.Solid.SignOutAlt,
                    contentDescription = null,
                    modifier = Modifier.size(e.d(20))
                )
                Spacer(Modifier.width(e.d(10)))
                Text("Cerrar sesión", fontSize = e.s(20), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
