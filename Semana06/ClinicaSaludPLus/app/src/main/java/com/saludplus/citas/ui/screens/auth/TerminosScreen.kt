package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

@Composable
fun TerminosScreen(navController: NavController) {
    val e = rememberEscala()
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            BarraSuperior("Términos y condiciones", onAtras = { navController.popBackStack() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .navigationBarsPadding()
                .padding(horizontal = e.d(17), vertical = e.d(16))
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())) {
                Text(
                    "1. Uso de la aplicación\n" +
                            "SaludPlus Citas permite agendar citas médicas con los profesionales de la clínica.\n\n" +
                            "2. Datos personales\n" +
                            "Tus datos se usan únicamente para gestionar tus citas y no se comparten con terceros.\n\n" +
                            "3. Cancelaciones\n" +
                            "Puedes cancelar una cita desde el detalle de la misma cuando lo necesites.\n\n" +
                            "4. Responsabilidad\n" +
                            "La información mostrada no reemplaza una consulta médica presencial.\n\n" +
                            "5. Cambios\n" +
                            "Estos términos pueden actualizarse y se notificará dentro de la aplicación.",
                    fontSize = e.s(18),
                    lineHeight = e.s(26),
                    color = TextoSecundario
                )
            }
            Spacer(Modifier.height(e.d(12)))
            BotonPrincipal("Entendido", onClick = { navController.popBackStack() })
        }
    }
}
