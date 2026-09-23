package com.maldonado.ejerciciob

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(
    usuario: UsuarioFit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        // Mostramos las iniciales dentro del avatar circular.
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = VerdeClaroFit,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = usuario.iniciales,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeFit
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = usuario.nombre,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = TextoFit
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = usuario.plan,
            fontSize = 14.sp,
            color = TextoSecundarioFit
        )

        Spacer(Modifier.height(28.dp))

        // Ambas tarjetas ocupan el mismo ancho.
        // Las estadísticas provienen del usuario de demostración.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TarjetaEstadistica(
                cantidad = usuario.clasesTomadas,
                etiqueta = "Clases",
                modifier = Modifier.weight(1f)
            )

            TarjetaEstadistica(
                cantidad = usuario.rachaAsistencia,
                etiqueta = "Rachas",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Compartimos el diseño para mantener iguales las dos estadísticas.
@Composable
private fun TarjetaEstadistica(
    cantidad: Int,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = FondoTarjetaFit,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cantidad.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextoFit
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = etiqueta,
                fontSize = 13.sp,
                color = TextoSecundarioFit
            )
        }
    }
}