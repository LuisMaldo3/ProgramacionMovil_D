package com.tecsup.huellitasvet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.Rol

@Suppress("DEPRECATION")
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
    // Igualamos barras del sistema con el perfil y el modo oscuro.
    // No se fija la barra de estado en verde cuando el veterinario está activo.
    val activity = LocalContext.current as? Activity
    SideEffect {
        activity?.window?.let { window ->
            window.statusBarColor = if (oscuro) {
                android.graphics.Color.rgb(16, 24, 39)
            } else if (rol == Rol.CLIENTE) {
                android.graphics.Color.rgb(246, 253, 249)
            } else {
                android.graphics.Color.rgb(247, 249, 255)
            }
            window.navigationBarColor = if (oscuro) {
                android.graphics.Color.rgb(16, 24, 39)
            } else {
                android.graphics.Color.BLACK
            }
            val indicadores = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = if (oscuro) {
                indicadores and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                indicadores or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
    MaterialTheme(colorScheme = colores, typography = VetTypography, content = content)
}
