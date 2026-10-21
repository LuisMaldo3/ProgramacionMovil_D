package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.DatoGrafico

/** [ENTREGADO] Gráfico reutilizable para el Paso 11. */
@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val colores = listOf(Color(0xFFD97706), Color(0xFF16A34A), Color(0xFF475569), Color(0xFFDC2626))
    Canvas(modifier = modifier.size(170.dp)) {
        val total = datos.sumOf { it.valor.toDouble() }.toFloat()
        if (total <= 0f) {
            drawArc(Color.LightGray, 0f, 360f, false, style = Stroke(24f))
        } else {
            var inicio = -90f
            datos.forEachIndexed { indice, dato ->
                val angulo = 360f * (dato.valor / total)
                drawArc(colores[indice % colores.size], inicio, angulo, false, style = Stroke(24f))
                inicio += angulo
            }
        }
    }
}
