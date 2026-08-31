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
 * COMMIT 2 — Cálculos.
 * Usa CalculadoraTarifa para obtener tarifa básica, subtotal, descuento
 * y total de cada vehículo registrado, y lo muestra en consola y pantalla.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingresarDatos()
        val salida = calcular()
        consola(salida)
        mostrarEnPantalla(salida)
    }

    private fun ingresarDatos() {
        // Misma placa ABC-123 cuatro veces para demostrar el cliente frecuente
        RegistroVehiculos.ingresar("abc-123", "Auto", 3, "Juan Pérez")
        RegistroVehiculos.ingresar("XYZ-789", "Moto", 1, "María Salas")
        RegistroVehiculos.ingresar("DEF-456", "Camioneta", 6, "Pedro Gonzales")
        RegistroVehiculos.ingresar("ABC-123", "Auto", 2, "Juan Pérez")
        RegistroVehiculos.ingresar("ABC-123", "Auto", 4, "Juan Pérez")
        RegistroVehiculos.ingresar("ABC-123", "Auto", 6, "Juan Pérez")
    }

    private fun calcular(): String {
        val sb = StringBuilder()
        sb.appendLine("🅿 CONTROL DE ESTACIONAMIENTO — CÁLCULOS")
        sb.appendLine("--------------------------------------")
        for (v in RegistroVehiculos.listar()) {
            val r = CalculadoraTarifa.calcular(v)
            sb.appendLine("Placa ${v.placa} · ${v.tipo} · ${v.horas} h · ${v.cliente}")
            sb.appendLine("  Tarifa básica: S/ ${"%.2f".format(r.tarifaBasica)}")
            sb.appendLine("  Subtotal:      S/ ${"%.2f".format(r.subtotal)}")
            sb.appendLine("  Frecuente:     ${if (r.esFrecuente) "SÍ" else "NO"} (${r.visitas} visitas)")
            sb.appendLine("  Descuento 10%: S/ ${"%.2f".format(r.descuento)}")
            sb.appendLine("  TOTAL:         S/ ${"%.2f".format(r.total)}")
            sb.appendLine("--------------------------------------")
        }
        return sb.toString()
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