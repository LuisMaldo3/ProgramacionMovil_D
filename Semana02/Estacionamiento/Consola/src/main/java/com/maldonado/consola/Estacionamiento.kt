package com.maldonado.consola

/*
 * CONTROL DE ESTACIONAMIENTO - programa de consola en Kotlin
 * Commit 2: Calculos
 *  - Tarifa basica por tipo: Moto S/ 2, Auto S/ 4, Camioneta S/ 10
 *  - Hasta 2 horas tarifa normal; horas 3 a 5 recargo 20 %; desde la hora 6 recargo 50 %
 *  - Cliente frecuente (mas de 3 visitas con la misma placa): 10 % de descuento sobre el total
 *  - Muestra tarifa basica, subtotal, descuento y total
 */

// =============================== 1. INGRESO DE DATOS ===============================

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

// =============================== 2. CALCULOS ===============================

data class DetalleHora(val hora: Int, val tarifa: Double, val recargoPct: Int, val importe: Double)

data class Boleta(
    val numero: Int,
    val vehiculo: Vehiculo,
    val tarifaBasica: Double,
    val detalle: List<DetalleHora>,
    val subtotal: Double,
    val esFrecuente: Boolean,
    val visitas: Int,
    val descuento: Double,
    val total: Double
)

object CalculadoraTarifa {

    /** Tarifa basica por hora segun el tipo de vehiculo. */
    fun tarifaBasica(tipo: String): Double = when (tipo) {
        "Moto" -> 2.0
        "Auto" -> 4.0
        "Camioneta" -> 10.0
        else -> 0.0
    }

    /** Recargo de cada hora: 0 % (horas 1-2), 20 % (horas 3-5), 50 % (hora 6 en adelante). */
    fun recargoPorHora(hora: Int): Int = when {
        hora <= 2 -> 0
        hora <= 5 -> 20
        else -> 50
    }

    private var contadorBoletas = 0

    fun calcular(v: Vehiculo): Boleta {
        require(v.horas >= 1) { "Ningun vehiculo puede registrarse con menos de 1 hora" }

        // Cliente frecuente: se decide por el historial de la placa ANTES de registrar la visita actual
        val esFrecuente = RegistroClientes.esFrecuente(v.placa)
        val visitas = RegistroClientes.registrarVisita(v.placa)

        val tarifa = tarifaBasica(v.tipo)
        val detalle = (1..v.horas).map { h ->
            val pct = recargoPorHora(h)
            DetalleHora(h, tarifa, pct, tarifa * (1 + pct / 100.0))
        }
        val subtotal = detalle.sumOf { it.importe }
        val descuento = if (esFrecuente) subtotal * 0.10 else 0.0
        contadorBoletas++
        return Boleta(contadorBoletas, v, tarifa, detalle, subtotal, esFrecuente, visitas, descuento, subtotal - descuento)
    }
}

/** Historial de visitas por placa. Frecuente = mas de 3 visitas (desde la 4.a). */
object RegistroClientes {
    const val VISITAS_PARA_FRECUENTE = 3
    private val visitasPorPlaca = mutableMapOf<String, Int>()

    fun visitasPrevias(placa: String): Int = visitasPorPlaca[placa] ?: 0
    fun esFrecuente(placa: String): Boolean = visitasPrevias(placa) >= VISITAS_PARA_FRECUENTE
    fun registrarVisita(placa: String): Int {
        val n = visitasPrevias(placa) + 1
        visitasPorPlaca[placa] = n
        return n
    }
}

// =============================== PANTALLA ===============================

object Pantalla {
    private const val ANCHO = 46
    private fun s(v: Double) = "%.2f".format(v)
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
        println("  Tarifas por hora: Moto S/ 2 | Auto S/ 4 | Camioneta S/ 10")
        println(linea())
        println("  1) Registrar vehiculo y calcular")
        println("  2) Ver vehiculos registrados")
        println("  3) Salir")
        println(linea())
    }

    fun resultado(b: Boleta) {
        val v = b.vehiculo
        println()
        println(linea())
        println("  Placa   : ${v.placa}    Tipo: ${v.tipo}    Horas: ${v.horas}")
        println("  Cliente : ${v.cliente}")
        println(linea())
        println(String.format("  %-28s S/ %8s", "Tarifa basica (por hora)", s(b.tarifaBasica)))
        println(String.format("  %-28s S/ %8s", "Subtotal", s(b.subtotal)))
        if (b.esFrecuente) {
            println(String.format("  %-28s %s", "Cliente frecuente", "SI (${b.visitas} visitas)"))
            println(String.format("  %-28s S/ %8s", "Descuento 10 %", "-" + s(b.descuento)))
        } else {
            println(String.format("  %-28s %s", "Cliente frecuente", "NO (${b.visitas} de ${RegistroClientes.VISITAS_PARA_FRECUENTE + 1} visitas)"))
        }
        println(linea('='))
        println(String.format("  %-28s S/ %8s", "TOTAL A PAGAR", s(b.total)))
        println(linea('='))
    }

    fun registrados(lista: List<Boleta>) {
        titulo("VEHICULOS REGISTRADOS")
        if (lista.isEmpty()) {
            println("  Aun no hay vehiculos registrados.")
            return
        }
        println(String.format("  %-4s %-9s %-10s %-5s %-16s %s", "No.", "Placa", "Tipo", "Hrs", "Cliente", "Total"))
        println(linea())
        for (b in lista) {
            val cliente = if (b.vehiculo.cliente.length > 15) b.vehiculo.cliente.substring(0, 15) else b.vehiculo.cliente
            println(String.format("  %-4s %-9s %-10s %-5d %-16s S/ %s",
                "%03d".format(b.numero), b.vehiculo.placa, b.vehiculo.tipo, b.vehiculo.horas, cliente, s(b.total)))
        }
        println(linea())
        println("  Recaudado: S/ ${s(lista.sumOf { it.total })}")
    }
}

// =============================== PROGRAMA PRINCIPAL ===============================

val boletas = mutableListOf<Boleta>()

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
    val boleta = CalculadoraTarifa.calcular(RegistroVehiculos.ultimo())
    boletas.add(boleta)
    Pantalla.resultado(boleta)
}

fun main() {
    while (true) {
        Pantalla.menu()
        when (leer("  Opcion: ")) {
            "1" -> registrarVehiculo()
            "2" -> Pantalla.registrados(boletas)
            "3" -> {
                println("  Programa finalizado. Recaudado: S/ ${"%.2f".format(boletas.sumOf { it.total })}")
                return
            }
            else -> println("  [X] Opcion no valida. Elige 1, 2 o 3.")
        }
    }
}