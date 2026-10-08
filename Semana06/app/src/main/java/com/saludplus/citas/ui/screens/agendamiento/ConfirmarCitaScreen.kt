package com.saludplus.citas.ui.screens.agendamiento

import com.saludplus.citas.ui.theme.RellenoIconoConfirmar
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.fontawesomeicons.Solid
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.solid.MapMarkerAlt
import compose.icons.fontawesomeicons.solid.UserMd
import compose.icons.fontawesomeicons.solid.Clock
import compose.icons.fontawesomeicons.solid.CalendarAlt
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
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

@Composable
fun ConfirmarCitaScreen(
    navController: NavController,
    medicoId: Int,
    fecha: String,
    hora: String
) {
    val e = rememberEscala()
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var motivo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior("Confirmar cita", onAtras = { navController.popBackStack() })

        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = e.d(24))
            ) {
                Spacer(Modifier.height(e.d(7)))

                // Tarjeta del médico (y 103–225)
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
                        Spacer(Modifier.width(e.d(12)))
                        Column {
                            Text(
                                medico.nombre,
                                fontSize = e.s(22),
                                lineHeight = e.s(28),
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                            Text(
                                cargoMedico(medico, especialidad),
                                fontSize = e.s(21),
                                lineHeight = e.s(26),
                                color = TextoSecundario
                            )
                            Text(
                                "CMP: ${medico.cmp}",
                                fontSize = e.s(20),
                                lineHeight = e.s(24),
                                color = TextoSecundario
                            )
                        }
                    }
                }

                Spacer(Modifier.height(e.d(11)))

                // La fecha llega como "yyyy-MM-dd" y se muestra en texto en español
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
                    icono = FontAwesomeIcons.Solid.UserMd,
                    etiqueta = "Tipo de atención",
                    valor = "Consulta presencial"
                )
                FilaInfo(
                    icono = FontAwesomeIcons.Solid.MapMarkerAlt,
                    etiqueta = "Dirección",
                    valor = "Av. Los Olivos 123\nLima"
                )

                Spacer(Modifier.height(e.d(2)))

                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextoPrincipal)) {
                            append("Motivo de la consulta")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = TextoSecundario)) {
                            append(" (opcional)")
                        }
                    },
                    modifier = Modifier.padding(start = e.d(18)),
                    fontSize = e.s(18),
                    lineHeight = e.s(24)
                )
                Spacer(Modifier.height(e.d(14)))
                Box(
                    modifier = Modifier
                        .padding(horizontal = e.d(17))
                        .fillMaxWidth()
                        .height(e.d(74))
                        .clip(RoundedCornerShape(e.d(10)))
                        .border(1.dp, BordeMotivo, RoundedCornerShape(e.d(10)))
                        .padding(horizontal = e.d(14), vertical = e.d(12))
                ) {
                    BasicTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        textStyle = TextStyle(fontSize = e.s(19), color = Color.Black),
                        cursorBrush = SolidColor(Azul),
                        decorationBox = { campo ->
                            Box {
                                if (motivo.isEmpty()) {
                                    Text(
                                        "Describe tu motivo",
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

                if (error != null) {
                    Text(
                        error ?: "",
                        modifier = Modifier.padding(start = e.d(18), top = e.d(8)),
                        color = ErrorRojo,
                        fontSize = e.s(14)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = e.d(17), end = e.d(17), top = e.d(8), bottom = e.d(20))
        ) {
            BotonPrincipal("Agendar cita", onClick = {
                if (Repositorio.agendarCita(medicoId, fecha, hora)) {
                    navController.navigate(Rutas.CITA_EXITOSA) {
                        popUpTo(Rutas.HOME)
                    }
                } else {
                    error = "Ese horario ya fue ocupado; elige otro"
                }
            })
        }
    }
}

/** Fila de información: ícono del diseño a la izquierda, etiqueta pequeña y valor debajo. */
@Composable
private fun FilaInfo(icono: ImageVector, etiqueta: String, valor: String) {
    val e = rememberEscala()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = e.d(26), top = e.d(2), bottom = e.d(28.5f)),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = e.d(6))
                .size(e.d(44))
                .clip(RoundedCornerShape(e.d(12)))
                .background(RellenoIconoConfirmar),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Azul,
                modifier = Modifier.size(e.d(22))
            )
        }
        Spacer(Modifier.width(e.d(20)))
        Column {
            Text(
                etiqueta,
                fontSize = e.s(16),
                lineHeight = e.s(20),
                color = TextoSecundario
            )
            Text(
                valor,
                fontSize = e.s(18),
                lineHeight = e.s(24),
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )
        }
    }
}
