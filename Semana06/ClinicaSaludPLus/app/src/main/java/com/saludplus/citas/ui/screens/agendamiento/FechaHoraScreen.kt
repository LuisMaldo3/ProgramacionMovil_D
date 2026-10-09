package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FotoMedico
import com.saludplus.citas.ui.components.cargoMedico
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeHora
import com.saludplus.citas.ui.theme.GrisClaro
import com.saludplus.citas.ui.theme.RellenoHora
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    navController: NavController,
    medicoId: Int
) {
    val e = rememberEscala()

    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    // Si hoy es sábado o domingo, empieza el lunes.
    val inicio = remember {
        FechasEs.primerDiaHabil(LocalDate.now())
    }

    var semana by rememberSaveable(medicoId) {
        mutableStateOf(0)
    }

    // Genera cinco días hábiles desde el inicio del periodo mostrado.
    val dias = FechasEs.diasHabiles(
        inicio.plusWeeks(semana.toLong()),
        5
    )

    var fechaIso by rememberSaveable(medicoId, semana) {
        mutableStateOf(
            dias.firstOrNull { Repositorio.horariosDisponibles(medicoId, it.toString()).isNotEmpty() }?.toString()
                ?: dias.first().toString()
        )
    }

    var hora by rememberSaveable(medicoId, fechaIso) {
        mutableStateOf<String?>(null)
    }

    // Asegurarse de que si fechaIso no está en 'dias', se seleccione el primer día disponible de 'dias'
    if (dias.none { it.toString() == fechaIso }) {
        val primerDisp = dias.firstOrNull { Repositorio.horariosDisponibles(medicoId, it.toString()).isNotEmpty() }?.toString()
            ?: dias.first().toString()
        fechaIso = primerDisp
        hora = null
    }

    val fecha = LocalDate.parse(fechaIso)

    val horarios = Repositorio.horariosDisponibles(
        medicoId,
        fechaIso
    )

    fun cambiarSemana(delta: Int) {
        val nueva = semana + delta
        if (nueva < 0) return
        semana = nueva
        val nuevosDias = FechasEs.diasHabiles(
            inicio.plusWeeks(nueva.toLong()),
            5
        )
        fechaIso = nuevosDias.firstOrNull {
            Repositorio.horariosDisponibles(medicoId, it.toString()).isNotEmpty()
        }?.toString() ?: nuevosDias.first().toString()
        hora = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior(
            "Seleccionar fecha y hora",
            onAtras = {
                navController.popBackStack()
            }
        )

        Spacer(
            modifier = Modifier.height(e.d(11))
        )

        // Tarjeta del médico.
        if (medico != null) {
            Row(
                modifier = Modifier
                    .padding(horizontal = e.d(16))
                    .fillMaxWidth()
                    .height(e.d(122))
                    .clip(RoundedCornerShape(e.d(16)))
                    .background(RellenoTarjetaMedico)
                    .padding(start = e.d(12)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FotoMedico(
                    medico = medico,
                    tamano = e.d(96)
                )

                Spacer(
                    modifier = Modifier.width(e.d(13))
                )

                Column {
                    Text(
                        text = medico.nombre,
                        fontSize = e.s(22),
                        lineHeight = e.s(28),
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )

                    Spacer(
                        modifier = Modifier.height(e.d(13))
                    )

                    Text(
                        text = cargoMedico(medico, especialidad),
                        fontSize = e.s(21),
                        lineHeight = e.s(26),
                        color = TextoSecundario
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(e.d(30))
        )

        // Mes y año con navegación por semanas.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(e.d(28))
                .padding(horizontal = e.d(27)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Semana anterior",
                tint = if (semana > 0) {
                    TextoPrincipal
                } else {
                    GrisClaro
                },
                modifier = Modifier
                    .size(e.d(28))
                    .clickable(enabled = semana > 0) {
                        cambiarSemana(-1)
                    }
            )

            Text(
                text = FechasEs.mesYAnio(dias),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = e.s(21),
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Semana siguiente",
                tint = TextoPrincipal,
                modifier = Modifier
                    .size(e.d(28))
                    .clickable {
                        cambiarSemana(1)
                    }
            )
        }

        Spacer(
            modifier = Modifier.height(e.d(34))
        )

        // Días disponibles basados en la disponibilidad real del médico.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = e.d(15)),
            horizontalArrangement = Arrangement.spacedBy(e.d(10))
        ) {
            dias.forEach { dia ->
                val seleccionado = dia == fecha
                val horariosDia = Repositorio.horariosDisponibles(medicoId, dia.toString())
                val disponible = horariosDia.isNotEmpty()

                Column(
                    modifier = Modifier.width(e.d(58)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = FechasEs.diaSemanaCorto(dia),
                        fontSize = e.s(17),
                        lineHeight = e.s(20),
                        fontWeight = if (seleccionado) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        color = if (disponible) {
                            if (seleccionado) Azul else TextoSecundario
                        } else {
                            GrisClaro
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(e.d(5))
                    )

                    Box(
                        modifier = Modifier
                            .size(
                                width = e.d(58),
                                height = e.d(64)
                            )
                            .clip(RoundedCornerShape(e.d(12)))
                            .background(
                                when {
                                    seleccionado -> Azul
                                    disponible -> RellenoHora
                                    else -> GrisClaro.copy(alpha = 0.25f)
                                }
                            )
                            .clickable(enabled = disponible) {
                                if (disponible) {
                                    fechaIso = dia.toString()
                                    hora = null
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dia.dayOfMonth.toString(),
                            fontSize = e.s(25),
                            fontWeight = FontWeight.Bold,
                            color = when {
                                seleccionado -> Color.White
                                disponible -> TextoPrincipal
                                else -> TextoSecundario.copy(alpha = 0.4f)
                            }
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(e.d(27))
        )

        // Horarios disponibles para la fecha seleccionada.
        if (horarios.isEmpty()) {
            Text(
                text = "No hay horarios disponibles para este día",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = e.d(19)),
                color = TextoSecundario,
                fontSize = e.s(18)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = e.d(19)),
            horizontalArrangement = Arrangement.spacedBy(e.d(23)),
            verticalArrangement = Arrangement.spacedBy(e.d(15.5f))
        ) {
            items(horarios) { horario ->
                val seleccionada = horario == hora

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(e.d(59))
                        .clip(RoundedCornerShape(e.d(12)))
                        .background(
                            if (seleccionada) Azul else RellenoHora
                        )
                        .border(
                            width = 1.dp,
                            color = if (seleccionada) Azul else BordeHora,
                            shape = RoundedCornerShape(e.d(12))
                        )
                        .clickable {
                            hora = horario
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = horario,
                        fontSize = e.s(20),
                        color = if (seleccionada) {
                            Color.White
                        } else {
                            TextoPrincipal
                        }
                    )
                }
            }
        }

        // Continuar cuando se haya seleccionado un horario.
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    start = e.d(16),
                    end = e.d(16),
                    top = e.d(8),
                    bottom = e.d(20)
                )
        ) {
            BotonPrincipal(
                "Continuar",
                habilitado = hora != null,
                onClick = {
                    hora?.let { horaSeleccionada ->
                        navController.navigate(
                            Rutas.confirmar(
                                medicoId,
                                fechaIso,
                                horaSeleccionada
                            )
                        )
                    }
                }
            )
        }
    }
}
