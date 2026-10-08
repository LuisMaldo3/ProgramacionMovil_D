package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal

@Composable
fun TerminosScreen(navController: NavController) {
    Scaffold(
        topBar = { BarraSuperior("Términos y condiciones", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
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
                            "Estos términos pueden actualizarse y se notificará dentro de la aplicación."
                )
            }
            Spacer(Modifier.height(12.dp))
            BotonPrincipal("Entendido", onClick = { navController.popBackStack() })
        }
    }
}