package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.ChipEstado
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.ChipDisponibleFondo
import com.saludplus.citas.ui.theme.ChipDisponibleTexto
import com.saludplus.citas.ui.theme.RellenoIconoConfirmar
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Heartbeat
import compose.icons.fontawesomeicons.solid.Vial
import compose.icons.fontawesomeicons.solid.XRay

// Modelo propio de esta pantalla (lista fija).
private data class ResultadoMedico(
    val id: Int,
    val examen: String,
    val fecha: String,
    val estado: String,
    val icono: ImageVector
)

private val resultadosFijos = listOf(
    ResultadoMedico(1, "Hemograma completo", "2026-09-28", "Disponible", FontAwesomeIcons.Solid.Vial),
    ResultadoMedico(2, "Perfil lipídico", "2026-09-30", "Disponible", FontAwesomeIcons.Solid.Vial),
    ResultadoMedico(3, "Radiografía de tórax", "2026-10-02", "En proceso", FontAwesomeIcons.Solid.XRay),
    ResultadoMedico(4, "Electrocardiograma", "2026-10-05", "En proceso", FontAwesomeIcons.Solid.Heartbeat)
)

private val ProcesoFondo = Color(0xFFFFF1DC)
private val ProcesoTexto = Color(0xFFC2650A)

@Composable
fun ResultadosScreen(navController: NavController) {
    val e = rememberEscala()
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = { BarraSuperior("Resultados") },
        bottomBar = { BarraInferior(Rutas.RESULTADOS, navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = e.d(16), end = e.d(16), top = e.d(4), bottom = e.d(16)),
            verticalArrangement = Arrangement.spacedBy(e.d(12))
        ) {
            items(resultadosFijos, key = { it.id }) { r ->
                val disponible = r.estado == "Disponible"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(e.d(16)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BordeTarjeta)
                ) {
                    Row(
                        modifier = Modifier.padding(e.d(14)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(e.d(48))
                                .clip(RoundedCornerShape(e.d(12)))
                                .background(RellenoIconoConfirmar),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = r.icono,
                                contentDescription = null,
                                tint = Azul,
                                modifier = Modifier.size(e.d(24))
                            )
                        }
                        Spacer(Modifier.width(e.d(14)))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                r.examen,
                                fontSize = e.s(19),
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            Text(
                                FechasEs.fechaCorta(r.fecha),
                                fontSize = e.s(16),
                                color = TextoSecundario
                            )
                            Spacer(Modifier.height(e.d(6)))
                            ChipEstado(
                                texto = r.estado,
                                fondo = if (disponible) ChipDisponibleFondo else ProcesoFondo,
                                colorTexto = if (disponible) ChipDisponibleTexto else ProcesoTexto
                            )
                        }
                    }
                }
            }
        }
    }
}
