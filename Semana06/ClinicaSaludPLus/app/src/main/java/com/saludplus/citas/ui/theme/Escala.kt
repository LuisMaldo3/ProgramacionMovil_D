package com.saludplus.citas.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TAMAÑO GENERAL DE LA APP.
 * 1.0  = las proporciones exactas de la imagen del diseño (se ve grande en el teléfono).
 * 0.85 = más compacto (valor actual). Si todavía se ve grande, baja a 0.8 o 0.75;
 *        si se ve pequeño, sube a 0.9 o 1.0.
 */
private const val FACTOR_DISENO = 0.85f

/**
 * Las medidas del diseño están dadas para una pantalla de 360 x 780 dp.
 * d(valor) y s(valor) las escalan al tamaño real del teléfono (se toma el más chico entre
 * lo que permite el ancho y lo que permite el alto) para que la pantalla siempre quepa
 * y las proporciones se vean igual en cualquier teléfono.
 * El texto no crece con el "tamaño de fuente" del sistema, para que el diseño no se desarme.
 */
class Escala(val k: Float, private val fuente: Float) {
    fun d(valor: Number): Dp = (valor.toFloat() * k).dp
    fun s(valor: Number): TextUnit = (valor.toFloat() * k / fuente).sp
}

@Composable
fun rememberEscala(): Escala {
    val configuracion = LocalConfiguration.current
    val fuente = LocalDensity.current.fontScale
    val ancho = configuracion.screenWidthDp
    val alto = configuracion.screenHeightDp
    return remember(ancho, alto, fuente) {
        val porAncho = ancho / 360f
        val porAlto = alto / 780f
        Escala(minOf(porAncho, porAlto, 1.15f) * FACTOR_DISENO, if (fuente < 1f) 1f else fuente)
    }
}
