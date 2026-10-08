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
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    val e = rememberEscala()
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    // Primer día hábil desde hoy: si hoy es sábado o domingo, empieza el lunes
    val inicio = remember { FechasEs.primerDiaHabil(LocalDate.now()) }

    // 0 = semana actual. Las flechas suman o restan una semana.
    var semana by rememberSaveable { mutableStateOf(0) }
    var fechaIso by rememberSaveable { mutableStateOf(inicio.toString()) }
    var hora by rememberSaveable { mutableStateOf<String?>(null) }

    // Los 5 días hábiles de la semana mostrada (generados con LocalDate)
    val dias = FechasEs.diasHabiles(inicio.plusWeeks(semana.toLong()), 5)
    val fecha = LocalDate.parse(fechaIso)

    // Se recalcula solo cada vez que cambia el día: un horario reservado no aparece
    val horarios = Repositorio.horariosDisponibles(medicoId, fechaIso)

    fun cambiarSemana(delta: Int) {
        val nueva = semana + delta
        if (nueva < 0) return // no se puede retroceder antes de la semana actual
        semana = nueva
        fechaIso = FechasEs.diasHabiles(inicio.plusWeeks(nueva.toLong()), 5).first().toString()
        hora = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior("Seleccionar fecha y hora", onAtras = { navController.popBackStack() })
        Spacer(Modifier.height(e.d(11)))

        // Tarjeta del médico (y 107–229)
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
                FotoMedico(medico = medico, tamano = e.d(96))
                Spacer(Modifier.width(e.d(13)))
                Column {
                    Text(
                        medico.nombre,
                        fontSize = e.s(22),
                        lineHeight = e.s(28),
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(e.d(13)))
                    Text(
                        cargoMedico(medico, especialidad),
                        fontSize = e.s(21),
                        lineHeight = e.s(26),
                        color = TextoSecundario
                    )
                }
            }
        }

        Spacer(Modifier.height(e.d(30)))

        // Mes y año con las flechas de semana (y 259–283)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(e.d(28))
                .padding(horizontal = e.d(27)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ChevronLeft,
                contentDescription = "Semana anterior",
                tint = if (semana > 0) TextoPrincipal else GrisClaro,
                modifier = Modifier
                    .size(e.d(28))
                    .clickable(enabled = semana > 0) { cambiarSemana(-1) }
            )
            Text(
                FechasEs.mesYAnio(dias),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = e.s(21),
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "Semana siguiente",
                tint = TextoPrincipal,
                modifier = Modifier
                    .size(e.d(28))
                    .clickable { cambiarSemana(1) }
            )
        }

        Spacer(Modifier.height(e.d(34)))

        // Abreviaturas y cuadros de día (y 324–411)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = e.d(15)),
            horizontalArrangement = Arrangement.spacedBy(e.d(10))
        ) {
            dias.forEach { dia ->
                val seleccionado = dia == fecha
                Column(
                    modifier = Modifier.width(e.d(58)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        FechasEs.diaSemanaCorto(dia),
                        fontSize = e.s(17),
                        lineHeight = e.s(20),
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                        color = if (seleccionado) Azul else TextoSecundario
                    )
                    Spacer(Modifier.height(e.d(5)))
                    Box(
                        modifier = Modifier
                            .size(e.d(58), e.d(64))
                            .clip(RoundedCornerShape(e.d(12)))
                            .background(if (seleccionado) Azul else Color.Transparent)
                            .clickable {
                                fechaIso = dia.toString()
                                hora = null // al cambiar de día se reinicia la hora
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            dia.dayOfMonth.toString(),
                            fontSize = e.s(25),
                            fontWeight = FontWeight.Bold,
                            color = if (seleccionado) Color.White else TextoPrincipal
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(e.d(27)))

        // Cuadrícula de horarios (empieza en y 437)
        if (horarios.isEmpty()) {
            Text(
                "No hay horarios disponibles para este día",
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
            items(horarios) { h ->
                val seleccionada = h == hora
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(e.d(59))
                        .clip(RoundedCornerShape(e.d(12)))
                        .background(if (seleccionada) Azul else RellenoHora)
                        .border(
                            1.dp,
                            if (seleccionada) Azul else BordeHora,
                            RoundedCornerShape(e.d(12))
                        )
                        .clickable { hora = h },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        h,
                        fontSize = e.s(20),
                        color = if (seleccionada) Color.White else TextoPrincipal
                    )
                }
            }
        }

        // Botón "Continuar": solo se habilita con día y hora elegidos
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = e.d(16), end = e.d(16), top = e.d(8), bottom = e.d(20))
        ) {
            BotonPrincipal(
                "Continuar",
                habilitado = hora != null,
                onClick = {
                    hora?.let { navController.navigate(Rutas.confirmar(medicoId, fechaIso, it)) }
                }
            )
        }
    }
}
