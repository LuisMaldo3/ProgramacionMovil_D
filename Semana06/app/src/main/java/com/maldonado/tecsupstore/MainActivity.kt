package com.maldonado.tecsupstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maldonado.tecsupstore.navigation.AppNavegacion
import com.maldonado.tecsupstore.ui.theme.TecsupStoreTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            TecsupStoreTheme {

                AppNavegacion()
            }
        }
    }
}