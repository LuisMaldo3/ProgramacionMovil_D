package com.maldonado.ejercicioa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Compartimos los colores entre todas las pantallas.
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = MoradoClinica,
                    onPrimary = Color.White,
                    background = Color.White,
                    surface = Color.White
                )
            ) {
                // Mostramos la estructura que incluye el menú y la navegación.
                ClinicaApp()
            }
        }
    }
}