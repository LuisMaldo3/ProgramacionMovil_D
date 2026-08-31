package com.maldonado.estacionamiento

/**
 * COMMIT 2 — Cálculos.
 * Tarifa básica por tipo, recargo progresivo por hora (0 % / 20 % / 50 %),
 * cliente frecuente por historial de placa y descuento del 10 %.
 */

/** Una fila de la tabla Hora · Tarifa · Recargo · Importe */
data class DetalleHora(val hora: Int, val tarifa: Double, val recargoPct: Int, val importe: Double)

/** Resultado completo del cálculo de un vehículo */
data class ResultadoTarifa(
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

    /** Tarifa básica por hora según el tipo (regla de la pizarra). */
    fun tarifaBasica(tipo: String): Double = when (tipo) {
        "Moto" -> 2.0
        "Auto" -> 4.0
        "Camioneta" -> 10.0
        else -> 0.0
    }

    /**
     * Recargo de cada hora (progresivo, hora por hora):
     *  - horas 1 y 2 .......... 0 %
     *  - horas 3, 4 y 5 ....... 20 %
     *  - hora 6 en adelante ... 50 %
     */
    fun recargoPorHora(hora: Int): Int = when {
        hora <= 2 -> 0
        hora <= 5 -> 20
        else -> 50
    }

    /** Calcula detalle por hora, subtotal, descuento y total del vehículo. */
    fun calcular(v: Vehiculo): ResultadoTarifa {
        require(v.horas >= 1) { "Ningún vehículo puede registrarse con menos de 1 hora" }

        // Cliente frecuente: se decide por el historial de visitas de la placa
        val esFrecuente = RegistroClientes.esFrecuente(v.placa)
        val visitas = RegistroClientes.registrarVisita(v.placa)

        val tarifa = tarifaBasica(v.tipo)
        val detalle = (1..v.horas).map { hora ->
            val pct = recargoPorHora(hora)
            DetalleHora(hora, tarifa, pct, tarifa * (1 + pct / 100.0))
        }
        val subtotal = detalle.sumOf { it.importe }
        val descuento = if (esFrecuente) subtotal * 0.10 else 0.0
        val total = subtotal - descuento

        return ResultadoTarifa(v, tarifa, detalle, subtotal, esFrecuente, visitas, descuento, total)
    }
}

/**
 * Registro de visitas por placa (en memoria).
 * Criterio: un cliente es FRECUENTE cuando ya tiene 3 o más visitas anteriores,
 * es decir, desde su 4.ª visita obtiene el 10 % de descuento.
 */
object RegistroClientes {
    const val MINIMO_FRECUENTE = 3
    private val visitasPorPlaca = mutableMapOf<String, Int>()

    fun visitasPrevias(placa: String): Int = visitasPorPlaca[placa] ?: 0
    fun esFrecuente(placa: String): Boolean = visitasPrevias(placa) >= MINIMO_FRECUENTE

    fun registrarVisita(placa: String): Int {
        val nuevas = visitasPrevias(placa) + 1
        visitasPorPlaca[placa] = nuevas
        return nuevas
    }
}