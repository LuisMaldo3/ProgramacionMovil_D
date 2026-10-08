package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior

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
    Scaffold(
        topBar = { BarraSuperior("Resultados") },
        bottomBar = { BarraInferior(Rutas.RESULTADOS, navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resultadosFijos) { r ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(r.examen, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Fecha: ${r.fecha}", style = MaterialTheme.typography.bodyMedium)
                        Text("Estado: ${r.estado}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}