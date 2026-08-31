package com.maldonado.estacionamiento

/**
 * COMMIT 3 — Mostrar resultados.
 * Arma el texto del reporte con el formato de la pizarra:
 * Tarifa básica, tabla Hora · Tarifa · Recargo · Importe y Total.
 */
object Reporte {

    private fun s(v: Double) = "%.2f".format(v)

    fun deVehiculo(r: ResultadoTarifa): String {
        val v = r.vehiculo
        val sb = StringBuilder()
        sb.appendLine("══════════════════════════════════════")
        sb.appendLine("Placa:   ${v.placa}")
        sb.appendLine("Tipo:    ${v.tipo}")
        sb.appendLine("Horas:   ${v.horas}")
        sb.appendLine("Cliente: ${v.cliente}")
        sb.appendLine("Tarifa básica: S/ ${s(r.tarifaBasica)}")
        sb.appendLine("--------------------------------------")
        sb.appendLine(String.format("%-6s %-8s %-8s %s", "Hora", "Tarifa", "Recargo", "Importe"))
        for (d in r.detalle) {
            sb.appendLine(String.format("%-6d %-8s %-8s %s", d.hora, s(d.tarifa), "${d.recargoPct}%", s(d.importe)))
        }
        sb.appendLine("--------------------------------------")
        sb.appendLine("Subtotal:          S/ ${s(r.subtotal)}")
        if (r.esFrecuente) {
            sb.appendLine("Cliente frecuente: SÍ (${r.visitas} visitas)")
            sb.appendLine("Descuento 10%:    -S/ ${s(r.descuento)}")
        } else {
            sb.appendLine("Cliente frecuente: NO (${r.visitas} de ${RegistroClientes.MINIMO_FRECUENTE + 1} visitas)")
        }
        sb.appendLine("TOTAL:             S/ ${s(r.total)}")
        return sb.toString()
    }

    fun completo(resultados: List<ResultadoTarifa>): String {
        val sb = StringBuilder()
        sb.appendLine("🅿 CONTROL DE ESTACIONAMIENTO — REPORTE")
        sb.appendLine("Vehículos registrados: ${resultados.size}")
        resultados.forEach { sb.append(deVehiculo(it)) }
        sb.appendLine("══════════════════════════════════════")
        sb.appendLine("RECAUDACIÓN TOTAL: S/ ${s(resultados.sumOf { it.total })}")
        return sb.toString()
    }
}