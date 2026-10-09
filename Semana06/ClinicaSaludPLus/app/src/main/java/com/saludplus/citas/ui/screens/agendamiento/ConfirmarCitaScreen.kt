package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaInfo
import com.saludplus.citas.ui.components.FotoMedico
import com.saludplus.citas.ui.components.cargoMedico
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeMotivo
import com.saludplus.citas.ui.theme.ErrorRojo
import com.saludplus.citas.ui.theme.RellenoTarjetaMedico
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Building
import compose.icons.fontawesomeicons.solid.CalendarAlt
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.MapMarkerAlt

@Composable
fun ConfirmarCitaScreen(
    navController: NavController,
    medicoId: Int,
    fecha: String,
    hora: String
) {
    val e = rememberEscala()

    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    var motivo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior(
            "Confirmar cita",
            onAtras = {
                navController.popBackStack()
            }
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = e.d(24))
            ) {
                Spacer(
                    modifier = Modifier.height(e.d(7))
                )

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
                            modifier = Modifier.width(e.d(12))
                        )

                        Column {
                            Text(
                                text = medico.nombre,
                                fontSize = e.s(22),
                                lineHeight = e.s(28),
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )

                            Text(
                                text = cargoMedico(medico, especialidad),
                                fontSize = e.s(21),
                                lineHeight = e.s(26),
                                color = TextoSecundario
                            )

                            Text(
                                text = "CMP: ${medico.cmp}",
                                fontSize = e.s(20),
                                lineHeight = e.s(24),
                                color = TextoSecundario
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(e.d(11))
                )

                val local = Repositorio.localSeleccionado

                FilaInfo(
                    icono = FontAwesomeIcons.Solid.CalendarAlt,
                    etiqueta = "Fecha",
                    valor = FechasEs.fechaLarga(fecha)
                )

                FilaInfo(
                    icono = FontAwesomeIcons.Solid.Clock,
                    etiqueta = "Hora",
                    valor = FechasEs.rangoHora(hora)
                )

                FilaInfo(
                    icono = FontAwesomeIcons.Solid.Building,
                    etiqueta = "Sede / Local",
                    valor = "Local ${local?.nombre ?: "-"}"
                )

                FilaInfo(
                    icono = FontAwesomeIcons.Solid.MapMarkerAlt,
                    etiqueta = "Dirección",
                    valor = local?.direccion ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(e.d(2))
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                        ) {
                            append("Motivo de la consulta")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = TextoSecundario
                            )
                        ) {
                            append(" (opcional)")
                        }
                    },
                    modifier = Modifier.padding(start = e.d(18)),
                    fontSize = e.s(18),
                    lineHeight = e.s(24)
                )

                Spacer(
                    modifier = Modifier.height(e.d(14))
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = e.d(17))
                        .fillMaxWidth()
                        .height(e.d(74))
                        .clip(RoundedCornerShape(e.d(10)))
                        .border(
                            width = 1.dp,
                            color = BordeMotivo,
                            shape = RoundedCornerShape(e.d(10))
                        )
                        .padding(
                            horizontal = e.d(14),
                            vertical = e.d(12)
                        )
                ) {
                    BasicTextField(
                        value = motivo,
                        onValueChange = { nuevoMotivo: String ->
                            motivo = nuevoMotivo
                        },
                        textStyle = TextStyle(
                            fontSize = e.s(19),
                            color = Color.Black
                        ),
                        cursorBrush = SolidColor(Azul),
                        decorationBox = { campo ->
                            Box {
                                if (motivo.isEmpty()) {
                                    Text(
                                        text = "Describe tu motivo",
                                        fontSize = e.s(19),
                                        color = TextoSecundario
                                    )
                                }

                                campo()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                error?.let { mensaje ->
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(
                            start = e.d(18),
                            top = e.d(8)
                        ),
                        color = ErrorRojo,
                        fontSize = e.s(14)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    start = e.d(17),
                    end = e.d(17),
                    top = e.d(8),
                    bottom = e.d(20)
                )
        ) {
            BotonPrincipal(
                "Agendar cita",
                onClick = {
                    if (Repositorio.agendarCita(medicoId, fecha, hora)) {
                        navController.navigate(Rutas.CITA_EXITOSA) {
                            popUpTo(Rutas.HOME)
                        }
                    } else {
                        error = "Ese horario ya fue ocupado; elige otro"
                    }
                }
            )
        }
    }
}