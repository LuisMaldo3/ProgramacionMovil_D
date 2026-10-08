package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.RellenoIconoConfirmar
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TarjetaAgendarFondo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

/**
 * Fila de información del diseño (Confirmar cita): cuadro con ícono a la izquierda, la etiqueta
 * en negrita y debajo el valor en letra normal.
 */
@Composable
fun FilaInfo(
    icono: ImageVector,
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier,
    inicio: Int = 26,
    separacion: Float = 28.5f
) {
    val e = rememberEscala()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = e.d(inicio), top = e.d(2), bottom = e.d(separacion)),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = e.d(6))
                .size(e.d(44))
                .clip(RoundedCornerShape(e.d(12)))
                .background(RellenoIconoConfirmar),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Azul,
                modifier = Modifier.size(e.d(22))
            )
        }
        Spacer(Modifier.width(e.d(20)))
        Column {
            Text(
                etiqueta,
                fontSize = e.s(17),
                lineHeight = e.s(22),
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )
            Text(
                valor,
                fontSize = e.s(18),
                lineHeight = e.s(24),
                color = TextoPrincipal
            )
        }
    }
}

/** Etiqueta pequeña de estado (por ejemplo "Confirmada" o "En proceso"). */
@Composable
fun ChipEstado(texto: String, fondo: Color, colorTexto: Color, modifier: Modifier = Modifier) {
    val e = rememberEscala()
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(e.d(8)))
            .background(fondo)
            .padding(horizontal = e.d(10), vertical = e.d(4))
    ) {
        Text(texto, fontSize = e.s(15), fontWeight = FontWeight.SemiBold, color = colorTexto)
    }
}

/** Tarjeta con el médico (foto, nombre, cargo y CMP), la misma de Fecha y hora y Confirmar. */
@Composable
fun TarjetaMedicoInfo(medico: Medico, especialidad: Especialidad?, modifier: Modifier = Modifier) {
    val e = rememberEscala()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(e.d(110))
            .clip(RoundedCornerShape(e.d(16)))
            .background(RellenoTarjetaMedico)
            .padding(start = e.d(12)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FotoMedico(medico = medico, tamano = e.d(84))
        Spacer(Modifier.width(e.d(14)))
        Column {
            Text(
                medico.nombre,
                fontSize = e.s(21),
                lineHeight = e.s(26),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Text(
                cargoMedico(medico, especialidad),
                fontSize = e.s(18),
                lineHeight = e.s(22),
                color = TextoSecundario
            )
            Text(
                "CMP: ${medico.cmp}",
                fontSize = e.s(16),
                lineHeight = e.s(20),
                color = TextoSecundario
            )
        }
    }
}

/** Pantalla vacía: círculo con ícono, título, detalle y, si se pide, un botón. */
@Composable
fun EstadoVacio(
    icono: ImageVector,
    titulo: String,
    detalle: String,
    modifier: Modifier = Modifier,
    textoBoton: String? = null,
    onBoton: () -> Unit = {}
) {
    val e = rememberEscala()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = e.d(40)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(e.d(96))
                .clip(CircleShape)
                .background(TarjetaAgendarFondo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Azul,
                modifier = Modifier.size(e.d(40))
            )
        }
        Spacer(Modifier.height(e.d(16)))
        Text(
            titulo,
            fontSize = e.s(20),
            lineHeight = e.s(26),
            fontWeight = FontWeight.Bold,
            color = TextoPrincipal,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(e.d(6)))
        Text(
            detalle,
            fontSize = e.s(16),
            lineHeight = e.s(22),
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )
        if (textoBoton != null) {
            Spacer(Modifier.height(e.d(22)))
            BotonPrincipal(textoBoton, onClick = onBoton)
        }
    }
}
