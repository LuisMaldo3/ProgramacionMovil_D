package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.R
import com.tecsup.huellitasvet.model.Especie

/** Fotografías fotorrealistas locales generadas para la práctica (sin dependencia de Internet). */
@Composable
fun MascotaAvatar(especie: Especie, tamano: Dp = 68.dp, modifier: Modifier = Modifier) {
    val imagen = when (especie) {
        Especie.PERRO -> R.drawable.mascota_perro
        Especie.GATO -> R.drawable.mascota_gato
        Especie.OTRO -> R.drawable.mascota_conejo
    }
    Box(modifier = modifier.size(tamano).clip(CircleShape).background(Color(0xFFE7F7F1)),
        contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = "Imagen de ${especie.name.lowercase()}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize().clip(CircleShape)
        )
    }
}
