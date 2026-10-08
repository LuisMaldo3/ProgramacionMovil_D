package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun PerfilScreen(navController: NavController) {
    val e = rememberEscala()
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size
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
                .padding(horizontal = e.d(17), vertical = e.d(16)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = Azul,
                modifier = Modifier.size(e.d(96))
            )
            Spacer(Modifier.height(e.d(8)))
            Text(
                usuario?.nombre ?: "Sin sesión",
                fontSize = e.s(24),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(e.d(16)))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(e.d(16)))
                    .background(RellenoTarjetaMedico)
                    .padding(e.d(16))
            ) {
                FilaDato("Correo", usuario?.correo?.ifBlank { "-" } ?: "-")
                FilaDato("Teléfono", usuario?.telefono?.ifBlank { "-" } ?: "-")
                FilaDato("Citas agendadas", totalCitas.toString())
            }
            Spacer(Modifier.height(e.d(24)))
            Button(
                onClick = {
                    Repositorio.cerrarSesion()
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(e.d(62)),
                shape = RoundedCornerShape(e.d(14)),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRojo)
            ) {
                Text("Cerrar sesión", fontSize = e.s(20), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
