package com.maldonado.consola

/*
 * CONTROL DE ESTACIONAMIENTO - programa de consola en Kotlin
 * Commit 1: Ingreso de datos
 *  - Registro de placa (formato ABC-123), tipo (Moto / Auto / Camioneta), horas y cliente
 *  - Ningun vehiculo puede registrarse con menos de 1 hora
 *  - Menu con opcion de ver los vehiculos registrados y salir
 */

data class Vehiculo(val placa: String, val tipo: String, val horas: Int, val cliente: String)

object Validador {
    private val formatoPlaca = Regex("^[A-Z0-9]{3}-?[A-Z0-9]{3}$")

    fun normalizarPlaca(texto: String): String? {
        val p = texto.trim().uppercase().replace(" ", "")
        if (!formatoPlaca.matches(p)) return null
        return if (p.contains("-")) p else p.substring(0, 3) + "-" + p.substring(3)
    }
}

object RegistroVehiculos {
    val tiposPermitidos = listOf("Moto", "Auto", "Camioneta")
    private val vehiculos = mutableListOf<Vehiculo>()

    fun ingresar(placa: String, tipo: String, horas: Int, cliente: String): String? {
        val placaOk = Validador.normalizarPlaca(placa)
            ?: return "Placa invalida. Use el formato ABC-123 (3 caracteres, guion, 3 caracteres)"
        if (tipo !in tiposPermitidos) return "Tipo invalido. Use Moto, Auto o Camioneta"
        if (horas < 1) return "Ningun vehiculo puede registrarse con menos de 1 hora"
        if (cliente.isBlank()) return "El nombre del cliente es obligatorio"
        vehiculos.add(Vehiculo(placaOk, tipo, horas, cliente.trim()))
        return null
    }

    fun ultimo(): Vehiculo = vehiculos.last()
    fun listar(): List<Vehiculo> = vehiculos
}

object Pantalla {
    private const val ANCHO = 46
    private fun linea(c: Char = '-') = c.toString().repeat(ANCHO)
    private fun centrado(t: String) = " ".repeat(((ANCHO - t.length) / 2).coerceAtLeast(0)) + t

    fun titulo(t: String) {
        println()
        println(linea('='))
        println(centrado(t))
        println(linea('='))
    }

    fun menu() {
        titulo("CONTROL DE ESTACIONAMIENTO")
        println("  1) Registrar vehiculo")
        println("  2) Ver vehiculos registrados")
        println("  3) Salir")
        println(linea())
    }

    fun registrados(lista: List<Vehiculo>) {
        titulo("VEHICULOS REGISTRADOS")
        if (lista.isEmpty()) {
            println("  Aun no hay vehiculos registrados.")
            return
        }
        println(String.format("  %-4s %-9s %-10s %-5s %s", "No.", "Placa", "Tipo", "Hrs", "Cliente"))
        println(linea())
        lista.forEachIndexed { i, v ->
            println(String.format("  %-4s %-9s %-10s %-5d %s", "%03d".format(i + 1), v.placa, v.tipo, v.horas, v.cliente))
        }
        println(linea())
        println("  Total registrados: ${lista.size}")
    }
}

fun leer(mensaje: String): String {
    print(mensaje)
    return readLine()?.trim() ?: ""
}

fun registrarVehiculo() {
    Pantalla.titulo("REGISTRO DE VEHICULO")
    val placa = leer("  Placa (ej. ABC-123): ")
    println("  Tipo: 1) Moto   2) Auto   3) Camioneta")
    val tipo = when (leer("  Elige (1-3): ")) {
        "1" -> "Moto"
        "2" -> "Auto"
        "3" -> "Camioneta"
        else -> ""
    }
    val horas = leer("  Horas de permanencia: ").toIntOrNull() ?: 0
    val cliente = leer("  Nombre del cliente: ")

    val error = RegistroVehiculos.ingresar(placa, tipo, horas, cliente)
    if (error != null) {
        println()
        println("  [X] No se registro: $error")
        return
    }
    val v = RegistroVehiculos.ultimo()
    println()
    println("  [OK] Registrado: ${v.placa} | ${v.tipo} | ${v.horas} h | ${v.cliente}")
}

fun main() {
    while (true) {
        Pantalla.menu()
        when (leer("  Opcion: ")) {
            "1" -> registrarVehiculo()
            "2" -> Pantalla.registrados(RegistroVehiculos.listar())
            "3" -> {
                println("  Programa finalizado. Vehiculos registrados: ${RegistroVehiculos.listar().size}")
                return
            }
            else -> println("  [X] Opcion no valida. Elige 1, 2 o 3.")
        }
    }
}