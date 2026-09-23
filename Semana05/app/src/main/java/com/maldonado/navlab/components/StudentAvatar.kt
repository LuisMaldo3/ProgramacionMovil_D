package com.maldonado.navlab.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maldonado.navlab.ui.theme.LilaClaro
import com.maldonado.navlab.ui.theme.MoradoPrincipal

@Composable
fun StudentAvatar(
    size: Dp,
    iconSize: Dp,
    modifier: Modifier = Modifier,
    photoResId: Int? = null,
    backgroundColor: Color = LilaClaro,
    iconColor: Color = MoradoPrincipal,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.White,
    elevation: Dp = 0.dp
) {
    var baseModifier = modifier.size(size)
    if (elevation > 0.dp) {
        baseModifier = baseModifier.shadow(elevation, CircleShape)
    }
    if (borderWidth > 0.dp) {
        baseModifier = baseModifier.border(borderWidth, borderColor, CircleShape)
    }

    Box(
        modifier = baseModifier
            .clip(CircleShape)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (photoResId != null) {
            Image(
                painter = painterResource(id = photoResId),
                contentDescription = "Fotografía del estudiante",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(size)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Silueta de persona de sustitución",
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
