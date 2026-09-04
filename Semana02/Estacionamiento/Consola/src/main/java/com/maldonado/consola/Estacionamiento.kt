package com.maldonado.consola

/*
 * CONTROL DE ESTACIONAMIENTO - programa de consola en Kotlin
 *
 * Commit 1: Ingreso de datos     -> registro de placa, tipo, horas y cliente con validaciones
 * Commit 2: Calculos             -> tarifa basica, recargos por hora, cliente frecuente y descuento
 * Commit 3: Mostrar resultados   -> boleta con tabla Hora/Tarifa/Recargo/Importe, historial y boleta por placa
 *
 * Reglas:
 *  - Tarifa por hora: Moto S/ 2, Auto S/ 4, Camioneta S/ 10
 *  - Hasta 2 horas: tarifa normal
 *  - Mas de 2 y hasta 5 horas: recargo del 20 % en esas horas
 *  - Mas de 5 horas: recargo del 50 % en las horas posteriores a la quinta
 *  - Cliente frecuente (mas de 3 visitas con la misma placa): 10 % de descuento sobre el total
 *  - Ningun vehiculo puede registrarse con menos de 1 hora
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
    val tiposPermitidos = listOf("Moto", "Auto", "Camioneta", "Trailer")
    private val vehiculos = mutableListOf<Vehiculo>()
    var aforoMaximo: Int = Int.MAX_VALUE

    fun ingresar(placa: String, tipo: String, horas: Int, cliente: String): String? {
        val placaOk = Validador.normalizarPlaca(placa)
            ?: return "Placa invalida. Use el formato ABC-123 (3 caracteres, guion, 3 caracteres)"
        if (tipo !in tiposPermitidos) return "Tipo invalido. Use Moto, Auto, Camioneta o Trailer"
        if (horas < 1) return "Ningun vehiculo puede registrarse con menos de 1 hora"
        if (cliente.isBlank()) return "El nombre del cliente es obligatorio"
        if (vehiculos.size >= aforoMaximo) return "Estacionamiento lleno. No se pueden registrar mas vehiculos (aforo maximo: $aforoMaximo)"
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

    fun tarifaBasica(tipo: String): Double = when (tipo) {
        "Moto" -> 2.0
        "Auto" -> 4.0
        "Camioneta" -> 10.0
        "Trailer" -> 20.0
        else -> 0.0
    }

    /** Recargo de cada hora: 0 % (horas 1-2), 20 % (horas 3-5), 50 % (hora 6 en adelante). */
    fun recargoPorHora(tipo: String, hora: Int): Int = when (tipo) {
        "Trailer" -> when {
            hora <= 2 -> 0
            hora <= 5 -> 20
            hora <= 10 -> 40
            else -> 50
        }
        else -> when {
            hora <= 2 -> 0
            hora <= 5 -> 20
            else -> 50
        }
    }

    private var contadorBoletas = 0

    fun calcular(v: Vehiculo): Boleta {
        require(v.horas >= 1) { "Ningun vehiculo puede registrarse con menos de 1 hora" }

        val esFrecuente = RegistroClientes.esFrecuente(v.placa)
        val visitas = RegistroClientes.registrarVisita(v.placa)

        val tarifa = tarifaBasica(v.tipo)
        val detalle = (1..v.horas).map { h ->
            val pct = recargoPorHora(v.tipo, h)
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

// =============================== 3. MOSTRAR RESULTADOS ===============================

object Historial {
    private val boletas = mutableListOf<Boleta>()
    fun agregar(b: Boleta) = boletas.add(b)
    fun todas(): List<Boleta> = boletas
    fun porPlaca(placa: String): List<Boleta> = boletas.filter { it.vehiculo.placa == placa }
}

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
        println("  Tarifas por hora: Moto S/ 2 | Auto S/ 4 | Camioneta S/ 10 | Trailer S/ 20")
        println(linea())
        println("  1) Registrar vehiculo")
        println("  2) Ver historial del dia")
        println("  3) Buscar boleta por placa")
        println("  4) Salir")
        println(linea())
    }

    fun boleta(b: Boleta) {
        val v = b.vehiculo
        println()
        println(linea('='))
        println(centrado("BOLETA No. ${"%03d".format(b.numero)}"))
        println(linea('='))
        println("  Placa   : ${v.placa}")
        println("  Tipo    : ${v.tipo}")
        println("  Horas   : ${v.horas}")
        println("  Cliente : ${v.cliente}")
        println("  Tarifa basica: S/ ${s(b.tarifaBasica)}")
        println(linea())
        println(String.format("  %-6s %-10s %-10s %s", "Hora", "Tarifa", "Recargo", "Importe"))
        println(linea())
        for (d in b.detalle) {
            println(String.format("  %-6d %-10s %-10s %s", d.hora, s(d.tarifa), "${d.recargoPct} %", s(d.importe)))
        }
        println(linea())
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

    fun historial(boletas: List<Boleta>) {
        titulo("HISTORIAL DEL DIA")
        if (boletas.isEmpty()) {
            println("  Aun no hay vehiculos registrados.")
            return
        }
        println(String.format("  %-4s %-9s %-10s %-5s %-16s %s", "No.", "Placa", "Tipo", "Hrs", "Cliente", "Total"))
        println(linea())
        for (b in boletas) {
            val cliente = if (b.vehiculo.cliente.length > 15) b.vehiculo.cliente.substring(0, 15) else b.vehiculo.cliente
            println(String.format("  %-4s %-9s %-10s %-5d %-16s S/ %s",
                "%03d".format(b.numero), b.vehiculo.placa, b.vehiculo.tipo, b.vehiculo.horas, cliente, s(b.total)))
        }
        println(linea())
        println(String.format("  Vehiculos: %-3d  Descuentos: S/ %-8s  Recaudado: S/ %s",
            boletas.size, s(boletas.sumOf { it.descuento }), s(boletas.sumOf { it.total })))
    }

    fun resumenFinal(boletas: List<Boleta>) {
        titulo("RESUMEN DEL DIA")
        println("  Vehiculos atendidos : ${boletas.size}")
        println("  Total sin descuento : S/ ${s(boletas.sumOf { it.subtotal })}")
        println("  Total descuentos    : S/ ${s(boletas.sumOf { it.descuento })}")
        println("  TOTAL RECAUDADO     : S/ ${s(boletas.sumOf { it.total })}")
        println(linea('='))
        println("  Gracias. Programa finalizado.")
    }
}

// =============================== PROGRAMA PRINCIPAL ===============================

fun leer(mensaje: String): String {
    print(mensaje)
    return readLine()?.trim() ?: ""
}

fun registrarVehiculo() {
    Pantalla.titulo("REGISTRO DE VEHICULO")
    val placa = leer("  Placa (ej. ABC-123): ")
    println("  Tipo: 1) Moto   2) Auto   3) Camioneta   4) Trailer")
    val tipo = when (leer("  Elige (1-4): ")) {
        "1" -> "Moto"
        "2" -> "Auto"
        "3" -> "Camioneta"
        "4" -> "Trailer"
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
    Historial.agregar(boleta)
    Pantalla.boleta(boleta)
}

fun buscarPorPlaca() {
    Pantalla.titulo("BOLETA POR PLACA")
    val placa = Validador.normalizarPlaca(leer("  Placa a buscar: "))
    if (placa == null) {
        println("  [X] Placa invalida. Use el formato ABC-123")
        return
    }
    val encontradas = Historial.porPlaca(placa)
    if (encontradas.isEmpty()) {
        println("  No hay boletas registradas para la placa $placa")
        return
    }
    println("  Placa $placa - ${encontradas.size} boleta(s) - cliente frecuente: ${if (RegistroClientes.esFrecuente(placa)) "SI" else "NO"}")
    for (b in encontradas) Pantalla.boleta(b)
}

fun main() {
    // --- COMMIT 5: se pregunta el aforo maximo antes de iniciar el menu ---
    Pantalla.titulo("CONFIGURACION INICIAL")
    var aforo: Int?
    do {
        aforo = leer("  Cuantos vehiculos caben en el estacionamiento (aforo maximo): ").toIntOrNull()
        if (aforo == null || aforo <= 0) println("  [X] Ingresa un numero valido mayor a 0")
    } while (aforo == null || aforo <= 0)
    RegistroVehiculos.aforoMaximo = aforo

    while (true) {
        Pantalla.menu()
        when (leer("  Opcion: ")) {
            "1" -> registrarVehiculo()
            "2" -> Pantalla.historial(Historial.todas())
            "3" -> buscarPorPlaca()
            "4" -> {
                Pantalla.resumenFinal(Historial.todas())
                return
            }
            else -> println("  [X] Opcion no valida. Elige 1, 2, 3 o 4.")
        }
    }
}