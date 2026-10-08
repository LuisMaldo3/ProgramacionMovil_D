package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.CampoBusqueda
import com.saludplus.citas.ui.components.FotoMedico
import com.saludplus.citas.ui.components.cargoMedico
import com.saludplus.citas.ui.theme.ChipDisponibleFondo
import com.saludplus.citas.ui.theme.ChipDisponibleTexto
import com.saludplus.citas.ui.theme.ChipSinHorariosFondo
import com.saludplus.citas.ui.theme.ChipSinHorariosTexto
import com.saludplus.citas.ui.theme.Divisor
import com.saludplus.citas.ui.theme.Estrella
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import java.time.LocalDate

@Composable
fun MedicosScreen(navController: NavController, especialidadId: Int) {
    val e = rememberEscala()
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    var mostrarBusqueda by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }
    // Ordenados por calificación (de mayor a menor) y filtrados en tiempo real
    val medicos = Repositorio.buscarMedicos(especialidadId, busqueda.trim())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior(
            titulo = "Médicos de ${especialidad?.nombre ?: ""}".trim(),
            onAtras = { navController.popBackStack() },
            accion = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Buscar médico",
                    tint = TextoPrincipal,
                    modifier = Modifier
                        .size(e.d(30))
                        .clickable {
                            mostrarBusqueda = !mostrarBusqueda
                            if (!mostrarBusqueda) busqueda = ""
                        }
                )
            }
        )

        if (mostrarBusqueda) {
            Spacer(Modifier.height(e.d(4)))
            CampoBusqueda(
                valor = busqueda,
                onCambio = { busqueda = it },
                placeholder = "Buscar médico...",
                modifier = Modifier.padding(horizontal = e.d(16))
            )
        }
        Spacer(Modifier.height(e.d(14)))

        if (medicos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text(
                    "No hay médicos disponibles",
                    modifier = Modifier.padding(top = e.d(40)),
                    color = TextoSecundario,
                    fontSize = e.s(18)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(medicos, key = { it.id }) { medico ->
                    FilaMedico(
                        medico = medico,
                        cargo = cargoMedico(medico, especialidad),
                        disponibilidad = disponibilidad(medico.id),
                        onClick = { navController.navigate(Rutas.fechaHora(medico.id)) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = e.d(16)),
                        color = Divisor
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaMedico(
    medico: Medico,
    cargo: String,
    disponibilidad: String?,
    onClick: () -> Unit
) {
    val e = rememberEscala()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(e.d(157))
            .clickable(onClick = onClick)
    ) {
        FotoMedico(
            medico = medico,
            tamano = e.d(96),
            modifier = Modifier.padding(start = e.d(18), top = e.d(15))
        )
        Column(
            modifier = Modifier.padding(start = e.d(123), top = e.d(24))
        ) {
            Text(
                medico.nombre,
                fontSize = e.s(19),
                lineHeight = e.s(24),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(e.d(7)))
            Text(
                cargo,
                fontSize = e.s(18),
                lineHeight = e.s(22),
                color = TextoSecundario
            )
            Spacer(Modifier.height(e.d(7)))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = Estrella,
                    modifier = Modifier.size(e.d(22))
                )
                Spacer(Modifier.width(e.d(4)))
                Text(
                    "${medico.calificacion} (${medico.resenas})",
                    fontSize = e.s(18),
                    lineHeight = e.s(22),
                    color = TextoSecundario
                )
            }
        }
        // Etiqueta de disponibilidad (abajo a la derecha)
        val activo = disponibilidad != null
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = e.d(18), bottom = e.d(23))
                .clip(RoundedCornerShape(e.d(8)))
                .background(if (activo) ChipDisponibleFondo else ChipSinHorariosFondo)
                .padding(horizontal = e.d(10), vertical = e.d(4))
        ) {
            Text(
                disponibilidad ?: "Sin horarios",
                fontSize = e.s(15),
                fontWeight = FontWeight.SemiBold,
                color = if (activo) ChipDisponibleTexto else ChipSinHorariosTexto
            )
        }
    }
}

/** Primer día hábil (entre los próximos 5) en que el médico tiene horarios libres. */
private fun disponibilidad(medicoId: Int): String? {
    val hoy = LocalDate.now()
    val primero = FechasEs.diasHabiles(hoy, 5).firstOrNull {
        Repositorio.horariosDisponibles(medicoId, it.toString()).isNotEmpty()
    } ?: return null
    return when (primero) {
        hoy -> "Disponible hoy"
        hoy.plusDays(1) -> "Disponible mañana"
        else -> "Disponible esta semana"
    }
}
