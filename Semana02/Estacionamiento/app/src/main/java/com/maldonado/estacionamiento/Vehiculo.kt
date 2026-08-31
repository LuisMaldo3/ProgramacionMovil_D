package com.maldonado.estacionamiento

/**
 * COMMIT 1 — Ingreso de datos.
 * Datos que se registran de cada vehículo y la validación de ingreso.
 */
data class Vehiculo(
    val placa: String,
    val tipo: String,      // "Moto", "Auto" o "Camioneta"
    val horas: Int,
    val cliente: String
)

object RegistroVehiculos {
    val tiposPermitidos = listOf("Moto", "Auto", "Camioneta")
    private val vehiculos = mutableListOf<Vehiculo>()

    /**
     * Registra un vehículo. Devuelve un mensaje de error si los datos
     * no cumplen las reglas, o null si se registró correctamente.
     */
    fun ingresar(placa: String, tipo: String, horas: Int, cliente: String): String? {
        if (placa.isBlank()) return "Error: la placa es obligatoria"
        if (cliente.isBlank()) return "Error: el nombre del cliente es obligatorio"
        if (tipo !in tiposPermitidos) return "Error: tipo inválido ($tipo). Use Moto, Auto o Camioneta"
        if (horas < 1) return "Error: ningún vehículo puede registrarse con menos de 1 hora (placa $placa)"

        vehiculos.add(Vehiculo(placa.trim().uppercase(), tipo, horas, cliente.trim()))
        return null
    }

    fun listar(): List<Vehiculo> = vehiculos
}