package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.rememberEscala

/**
 * Barra superior blanca del diseño. Mide 96 dp desde el borde superior de la pantalla
 * (incluye la barra de estado): el título queda centrado a ≈ 72 dp.
 */
@Composable
fun BarraSuperior(
    titulo: String,
    onAtras: (() -> Unit)? = null,
    accion: (@Composable () -> Unit)? = null
) {
    val e = rememberEscala()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(e.d(96))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(e.d(48))
        ) {
            if (onAtras != null) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoPrincipal,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = e.d(16))
                        .size(e.d(30))
                        .clickable { onAtras() }
                )
            }
            Text(
                titulo,
                modifier = Modifier.align(Alignment.Center),
                fontSize = e.s(21),
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )
            if (accion != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = e.d(16))
                ) {
                    accion()
                }
            }
        }
    }
}
