package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs

// Modelo propio de esta pantalla (lista fija).
private data class ResultadoMedico(
    val id: Int,
    val examen: String,
    val fecha: String,
    val estado: String
)

private val resultadosFijos = listOf(
    ResultadoMedico(1, "Hemograma completo", "2026-09-28", "Disponible"),
    ResultadoMedico(2, "Perfil lipídico", "2026-09-30", "Disponible"),
    ResultadoMedico(3, "Radiografía de tórax", "2026-10-02", "En proceso"),
    ResultadoMedico(4, "Electrocardiograma", "2026-10-05", "En proceso")
)

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
            contentPadding = PaddingValues(e.d(16)),
            verticalArrangement = Arrangement.spacedBy(e.d(12))
        ) {
            items(resultadosFijos, key = { it.id }) { r ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(e.d(16)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BordeTarjeta)
                ) {
                    Column(modifier = Modifier.padding(e.d(16))) {
                        Text(
                            r.examen,
                            fontSize = e.s(19),
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )
                        Text(
                            "Fecha: ${FechasEs.fechaCorta(r.fecha)}",
                            fontSize = e.s(17),
                            color = TextoSecundario
                        )
                        Text(
                            "Estado: ${r.estado}",
                            fontSize = e.s(17),
                            color = TextoSecundario
                        )
                    }
                }
            }
        }
    }
}
