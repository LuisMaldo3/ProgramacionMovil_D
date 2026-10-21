package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.R
import com.tecsup.huellitasvet.model.Especie

@Composable
fun MascotaAvatar(especie: Especie, tamano: Dp = 68.dp, modifier: Modifier = Modifier) {
    val resId = when (especie) {
        Especie.PERRO -> R.drawable.mascota_perro
        Especie.GATO -> R.drawable.mascota_gato
        Especie.OTRO -> R.drawable.mascota_conejo
    }

    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = especie.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(tamano)
        )
    }
}
