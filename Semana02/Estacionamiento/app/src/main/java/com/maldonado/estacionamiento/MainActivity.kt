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
 * COMMIT 1 — Ingreso de datos.
 * Registra los vehículos (placa, tipo, horas, cliente), valida las reglas de
 * ingreso y muestra lo registrado en la consola (Logcat) y en pantalla.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val salida = ingresarDatos()
        consola(salida)
        mostrarEnPantalla(salida)
    }

    private fun ingresarDatos(): String {
        val sb = StringBuilder()
        sb.appendLine("🅿 CONTROL DE ESTACIONAMIENTO — INGRESO DE DATOS")
        sb.appendLine("Tipos permitidos: ${RegistroVehiculos.tiposPermitidos}")
        sb.appendLine("--------------------------------------")

        // Datos de ingreso: placa, tipo, horas, cliente
        val ingresos = listOf(
            arrayOf("abc-123", "Auto", 3, "Juan Pérez"),
            arrayOf("XYZ-789", "Moto", 1, "María Salas"),
            arrayOf("DEF-456", "Camioneta", 6, "Pedro Gonzales"),
            arrayOf("GHI-000", "Auto", 0, "Cliente Error"),      // ← menos de 1 hora: debe rechazarse
            arrayOf("JKL-111", "Bicicleta", 2, "Ana Castro")    // ← tipo inválido: debe rechazarse
        )

        for (d in ingresos) {
            val error = RegistroVehiculos.ingresar(d[0] as String, d[1] as String, d[2] as Int, d[3] as String)
            if (error != null) sb.appendLine("✖ $error")
            else sb.appendLine("✔ Registrado: ${(d[0] as String).uppercase()} · ${d[1]} · ${d[2]} h · ${d[3]}")
        }

        sb.appendLine("--------------------------------------")
        sb.appendLine("VEHÍCULOS REGISTRADOS: ${RegistroVehiculos.listar().size}")
        for (v in RegistroVehiculos.listar()) {
            sb.appendLine("Placa: ${v.placa} | Tipo: ${v.tipo} | Horas: ${v.horas} | Cliente: ${v.cliente}")
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