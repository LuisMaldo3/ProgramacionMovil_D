package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.saludplus.citas.R
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.LogoSaludPlus
import com.saludplus.citas.ui.theme.Azul
import com.saludplus.citas.ui.theme.AzulMarca
import com.saludplus.citas.ui.theme.FondoSplashAbajo
import com.saludplus.citas.ui.theme.FondoSplashArriba
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.rememberEscala

/**
 * Splash en tres bloques que siempre caben en la pantalla:
 * 1) logo y textos arriba, 2) la ilustración ocupa TODO el espacio que sobra en el medio,
 * 3) botón "Comenzar" y enlace abajo (respetando la barra de navegación del teléfono).
 */
@Composable
fun SplashScreen(navController: NavController) {
    val e = rememberEscala()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to FondoSplashArriba,
                    0.45f to FondoSplashAbajo,
                    1f to FondoSplashAbajo
                )
            )
    ) {
        // 1) Logo y textos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = e.d(20)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoSaludPlus()
            Spacer(Modifier.height(e.d(8)))
            Text(
                "Clínica",
                fontSize = e.s(36),
                lineHeight = e.s(40),
                fontWeight = FontWeight.Bold,
                color = AzulMarca
            )
            Text(
                "SaludPlus",
                fontSize = e.s(45),
                lineHeight = e.s(50),
                fontWeight = FontWeight.Bold,
                color = AzulMarca
            )
            Spacer(Modifier.height(e.d(6)))
            Text(
                "Tu salud, nuestra prioridad",
                fontSize = e.s(22),
                lineHeight = e.s(26),
                color = TextoSecundario
            )
        }

        // 2) Ilustración: ocupa todo el ancho y todo el alto que sobre
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.ilustracion_bienvenida_saludplus),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alignment = BiasAlignment(0f, -0.3f),
                contentScale = ContentScale.Crop
            )
            // Fundido superior para que la imagen se mezcle con el fondo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(e.d(30))
                    .background(Brush.verticalGradient(listOf(FondoSplashAbajo, Color.Transparent)))
            )
            // Fundido inferior: la bata del doctor se mezcla con el fondo del pie
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(e.d(40))
                    .background(Brush.verticalGradient(listOf(Color.Transparent, FondoSplashAbajo)))
            )
        }

        // 3) Botón y enlace
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FondoSplashAbajo)
                .navigationBarsPadding()
                .padding(start = e.d(22), end = e.d(22), top = e.d(10), bottom = e.d(14)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Rutas.REGISTRO) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(e.d(55)),
                shape = RoundedCornerShape(e.d(12)),
                colors = ButtonDefaults.buttonColors(containerColor = Azul, contentColor = Color.White)
            ) {
                Text("Comenzar", fontSize = e.s(20), fontWeight = FontWeight.SemiBold)
            }
            Text(
                "Ya tengo una cuenta",
                modifier = Modifier
                    .clickable { navController.navigate(Rutas.LOGIN) }
                    .padding(vertical = e.d(12)),
                fontSize = e.s(18),
                fontWeight = FontWeight.Bold,
                color = Azul
            )
        }
    }
}
