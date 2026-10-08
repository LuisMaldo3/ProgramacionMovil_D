package com.saludplus.citas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.GrisIcono
import com.saludplus.citas.ui.theme.TarjetaAgendarFondo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import com.saludplus.citas.util.FechasEs
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Clock
import java.time.LocalDate

/** Tarjeta de una cita: bloque con el día y el mes, médico, especialidad y rango de hora. */
@Composable
fun TarjetaCita(
    medico: String,
    especialidad: String,
    fechaIso: String,
    hora: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val e = rememberEscala()
    val fecha = try { LocalDate.parse(fechaIso) } catch (ex: Exception) { null }
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(e.d(16)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeTarjeta)
    ) {
        Row(modifier = Modifier.padding(e.d(14)), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .size(e.d(58), e.d(64))
                    .clip(RoundedCornerShape(e.d(14)))
                    .background(TarjetaAgendarFondo),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    fecha?.dayOfMonth?.toString() ?: "-",
                    fontSize = e.s(24),
                    lineHeight = e.s(28),
                    fontWeight = FontWeight.Bold,
                    color = Azul
                )
                Text(
                    fecha?.let { FechasEs.nombreMes(it).take(3).uppercase() } ?: "",
                    fontSize = e.s(13),
                    fontWeight = FontWeight.Bold,
                    color = Azul
                )
            }
            Spacer(Modifier.width(e.d(14)))
            Column(modifier = Modifier.weight(1f)) {
                Text(medico, fontSize = e.s(19), fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(especialidad, fontSize = e.s(16), color = TextoSecundario)
                Spacer(Modifier.height(e.d(4)))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        FontAwesomeIcons.Solid.Clock,
                        contentDescription = null,
                        tint = Azul,
                        modifier = Modifier.size(e.d(14))
                    )
                    Spacer(Modifier.width(e.d(6)))
                    Text(
                        FechasEs.rangoHora(hora),
                        fontSize = e.s(16),
                        fontWeight = FontWeight.SemiBold,
                        color = TextoPrincipal
                    )
                }
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = GrisIcono,
                modifier = Modifier.size(e.d(24))
            )
        }
    }
}
