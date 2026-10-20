package com.tecsup.huellitasvet

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecsup.huellitasvet.ui.navigation.NavVet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Barra de estado clara, como en el mockup de HuellitasVet.
        // En Android 15+ el sistema puede aplicar edge-to-edge automáticamente.
        @Suppress("DEPRECATION")
        window.statusBarColor = android.graphics.Color.rgb(246, 253, 249)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContent { NavVet(application as VetApp) }
    }
}
