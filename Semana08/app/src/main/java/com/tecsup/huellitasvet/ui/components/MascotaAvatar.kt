package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie

/** [ENTREGADO] Avatar vectorial hecho con Compose Canvas: no usa XML ni imágenes externas. */
@Composable
fun MascotaAvatar(especie: Especie, tamano: Dp = 68.dp, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(tamano)) {
        val x = size.width / 2f
        val y = size.height / 2f
        val radio = size.minDimension * 0.31f
        val colorPiel = when (especie) {
            Especie.PERRO -> Color(0xFFE9B778)
            Especie.GATO -> Color(0xFFBFCAD3)
            Especie.OTRO -> Color(0xFFA9DCC6)
        }
        drawCircle(color = Color(0xFFE8F5EE), radius = size.minDimension * 0.49f)
        if (especie == Especie.GATO) {
            val izq = Path().apply {
                moveTo(x - radio * 0.88f, y - radio * 0.33f)
                lineTo(x - radio * 0.9f, y - radio * 1.5f)
                lineTo(x - radio * 0.09f, y - radio * 0.91f)
                close()
            }
            val der = Path().apply {
                moveTo(x + radio * 0.88f, y - radio * 0.33f)
                lineTo(x + radio * 0.9f, y - radio * 1.5f)
                lineTo(x + radio * 0.09f, y - radio * 0.91f)
                close()
            }
            drawPath(izq, colorPiel)
            drawPath(der, colorPiel)
        } else if (especie == Especie.PERRO) {
            drawCircle(colorPiel.copy(alpha = 0.85f), radius = radio * 0.65f, center = Offset(x - radio * 0.90f, y - radio * 0.35f))
            drawCircle(colorPiel.copy(alpha = 0.85f), radius = radio * 0.65f, center = Offset(x + radio * 0.90f, y - radio * 0.35f))
        }
        drawCircle(colorPiel, radius = radio, center = Offset(x,y))
        drawCircle(Color(0xFF334155), radius = radio * 0.095f, center = Offset(x - radio * 0.37f, y - radio * 0.08f))
        drawCircle(Color(0xFF334155), radius = radio * 0.095f, center = Offset(x + radio * 0.37f, y - radio * 0.08f))
        drawCircle(Color(0xFF475569), radius = radio * 0.13f, center = Offset(x, y + radio * 0.32f))
        drawCircle(Color.White, radius = radio * 0.065f, center = Offset(x - radio * 0.05f, y + radio * 0.27f))
    }
}
