package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val fechas = remember { proximosDias(7) }
    var fecha by remember { mutableStateOf(fechas.first().first) }
    var hora by remember { mutableStateOf<String?>(null) }
    val horarios = Repositorio.horariosDisponibles(medicoId, fecha)

    Scaffold(
        topBar = { BarraSuperior("Fecha y hora", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(medico?.nombre ?: "Médico", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            Text("Elige el día", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fechas) { (valor, etiqueta) ->
                    FilterChip(
                        selected = valor == fecha,
                        onClick = {
                            fecha = valor
                            hora = null
                        },
                        label = { Text(etiqueta) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            if (horarios.isEmpty()) {
                Text("No hay horarios disponibles para este día")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(horarios) { h ->
                    FilterChip(
                        selected = h == hora,
                        onClick = { hora = h },
                        label = { Text(h) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            BotonPrincipal(
                "Continuar",
                habilitado = hora != null,
                onClick = {
                    hora?.let { navController.navigate(Rutas.confirmar(medicoId, fecha, it)) }
                }
            )
        }
    }
}

/** Devuelve pares (clave "yyyy-MM-dd", etiqueta "lun 07/10") de los próximos días. */
private fun proximosDias(cantidad: Int): List<Pair<String, String>> {
    val formatoClave = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val formatoEtiqueta = SimpleDateFormat("EEE dd/MM", Locale.forLanguageTag("es-PE"))
    val calendario = Calendar.getInstance()
    val resultado = mutableListOf<Pair<String, String>>()
    repeat(cantidad) {
        resultado.add(formatoClave.format(calendario.time) to formatoEtiqueta.format(calendario.time))
        calendario.add(Calendar.DAY_OF_YEAR, 1)
    }
    return resultado
}