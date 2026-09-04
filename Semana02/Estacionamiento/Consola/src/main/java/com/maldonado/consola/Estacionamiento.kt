package com.maldonado.consola

/*
 * CONTROL DE ESTACIONAMIENTO - programa de consola en Kotlin
 *
 * Commit 1: Ingreso de datos     -> registro de placa, tipo, horas y cliente con validaciones
 * Commit 2: Calculos             -> tarifa basica, recargos por hora, cliente frecuente y descuento
 * Commit 3: Mostrar resultados   -> boleta con tabla Hora/Tarifa/Recargo/Importe, historial y boleta por placa
 * Commit 4: Nuevo vehiculo Trailer -> se agrega el tipo "Trailer" con tarifa basica de S/ 20
 * Commit 5: Recargos del Trailer y aforo -> tabla de recargo propia para Trailer (1-2h: 0%, 3-5h: 20%, 6-10h: 40%, +10h: 50%);
 *                                            se pregunta el aforo maximo al iniciar y no se registran vehiculos si ya esta lleno
 * Commit 6: IGV y descuento extra  -> si el total supera S/ 500 se aplica 20% de descuento adicional,
 *                                     y al final se agrega el IGV (18%) sobre el total
 * Commit 7: Limite maximo de horas -> no se permite registrar un vehiculo por mas de 48 horas
 *
 * Reglas:
 *  - Tarifa por hora: Moto S/ 2, Auto S/ 4, Camioneta S/ 10, Trailer S/ 20
 *  - Moto / Auto / Camioneta -> hasta 2 horas: tarifa normal; 3 a 5 horas: recargo 20%; mas de 5 horas: recargo 50%
 *  - Trailer -> hasta 2 horas: tarifa normal; 3 a 5 horas: recargo 20%; 6 a 10 horas: recargo 40%; mas de 10 horas: recargo 50%
 *  - Cliente frecuente (mas de 3 visitas con la misma placa): 10 % de descuento sobre el total
 *  - Si el total (despues del descuento de cliente frecuente) supera S/ 500: 20 % de descuento adicional
 *  - Al final se agrega el IGV del 18 % sobre el total
 *  - Ningun vehiculo puede registrarse con menos de 1 hora ni con mas de 48 horas
 *  - No se registran vehiculos si ya se llego al aforo maximo del estacionamiento
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
    // --- COMMIT 4: Nuevo vehiculo Trailer (se agrega a la lista de tipos permitidos) ---
    val tiposPermitidos = listOf("Moto", "Auto", "Camioneta", "Trailer")
    private val vehiculos = mutableListOf<Vehiculo>()

    // --- COMMIT 5: Aforo maximo del estacionamiento (se define al iniciar el programa) ---
    var aforoMaximo: Int = Int.MAX_VALUE

    // --- COMMIT 7: limite maximo de horas por vehiculo (evita que el programa se cuelgue) ---
    const val HORAS_MAXIMAS = 48

    fun ingresar(placa: String, tipo: String, horas: Int, cliente: String): String? {
        val placaOk = Validador.normalizarPlaca(placa)
            ?: return "Placa invalida. Use el formato ABC-123 (3 caracteres, guion, 3 caracteres)"
        if (tipo !in tiposPermitidos) return "Tipo invalido. Use Moto, Auto, Camioneta o Trailer"
        if (horas < 1) return "Ningun vehiculo puede registrarse con menos de 1 hora"
        // --- COMMIT 7: no se registra si supera el limite maximo de horas ---
        if (horas > HORAS_MAXIMAS) return "No se puede registrar por mas de $HORAS_MAXIMAS horas"
        if (cliente.isBlank()) return "El nombre del cliente es obligatorio"
        // --- COMMIT 5: no se registra si ya se llego al aforo maximo ---
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
    // --- COMMIT 6: nuevos campos para descuento extra e IGV ---
    val totalConDescuento: Double,
    val aplicaDescuentoExtra: Boolean,
    val descuentoExtra: Double,
    val totalAntesIgv: Double,
    val igv: Double,
    val total: Double
)

object CalculadoraTarifa {

    // --- COMMIT 4: Nuevo vehiculo Trailer (tarifa basica S/ 20) ---
    fun tarifaBasica(tipo: String): Double = when (tipo) {
        "Moto" -> 2.0
        "Auto" -> 4.0
        "Camioneta" -> 10.0
        "Trailer" -> 20.0
        else -> 0.0
    }

    /**
     * --- COMMIT 5: Recargos del Trailer ---
     * Recargo de cada hora segun el tipo de vehiculo:
     *  - Moto / Auto / Camioneta: 0 % (horas 1-2), 20 % (horas 3-5), 50 % (hora 6 en adelante)
     *  - Trailer: 0 % (horas 1-2), 20 % (horas 3-5), 40 % (horas 6 a 10), 50 % (hora 11 en adelante)
     */
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

    // --- COMMIT 6: IGV y descuento extra ---
    const val UMBRAL_DESCUENTO_EXTRA = 500.0
    const val PORCENTAJE_DESCUENTO_EXTRA = 0.20
    const val PORCENTAJE_IGV = 0.18

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
        val totalConDescuento = subtotal - descuento

        // --- COMMIT 6: si el total supera S/ 500, se aplica 20% de descuento adicional ---
        val aplicaDescuentoExtra = totalConDescuento > UMBRAL_DESCUENTO_EXTRA
        val descuentoExtra = if (aplicaDescuentoExtra) totalConDescuento * PORCENTAJE_DESCUENTO_EXTRA else 0.0
        val totalAntesIgv = totalConDescuento - descuentoExtra

        // --- COMMIT 6: se agrega el IGV (18%) sobre el total ---
        val igv = totalAntesIgv * PORCENTAJE_IGV
        val totalFinal = totalAntesIgv + igv

        contadorBoletas++
        return Boleta(
            contadorBoletas, v, tarifa, detalle, subtotal, esFrecuente, visitas,
            descuento, totalConDescuento, aplicaDescuentoExtra, descuentoExtra,
            totalAntesIgv, igv, totalFinal
        )
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
        // --- COMMIT 6: se muestra el descuento extra (si supera S/ 500) y el IGV ---
        if (b.aplicaDescuentoExtra) {
            println(String.format("  %-28s S/ %8s", "Descuento extra 20 % (>S/500)", "-" + s(b.descuentoExtra)))
        }
        println(String.format("  %-28s S/ %8s", "Subtotal antes de IGV", s(b.totalAntesIgv)))
        println(String.format("  %-28s S/ %8s", "IGV 18 %", "+" + s(b.igv)))
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
            boletas.size, s(boletas.sumOf { it.descuento + it.descuentoExtra }), s(boletas.sumOf { it.total })))
    }

    fun resumenFinal(boletas: List<Boleta>) {
        titulo("RESUMEN DEL DIA")
        println("  Vehiculos atendidos : ${boletas.size}")
        println("  Total sin descuento : S/ ${s(boletas.sumOf { it.subtotal })}")
        println("  Total descuentos    : S/ ${s(boletas.sumOf { it.descuento + it.descuentoExtra })}")
        println("  Total IGV           : S/ ${s(boletas.sumOf { it.igv })}")
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