package com.maldonado.ejercicioa

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
            // Mantenemos el fondo blanco y el color principal morado.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = MoradoClinica,
                    onPrimary = Color.White,
                    background = Color.White,
                    surface = Color.White
                )
            ) {
                Scaffold(
                    containerColor = Color.White,

                    // El título y el saludo están juntos dentro del encabezado.
                    topBar = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MoradoClinica)
                                .windowInsetsPadding(
                                    WindowInsets.statusBars
                                )
                                .padding(
                                    horizontal = 18.dp,
                                    vertical = 12.dp
                                )
                        ) {
                            Text(
                                text = "Clínica Salud+",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = "Hola, Juan",
                                fontSize = 12.sp,
                                color = Color(0xFFE5D5EF)
                            )
                        }
                    }
                ) { padding ->

                    // Respetamos el espacio del encabezado y las barras del sistema.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        // Conectaremos las tarjetas y el menú en el siguiente avance.
                        InicioScreen(
                            alElegirMedico = {}
                        )
                    }
                }
            }
        }
    }
}