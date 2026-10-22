package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.DatoGrafico

/** [ENTREGADO] Gráfico reutilizable para el Paso 11. */
@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val animacion = remember(datos) { Animatable(0f) }
    LaunchedEffect(datos) {
        animacion.snapTo(0f)
        animacion.animateTo(1f, animationSpec = tween(durationMillis = 850))
    }
    val progreso = animacion.value
    Canvas(modifier = modifier.fillMaxWidth().height(150.dp)) {
        val mayor = (datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
        if (datos.isNotEmpty()) {
            val espacio = size.width / datos.size
            datos.forEachIndexed { index, dato ->
                val alto = (dato.valor / mayor) * size.height * 0.86f * progreso
                val ancho = espacio * 0.55f
                drawRoundRect(
                    color = if (index == 0) Color(0xFF1D4ED8) else Color(0xFF93C5FD),
                    topLeft = Offset(index * espacio + (espacio - ancho) / 2, size.height - alto),
                    size = Size(ancho, alto), cornerRadius = CornerRadius(9f, 9f)
                )
            }
        }
    }
}
