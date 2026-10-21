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
    val primario = if (rol == Rol.CLIENTE) VerdePetroleo else AzulProfesional
    val secundario = if (rol == Rol.CLIENTE) Turquesa else Color(0xFF3B82F6)
    val contenedor = if (rol == Rol.CLIENTE) VerdeSuave else AzulClaro
    val fondo = if (rol == Rol.CLIENTE) BlancoCalido else FondoVeterinario

    val colores = if (oscuro) darkColorScheme(
        primary = if (rol == Rol.CLIENTE) Turquesa else Color(0xFF60A5FA),
        secondary = secundario,
        primaryContainer = primario,
        secondaryContainer = Color(0xFF1E293B),
        background = Color(0xFF0F172A),
        surface = Color(0xFF1E293B),
        surfaceVariant = Color(0xFF334155),
        onSurface = Color(0xFFF8FAFC),
        onBackground = Color(0xFFF8FAFC),
        onPrimary = Color.White
    ) else lightColorScheme(
        primary = primario,
        secondary = secundario,
        primaryContainer = contenedor,
        secondaryContainer = Color(0xFFF1F5F9),
        background = fondo,
        surface = Color.White,
        surfaceVariant = Color(0xFFF1F5F9),
        onSurface = TextoPrincipal,
        onBackground = TextoPrincipal,
        onPrimary = Color.White
    )

    val activity = LocalContext.current as? Activity
    SideEffect {
        activity?.window?.let { window ->
            val colorBarra = if (oscuro) {
                android.graphics.Color.rgb(15, 23, 42)
            } else if (rol == Rol.CLIENTE) {
                android.graphics.Color.rgb(250, 251, 249)
            } else {
                android.graphics.Color.rgb(248, 250, 252)
            }
            window.statusBarColor = colorBarra
            window.navigationBarColor = colorBarra

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
