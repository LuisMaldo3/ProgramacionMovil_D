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
                //REAUNE EL ENZABEZADO, LAS PESTAÑAS Y NAVEGACION.
                FitApp()
            }
        }
    }
}