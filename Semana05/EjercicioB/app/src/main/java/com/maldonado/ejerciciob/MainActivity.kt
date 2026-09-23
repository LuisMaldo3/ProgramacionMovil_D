package com.maldonado.ejerciciob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Utilizamos los colores compartidos de DatosFit.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = VerdeFit,
                    onPrimary = Color.White,
                    background = Color.White,
                    surface = Color.White
                )
            ) {
                Scaffold(
                    containerColor = Color.White,
                    topBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(VerdeFit)
                                .windowInsetsPadding(WindowInsets.statusBars)
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 12.dp
                                )
                        ) {
                            Text(
                                text = "TECSUP Fit",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = "Hola, Diego",
                                fontSize = 12.sp,
                                color = Color(0xFFD8EEE7)
                            )
                        }
                    }
                ) { padding ->
                    // La lista comienza después del encabezado.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        InicioScreen(
                            // Conectaremos la tarjeta con el detalle en otro avance.
                            alElegirClase = {}
                        )
                    }
                }
            }
        }
    }
}