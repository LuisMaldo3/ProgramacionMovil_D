
package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.EstadoCita
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Componente original del laboratorio
@Composable
fun CitaCard(
    titulo: String,
    detalle: String,
    estado: EstadoCita,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                detalle,
                style = MaterialTheme.typography.bodyMedium
            )

            EstadoCitaChip(estado)
        }
    }
}

// Tarjeta del Paso 10
@Composable
fun CitaCard(
    cita: CitaConDetalle,
    mostrarCancelar: Boolean,
    cancelando: Boolean,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val verde = Color(0xFF059669)
    val texto = Color(0xFF1F2937)
    val subtitulo = Color(0xFF818B98)

    val nombreServicio = cita.servicio.nombre

    val icono: ImageVector = when {
        nombreServicio.contains(
            "vacun",
            ignoreCase = true
        ) -> Icons.Filled.Vaccines

        nombreServicio.contains(
            "baño",
            ignoreCase = true
        ) || nombreServicio.contains(
            "corte",
            ignoreCase = true
        ) -> Icons.Filled.ContentCut

        else -> Icons.Filled.Favorite
    }

    val fechaCorta = runCatching {
        LocalDate.parse(cita.cita.fecha)
            .format(
                DateTimeFormatter.ofPattern(
                    "EEE dd",
                    Locale("es", "PE")
                )
            ).replaceFirstChar {
                it.titlecase(Locale("es", "PE"))
            }
    }.getOrDefault(cita.cita.fecha)

    val estado = runCatching {
        EstadoCita.valueOf(cita.cita.estado)
    }.getOrDefault(EstadoCita.PENDIENTE)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFE2E8E7)
        ),
        shadowElevation = 0.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
        ) {

            Row(
                modifier = Modifier.padding(
                    start = 14.dp,
                    top = 13.dp,
                    end = 14.dp,
                    bottom = 14.dp
                ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                // Icono del servicio
                Surface(
                    modifier = Modifier.size(47.dp),
                    shape = RoundedCornerShape(13.dp),
                    color = Color(0xFFD1FAE5)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = verde,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Información
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(5.dp)
                ) {

                    Text(
                        text = nombreServicio,
                        fontWeight = FontWeight.Bold,
                        color = texto,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${cita.mascota.mascota.nombre} · " +
                                "$fechaCorta · ${cita.cita.hora}",
                        color = subtitulo,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(2.dp))

                    EstadoCitaChip(estado)
                }
            }

            // Cancelar
            if (mostrarCancelar) {

                TextButton(
                    onClick = onCancelar,
                    enabled = !cancelando,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 8.dp,
                            bottom = 5.dp
                        ),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 3.dp
                    )
                ) {

                    Text(
                        text = if (cancelando) {
                            "Cancelando..."
                        } else {
                            "Cancelar"
                        },
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
