package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.PuntoPeso
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalTextApi::class)
@Composable
fun GraficoLineas(
    puntos: List<PuntoPeso>,
    modifier: Modifier = Modifier
) {
    val colorPrimario = MaterialTheme.colorScheme.primary
    val colorTexto = MaterialTheme.colorScheme.onSurface
    val colorSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    val colorGrid = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val textMeasurer = rememberTextMeasurer()

    val ultimoPeso = puntos.lastOrNull()?.peso

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ShowChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Text(
                        text = "Evolución del peso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorTexto
                    )
                }

                if (ultimoPeso != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "${"%.1f".format(Locale.US, ultimoPeso)} kg actual",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            if (puntos.size >= 2) {
                val primero = puntos.first().peso
                val ultimo = puntos.last().peso
                val cambio = ultimo - primero
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .5f)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Primera medición", style = MaterialTheme.typography.labelMedium,
                                color = colorSecundario)
                            Text("${"%.1f".format(Locale.US, primero)} kg",
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                                color = colorTexto)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Cambio registrado", style = MaterialTheme.typography.labelMedium,
                                color = colorSecundario)
                            val prefijo = if (cambio > 0) "+" else ""
                            Text("$prefijo${"%.1f".format(Locale.US, cambio)} kg",
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                                color = colorPrimario)
                        }
                    }
                }
            }

            if (puntos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Todavía no hay registros de peso.\nLas mediciones aparecerán después de cada atención.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorSecundario,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    val paddingLeft = 48.dp.toPx()
                    val paddingRight = 24.dp.toPx()
                    val paddingTop = 28.dp.toPx()
                    val paddingBottom = 32.dp.toPx()

                    val graphWidth = size.width - paddingLeft - paddingRight
                    val graphHeight = size.height - paddingTop - paddingBottom

                    if (puntos.size == 1) {
                        val p = puntos.first()
                        val cx = paddingLeft + graphWidth / 2f
                        val cy = paddingTop + graphHeight / 2f

                        drawLine(
                            color = colorGrid,
                            start = Offset(paddingLeft, cy),
                            end = Offset(size.width - paddingRight, cy),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )

                        drawCircle(color = colorPrimario, radius = 8.dp.toPx(), center = Offset(cx, cy))
                        drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(cx, cy))

                        val pesoText = "${"%.1f".format(Locale.US, p.peso)} kg"
                        val pesoLayout = textMeasurer.measure(
                            text = AnnotatedString(pesoText),
                            style = TextStyle(color = colorTexto, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        )
                        drawText(
                            textLayoutResult = pesoLayout,
                            topLeft = Offset(cx - pesoLayout.size.width / 2f, cy - 26.dp.toPx())
                        )

                        val fechaCorta = formatoFecha(p.fecha)
                        val fechaLayout = textMeasurer.measure(
                            text = AnnotatedString(fechaCorta),
                            style = TextStyle(color = colorSecundario, fontSize = 13.sp)
                        )
                        drawText(
                            textLayoutResult = fechaLayout,
                            topLeft = Offset(cx - fechaLayout.size.width / 2f, cy + 12.dp.toPx())
                        )
                    } else {
                        val minVal = puntos.minOf { it.peso }
                        val maxVal = puntos.maxOf { it.peso }
                        val rango = if (maxVal == minVal) 1.0 else maxVal - minVal
                        val minY = (minVal - rango * 0.15).coerceAtLeast(0.0)
                        val maxY = maxVal + rango * 0.15
                        val deltaY = if (maxY == minY) 1.0 else maxY - minY

                        // Grid lines (3 horizontal lines)
                        val gridSteps = 3
                        for (i in 0 until gridSteps) {
                            val fraction = i.toFloat() / (gridSteps - 1)
                            val y = paddingTop + graphHeight * (1f - fraction)
                            val valor = minY + (deltaY * fraction)

                            drawLine(
                                color = colorGrid,
                                start = Offset(paddingLeft, y),
                                end = Offset(size.width - paddingRight, y),
                                strokeWidth = 1.dp.toPx()
                            )

                            val yText = "%.1f".format(Locale.US, valor)
                            val yLayout = textMeasurer.measure(
                                text = AnnotatedString(yText),
                                style = TextStyle(color = colorSecundario, fontSize = 13.sp)
                            )
                            drawText(
                                textLayoutResult = yLayout,
                                topLeft = Offset(paddingLeft - yLayout.size.width - 6.dp.toPx(), y - yLayout.size.height / 2f)
                            )
                        }

                        val stepX = graphWidth / (puntos.size - 1)
                        val offsets = puntos.mapIndexed { index, p ->
                            val x = paddingLeft + index * stepX
                            val normalizedY = ((p.peso - minY) / deltaY).toFloat()
                            val y = paddingTop + graphHeight * (1f - normalizedY)
                            Offset(x, y)
                        }

                        // Gradient fill under path
                        val fillPath = Path().apply {
                            moveTo(offsets.first().x, size.height - paddingBottom)
                            offsets.forEach { lineTo(it.x, it.y) }
                            lineTo(offsets.last().x, size.height - paddingBottom)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(colorPrimario.copy(alpha = 0.35f), colorPrimario.copy(alpha = 0.02f)),
                                startY = paddingTop,
                                endY = size.height - paddingBottom
                            )
                        )

                        // Line connecting points
                        val linePath = Path().apply {
                            offsets.forEachIndexed { i, offset ->
                                if (i == 0) moveTo(offset.x, offset.y) else lineTo(offset.x, offset.y)
                            }
                        }

                        drawPath(
                            path = linePath,
                            color = colorPrimario,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )

                        // Points & Labels
                        offsets.forEachIndexed { i, offset ->
                            val p = puntos[i]

                            drawCircle(color = colorPrimario, radius = 6.dp.toPx(), center = offset)
                            drawCircle(color = Color.White, radius = 3.dp.toPx(), center = offset)

                            // Evita superposición en series extensas y prioriza la última medición.
                            val mostrarEtiqueta = puntos.size <= 5 || i == puntos.lastIndex
                            if (mostrarEtiqueta) {
                            val valText = "%.1f".format(Locale.US, p.peso)
                            val valLayout = textMeasurer.measure(
                                text = AnnotatedString(valText),
                                style = TextStyle(color = colorTexto, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            )
                            drawText(
                                textLayoutResult = valLayout,
                                topLeft = Offset(offset.x - valLayout.size.width / 2f, offset.y - 18.dp.toPx())
                            )

                            }
                            // Muestra un número legible de fechas, incluso con muchos registros.
                            val salto = ((puntos.size + 2) / 4).coerceAtLeast(1)
                            if (i == 0 || i == puntos.lastIndex || i % salto == 0) {
                            val fechaCorta = formatoFecha(p.fecha)
                            val fechaLayout = textMeasurer.measure(
                                text = AnnotatedString(fechaCorta),
                                style = TextStyle(color = colorSecundario, fontSize = 13.sp)
                            )
                            drawText(
                                textLayoutResult = fechaLayout,
                                topLeft = Offset(offset.x - fechaLayout.size.width / 2f, size.height - paddingBottom + 8.dp.toPx())
                            )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatoFecha(fechaStr: String): String = runCatching {
    LocalDate.parse(fechaStr)
        .format(DateTimeFormatter.ofPattern("dd/MM", Locale("es", "PE")))
}.getOrDefault(fechaStr)
