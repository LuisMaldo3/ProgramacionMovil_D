package com.tecsup.huellitasvet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.Rol

@Composable
fun VetTheme(rol: Rol = Rol.CLIENTE, oscuro: Boolean = false, content: @Composable () -> Unit) {
    val primario = if (rol == Rol.CLIENTE) VerdeDueno else AzulVeterinario
    val contenedor = if (rol == Rol.CLIENTE) VerdeContenedor else AzulContenedor
    val fondo = if (rol == Rol.CLIENTE) FondoDueno else FondoVeterinario
    val colores = if (oscuro) darkColorScheme(
        primary = if (rol == Rol.CLIENTE) Color(0xFF6EE7B7) else Color(0xFF93C5FD),
        primaryContainer = primario,
        background = Color(0xFF101827),
        surface = Color(0xFF162131)
    ) else lightColorScheme(
        primary = primario,
        primaryContainer = contenedor,
        background = fondo,
        surface = Color.White,
        onSurface = TextoPrincipal,
        onBackground = TextoPrincipal
    )
    MaterialTheme(colorScheme = colores, typography = VetTypography, content = content)
}
