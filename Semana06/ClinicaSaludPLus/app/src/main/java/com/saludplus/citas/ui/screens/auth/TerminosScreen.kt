package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.RellenoIconoConfirmar
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.CalendarTimes
import compose.icons.fontawesomeicons.solid.ExclamationCircle
import compose.icons.fontawesomeicons.solid.FileContract
import compose.icons.fontawesomeicons.solid.ShieldAlt
import compose.icons.fontawesomeicons.solid.Sync

private class Apartado(val icono: ImageVector, val titulo: String, val texto: String)

@Composable
fun TerminosScreen(navController: NavController) {
    val e = rememberEscala()
    val apartados = listOf(
        Apartado(
            FontAwesomeIcons.Solid.FileContract, "Uso de la aplicación",
            "SaludPlus Citas permite agendar citas médicas con los profesionales de la clínica."
        ),
        Apartado(
            FontAwesomeIcons.Solid.ShieldAlt, "Datos personales",
            "Tus datos se usan únicamente para gestionar tus citas y no se comparten con terceros."
        ),
        Apartado(
            FontAwesomeIcons.Solid.CalendarTimes, "Cancelaciones",
            "Puedes cancelar una cita desde el detalle de la misma cuando lo necesites."
        ),
        Apartado(
            FontAwesomeIcons.Solid.ExclamationCircle, "Responsabilidad",
            "La información mostrada no reemplaza una consulta médica presencial."
        ),
        Apartado(
            FontAwesomeIcons.Solid.Sync, "Cambios",
            "Estos términos pueden actualizarse y se notificará dentro de la aplicación."
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BarraSuperior("Términos y condiciones", onAtras = { navController.popBackStack() })

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = e.d(20), vertical = e.d(8))
        ) {
            Text(
                "Lee con atención cómo funciona la aplicación antes de crear tu cuenta.",
                fontSize = e.s(18),
                lineHeight = e.s(26),
                color = TextoSecundario
            )
            Spacer(Modifier.height(e.d(22)))
            apartados.forEachIndexed { indice, apartado ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(e.d(44))
                            .clip(RoundedCornerShape(e.d(12)))
                            .background(RellenoIconoConfirmar),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = apartado.icono,
                            contentDescription = null,
                            tint = Azul,
                            modifier = Modifier.size(e.d(22))
                        )
                    }
                    Spacer(Modifier.width(e.d(16)))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "${indice + 1}. ${apartado.titulo}",
                            fontSize = e.s(19),
                            lineHeight = e.s(24),
                            fontWeight = FontWeight.Bold,
                            color = TextoPrincipal
                        )
                        Spacer(Modifier.height(e.d(4)))
                        Text(
                            apartado.texto,
                            fontSize = e.s(17),
                            lineHeight = e.s(24),
                            color = TextoSecundario
                        )
                    }
                }
                Spacer(Modifier.height(e.d(22)))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = e.d(17), end = e.d(17), top = e.d(8), bottom = e.d(20))
        ) {
            BotonPrincipal("Aceptar y continuar", onClick = {
                // Le avisa a Registro para que marque la casilla de aceptación
                navController.previousBackStackEntry?.savedStateHandle?.set(CLAVE_TERMINOS, true)
                navController.popBackStack()
            })
        }
    }
}
