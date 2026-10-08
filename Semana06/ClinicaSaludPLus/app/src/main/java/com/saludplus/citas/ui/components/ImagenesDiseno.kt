package com.saludplus.citas.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Allergies
import compose.icons.fontawesomeicons.solid.Baby
import compose.icons.fontawesomeicons.solid.Bone
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.Heart
import compose.icons.fontawesomeicons.solid.Heartbeat
import compose.icons.fontawesomeicons.solid.User
import compose.icons.fontawesomeicons.solid.Venus
import com.saludplus.citas.R
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.rememberEscala

/** Glifo (Font Awesome), color del glifo y color pastel del círculo de cada especialidad. */
private class EstiloEspecialidad(val icono: ImageVector, val color: Color, val fondo: Color)

private fun estiloEspecialidad(nombre: String): EstiloEspecialidad = when (nombre) {
    "Medicina General" -> EstiloEspecialidad(FontAwesomeIcons.Solid.User, Color(0xFF1E9BF0), Color(0xFFE0F3FE))
    "Pediatría" -> EstiloEspecialidad(FontAwesomeIcons.Solid.Baby, Color(0xFFF28C28), Color(0xFFFFE9D2))
    "Ginecología" -> EstiloEspecialidad(FontAwesomeIcons.Solid.Venus, Color(0xFFEC4899), Color(0xFFFDE7F1))
    "Cardiología" -> EstiloEspecialidad(FontAwesomeIcons.Solid.Heartbeat, Color(0xFFE53935), Color(0xFFFDE5E5))
    "Dermatología" -> EstiloEspecialidad(FontAwesomeIcons.Solid.Allergies, Color(0xFFF28C28), Color(0xFFFFE9D2))
    "Traumatología" -> EstiloEspecialidad(FontAwesomeIcons.Solid.Bone, Color(0xFF1E6FE0), Color(0xFFE3EEFE))
    "Oftalmología" -> EstiloEspecialidad(FontAwesomeIcons.Solid.Eye, Color(0xFF1E6FE0), Color(0xFFE3EEFE))
    else -> EstiloEspecialidad(FontAwesomeIcons.Solid.User, Azul, Color(0xFFE5F1FE))
}

/** Ícono de la especialidad dentro de un círculo pastel (todo dibujado con vectores de la librería). */
@Composable
fun IconoEspecialidad(especialidad: Especialidad, tamano: Dp, modifier: Modifier = Modifier) {
    val estilo = estiloEspecialidad(especialidad.nombre)
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(estilo.fondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = estilo.icono,
            contentDescription = especialidad.nombre,
            tint = estilo.color,
            modifier = Modifier.size(tamano * 0.5f)
        )
    }
}

/**
 * Logo de la clínica: cruz azul, lóbulo turquesa y corazón blanco.
 * Se dibuja con código (sin imágenes); el corazón es un ícono de Font Awesome.
 */
@Composable
fun LogoSaludPlus(modifier: Modifier = Modifier) {
    val e = rememberEscala()
    val ancho = 122f
    val alto = 109f
    Box(modifier = modifier.size(e.d(ancho), e.d(alto))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val radio = CornerRadius(w * 0.11f, w * 0.11f)
            // Brazo vertical de la cruz
            drawRoundRect(
                color = Azul,
                topLeft = Offset(w * 0.335f, 0f),
                size = Size(w * 0.37f, h),
                cornerRadius = radio
            )
            // Brazo horizontal de la cruz
            drawRoundRect(
                color = Azul,
                topLeft = Offset(w * 0.02f, h * 0.33f),
                size = Size(w * 0.66f, h * 0.38f),
                cornerRadius = radio
            )
            // Lóbulo turquesa de la derecha
            drawRoundRect(
                color = Color(0xFF1EA7D8),
                topLeft = Offset(w * 0.56f, h * 0.27f),
                size = Size(w * 0.44f, h * 0.56f),
                cornerRadius = CornerRadius(w * 0.20f, w * 0.20f)
            )
        }
        Icon(
            imageVector = FontAwesomeIcons.Solid.Heart,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .offset(x = e.d(ancho * 0.40f), y = e.d(alto * 0.47f))
                .size(e.d(41))
        )
    }
}

/** Foto de cada médico. Los que no tienen foto propia reutilizan una de las del diseño. */
@DrawableRes
fun fotoMedico(medico: Medico): Int {
    val fotosMujer = listOf(
        R.drawable.medico_ana_torres,
        R.drawable.medico_claudia_rojas,
        R.drawable.medico_mariana_soto
    )
    return when {
        medico.nombre.contains("Ana Torres") -> R.drawable.medico_ana_torres
        medico.nombre.contains("Claudia Rojas") -> R.drawable.medico_claudia_rojas
        medico.nombre.contains("Luis Ramírez") -> R.drawable.medico_luis_ramirez
        medico.nombre.contains("Mariana Soto") -> R.drawable.medico_mariana_soto
        medico.nombre.startsWith("Dra.") -> fotosMujer[medico.id % fotosMujer.size]
        else -> R.drawable.medico_luis_ramirez
    }
}

/** Texto que va bajo el nombre del médico ("Ginecóloga", "Cardiólogo"...). */
fun cargoMedico(medico: Medico, especialidad: Especialidad?): String {
    val mujer = medico.nombre.startsWith("Dra.")
    return when (especialidad?.nombre) {
        "Medicina General" -> if (mujer) "Médica general" else "Médico general"
        "Pediatría" -> "Pediatra"
        "Ginecología" -> if (mujer) "Ginecóloga" else "Ginecólogo"
        "Cardiología" -> if (mujer) "Cardióloga" else "Cardiólogo"
        "Dermatología" -> if (mujer) "Dermatóloga" else "Dermatólogo"
        "Traumatología" -> if (mujer) "Traumatóloga" else "Traumatólogo"
        "Oftalmología" -> if (mujer) "Oftalmóloga" else "Oftalmólogo"
        else -> especialidad?.nombre ?: ""
    }
}

/** Foto circular del médico (las fotos son imágenes; no existe un ícono equivalente). */
@Composable
fun FotoMedico(medico: Medico, tamano: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(fotoMedico(medico)),
        contentDescription = medico.nombre,
        modifier = modifier
            .size(tamano)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}
