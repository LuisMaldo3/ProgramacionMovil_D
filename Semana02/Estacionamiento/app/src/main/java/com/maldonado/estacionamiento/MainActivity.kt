package com.maldonado.estacionamiento

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val TAG = "ESTACIONAMIENTO"

/** Imprime en la consola (Logcat) línea por línea. Filtrar en Logcat con: tag:ESTACIONAMIENTO */
fun consola(texto: String) {
    texto.lines().forEach { Log.d(TAG, it) }
}

/**
 * COMMIT 3 — Mostrar resultados.
 * Genera el reporte completo (tarifa básica, tabla Hora · Tarifa · Recargo · Importe,
 * cliente frecuente, descuento y total) y lo muestra en consola y pantalla.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingresarDatos()
        val resultados = RegistroVehiculos.listar().map { CalculadoraTarifa.calcular(it) }
        val salida = Reporte.completo(resultados)
        consola(salida)
        mostrarEnPantalla(salida)
    }

    private fun ingresarDatos() {
        RegistroVehiculos.ingresar("abc-123", "Auto", 3, "Juan Pérez")        // ejemplo de la pizarra → 12.80
        RegistroVehiculos.ingresar("XYZ-789", "Moto", 1, "María Salas")
        RegistroVehiculos.ingresar("DEF-456", "Camioneta", 6, "Pedro Gonzales")
        RegistroVehiculos.ingresar("ABC-123", "Auto", 2, "Juan Pérez")
        RegistroVehiculos.ingresar("ABC-123", "Auto", 4, "Juan Pérez")
        RegistroVehiculos.ingresar("ABC-123", "Auto", 6, "Juan Pérez")        // 4.ª visita → frecuente, 10 %
        val error = RegistroVehiculos.ingresar("GHI-000", "Auto", 0, "Cliente Error")
        if (error != null) consola(error)                                       // se rechaza: menos de 1 hora
    }

    /** Muestra el mismo texto en la pantalla del celular/emulador. */
    private fun mostrarEnPantalla(texto: String) {
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(text = texto, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}