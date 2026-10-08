package com.saludplus.citas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.ui.theme.GrisIcono
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

/** Fila de la lista de Especialidades (84 dp de alto, como en el diseño). */
@Composable
fun TarjetaEspecialidad(
    especialidad: Especialidad,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val e = rememberEscala()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(e.d(84))
            .clickable(onClick = onClick)
            .padding(start = e.d(32), end = e.d(20)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoEspecialidad(especialidad = especialidad, tamano = e.d(46))
        Spacer(Modifier.width(e.d(19)))
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Top)
                .padding(top = e.d(13))
        ) {
            Text(
                especialidad.nombre,
                fontSize = e.s(18),
                lineHeight = e.s(24),
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal
            )
            Spacer(Modifier.height(e.d(7)))
            Text(
                especialidad.descripcion,
                fontSize = e.s(16),
                lineHeight = e.s(20),
                color = TextoSecundario
            )
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = GrisIcono,
            modifier = Modifier.size(e.d(24))
        )
    }
}
