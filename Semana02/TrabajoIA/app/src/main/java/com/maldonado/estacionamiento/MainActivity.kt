package com.maldonado.estacionamiento

// Commit 1: Ingreso de datos
// Se captura placa, tipo, horas y nombre del cliente.
// (El estado de "cliente frecuente" ya NO se pide manualmente:
// se calculará en el Commit 2 en base a las horas de permanencia)

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Vehiculo(
    val placa: String,
    val tipo: String,
    val horas: Int,
    val cliente: String
)

class MainActivity : AppCompatActivity() {

    private val TIPOS_VEHICULO = listOf("moto", "auto", "camioneta")
    private val vehiculos = mutableListOf<Vehiculo>()

    private lateinit var etPlaca: EditText
    private lateinit var spTipo: Spinner
    private lateinit var etHoras: EditText
    private lateinit var etCliente: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPlaca = findViewById(R.id.etPlaca)
        spTipo = findViewById(R.id.spTipo)
        etHoras = findViewById(R.id.etHoras)
        etCliente = findViewById(R.id.etCliente)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        spTipo.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, TIPOS_VEHICULO
        )

        btnRegistrar.setOnClickListener { registrarVehiculo() }
    }

    private fun registrarVehiculo() {
        val placa = etPlaca.text.toString().trim().uppercase()
        val tipo = spTipo.selectedItem.toString()
        val horas = etHoras.text.toString().toIntOrNull()
        val cliente = etCliente.text.toString().trim()

        if (placa.isEmpty() || cliente.isEmpty() || horas == null || horas < 1) {
            Toast.makeText(
                this,
                "Datos inválidos. Mínimo 1 hora y todos los campos completos.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        vehiculos.add(Vehiculo(placa, tipo, horas, cliente))
        Toast.makeText(this, "Vehículo registradoo: $placa", Toast.LENGTH_SHORT).show()

        etPlaca.text.clear()
        etHoras.text.clear()
        etCliente.text.clear() hip hop
    }
}