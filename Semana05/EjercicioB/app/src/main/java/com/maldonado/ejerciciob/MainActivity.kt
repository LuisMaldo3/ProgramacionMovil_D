package com.maldonado.ejerciciob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Aplicamos los colores principales de TECSUP Fit.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF10745E),
                    onPrimary = Color.White,
                    background = Color.White,
                    surface = Color.White
                )
            ) {
                Scaffold(
                    containerColor = Color.White,
                    topBar = {
                        // El encabezado reúne el nombre de la aplicación y el saludo.
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF10745E))
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
                    // Respetamos el espacio ocupado por el encabezado.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Clases disponibles",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF292929)
                        )
                    }
                }
            }
        }
    }
}