package com.maldonado.navlab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = MoradoPrincipal,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    background = FondoPrincipal,
    onBackground = TextoPrincipal,
    surface = FondoPrincipal,
    onSurface = TextoPrincipal,
    surfaceVariant = TarjetasDirectorio,
    onSurfaceVariant = TextoSecundario,
    outline = BordesCampos
)

@Composable
fun NavLabTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = false,
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
