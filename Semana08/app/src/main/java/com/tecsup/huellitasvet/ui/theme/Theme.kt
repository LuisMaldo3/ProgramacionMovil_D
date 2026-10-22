package com.tecsup.huellitasvet.ui.theme

import android.app.Activity
import android.view.View
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tecsup.huellitasvet.model.Rol

@Suppress("DEPRECATION")
@Composable
fun VetTheme(rol: Rol = Rol.CLIENTE, oscuro: Boolean = false, content: @Composable () -> Unit) {
    val dueno = rol == Rol.CLIENTE
    val primario = if (dueno) VerdeDueno else AzulVeterinario
    val contenedor = if (dueno) VerdeContenedor else AzulContenedor
    val fondo = if (dueno) FondoDueno else FondoVeterinario
    val darkBackground = Color(0xFF111C28)
    val darkSurface = Color(0xFF192839)

    val colores = if (oscuro) darkColorScheme(
        primary = if (dueno) Color(0xFF6ED5BA) else Color(0xFF9BB8FF),
        onPrimary = Color(0xFF102027),
        primaryContainer = if (dueno) Color(0xFF174B43) else Color(0xFF233D79),
        onPrimaryContainer = Color(0xFFEAF4FF),
        secondary = if (dueno) Color(0xFFFFB18F) else Color(0xFF8DD6E1),
        background = darkBackground,
        surface = darkSurface,
        surfaceVariant = Color(0xFF24384A),
        onSurface = Color(0xFFE8EFF7),
        onSurfaceVariant = Color(0xFFB3C0CE),
        onBackground = Color(0xFFE8EFF7),
        outline = Color(0xFF526578),
        error = Color(0xFFFF8A80)
    ) else lightColorScheme(
        primary = primario,
        onPrimary = Color.White,
        primaryContainer = contenedor,
        onPrimaryContainer = if (dueno) Color(0xFF07584D) else Color(0xFF163B8D),
        secondary = if (dueno) AcentoCalido else Color(0xFF31889B),
        onSecondary = Color.White,
        secondaryContainer = if (dueno) Color(0xFFFFE7DC) else Color(0xFFDDF4F7),
        onSecondaryContainer = if (dueno) Color(0xFF783A27) else Color(0xFF124C58),
        background = fondo,
        surface = Color.White,
        surfaceVariant = if (dueno) Color(0xFFEAF2EE) else Color(0xFFEBF0F8),
        onSurface = TextoPrincipal,
        onSurfaceVariant = GrisSecundario,
        onBackground = TextoPrincipal,
        outline = if (dueno) Color(0xFFD2E2DB) else Color(0xFFD5DEEB),
        error = Color(0xFFB42318)
    )

    val activity = LocalContext.current as? Activity
    SideEffect {
        activity?.window?.let { window ->
            val colorBarra = if (oscuro) darkBackground else fondo
            window.statusBarColor = colorBarra.toArgb()
            window.navigationBarColor = colorBarra.toArgb()
            var indicadores = window.decorView.systemUiVisibility
            indicadores = if (oscuro) {
                indicadores and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() and
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            } else {
                indicadores or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            window.decorView.systemUiVisibility = indicadores
        }
    }

    MaterialTheme(colorScheme = colores, typography = VetTypography, content = content)
}

private fun Color.toArgb(): Int = android.graphics.Color.argb(
    (alpha * 255).toInt(),
    (red * 255).toInt(),
    (green * 255).toInt(),
    (blue * 255).toInt()
)
