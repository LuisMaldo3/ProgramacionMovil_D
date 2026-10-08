package com.saludplus.citas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.BordeTarjeta
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun TarjetaCita(
    medico: String,
    especialidad: String,
    fecha: String,
    hora: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val e = rememberEscala()
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(e.d(16)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeTarjeta)
    ) {
        Row(modifier = Modifier.padding(e.d(16)), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = Azul,
                modifier = Modifier.size(e.d(40))
            )
            Spacer(Modifier.width(e.d(14)))
            Column {
                Text(medico, fontSize = e.s(19), fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(especialidad, fontSize = e.s(17), color = TextoSecundario)
                Text("$fecha · $hora", fontSize = e.s(16), color = TextoSecundario)
            }
        }
    }
}
