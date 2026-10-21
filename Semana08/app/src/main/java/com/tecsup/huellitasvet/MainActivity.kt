package com.tecsup.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecsup.huellitasvet.ui.navigation.NavVet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // VetTheme ajusta las barras del sistema según el perfil actual.
        setContent { NavVet(application as VetApp) }
    }
}
