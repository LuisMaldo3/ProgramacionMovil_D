package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CampoBusqueda
import com.saludplus.citas.ui.components.TarjetaEspecialidad
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.Divisor
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Building
import compose.icons.fontawesomeicons.solid.MapMarkerAlt

@Composable
fun EspecialidadesScreen(navController: NavController) {
    val e = rememberEscala()
    var busqueda by remember { mutableStateOf("") }
    val localActual = Repositorio.localSeleccionado
    var mostrarDialogoLocal by remember { mutableStateOf(false) }

    val resultados = Repositorio.buscarEspecialidades(busqueda.trim())
    val porCategoria = resultados.groupBy { it.categoria }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior("Especialidades", onAtras = { navController.popBackStack() })

        // Banner informativo del local
        if (localActual == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(16), vertical = e.d(6))
                    .clickable { navController.navigate(Rutas.LOCALES) },
                shape = RoundedCornerShape(e.d(12)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)),
                border = BorderStroke(1.dp, Color(0xFFFFEEBA))
            ) {
                Row(
                    modifier = Modifier.padding(e.d(12)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.Building,
                        contentDescription = null,
                        tint = Color(0xFF856404),
                        modifier = Modifier.size(e.d(22))
                    )
                    Spacer(Modifier.width(e.d(10)))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Selecciona un local primero",
                            fontSize = e.s(16),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF856404)
                        )
                        Text(
                            "Debes elegir una sede antes de seleccionar especialidad.",
                            fontSize = e.s(14),
                            color = Color(0xFF856404)
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(16), vertical = e.d(6)),
                shape = RoundedCornerShape(e.d(12)),
                colors = CardDefaults.cardColors(containerColor = RellenoTarjetaMedico),
                border = BorderStroke(1.dp, BordeTarjeta)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = e.d(12), vertical = e.d(8)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.MapMarkerAlt,
                        contentDescription = null,
                        tint = Azul,
                        modifier = Modifier.size(e.d(18))
                    )
                    Spacer(Modifier.width(e.d(8)))
                    Text(
                        "Local: ${localActual.nombre}",
                        fontSize = e.s(15),
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Cambiar local",
                        modifier = Modifier.clickable { navController.navigate(Rutas.LOCALES) },
                        fontSize = e.s(14),
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                }
            }
        }

        Spacer(Modifier.height(e.d(4)))
        CampoBusqueda(
            valor = busqueda,
            onCambio = { busqueda = it },
            placeholder = "Buscar especialidad...",
            modifier = Modifier.padding(horizontal = e.d(16))
        )
        Spacer(Modifier.height(e.d(8)))

        if (resultados.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text(
                    "No se encontraron especialidades",
                    modifier = Modifier.padding(top = e.d(40)),
                    color = TextoSecundario,
                    fontSize = e.s(18)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                porCategoria.forEach { (categoria, lista) ->
                    item {
                        Text(
                            text = categoria.ifBlank { "Otras Especialidades" },
                            modifier = Modifier.padding(start = e.d(20), top = e.d(16), bottom = e.d(8)),
                            fontSize = e.s(18),
                            lineHeight = e.s(22),
                            fontWeight = FontWeight.Bold,
                            color = Azul
                        )
                    }

                    items(lista, key = { it.id }) { especialidad ->
                        Column {
                            TarjetaEspecialidad(
                                especialidad = especialidad,
                                onClick = {
                                    if (Repositorio.localSeleccionado == null) {
                                        mostrarDialogoLocal = true
                                    } else {
                                        navController.navigate(Rutas.medicos(especialidad.id))
                                    }
                                }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(start = e.d(107)),
                                color = Divisor
                            )
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoLocal) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoLocal = false },
            title = { Text("Selección de local requerida") },
            text = { Text("Debes seleccionar una sede (La Molina o Independencia) antes de agendar tu cita.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoLocal = false
                    navController.navigate(Rutas.LOCALES)
                }) {
                    Text("Seleccionar local", color = Azul, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoLocal = false }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }
}
