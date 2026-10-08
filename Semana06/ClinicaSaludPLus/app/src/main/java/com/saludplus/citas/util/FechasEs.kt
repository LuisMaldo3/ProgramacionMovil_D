package com.saludplus.citas.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * Utilidades de fechas en español basadas en java.time.LocalDate.
 * Las fechas se guardan en el Repositorio como texto ISO "yyyy-MM-dd"
 * (LocalDate.toString()), por eso el bloqueo de horarios reservados sigue igual.
 */
object FechasEs {

    private val DIAS = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val DIAS_CORTOS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    private val MESES = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
    )

    fun esHabil(fecha: LocalDate): Boolean =
        fecha.dayOfWeek != DayOfWeek.SATURDAY && fecha.dayOfWeek != DayOfWeek.SUNDAY

    /** Si [desde] cae en sábado o domingo, avanza hasta el lunes. */
    fun primerDiaHabil(desde: LocalDate = LocalDate.now()): LocalDate {
        var fecha = desde
        while (!esHabil(fecha)) fecha = fecha.plusDays(1)
        return fecha
    }

    /** Devuelve [cantidad] días hábiles (sin sábados ni domingos) a partir de [desde]. */
    fun diasHabiles(desde: LocalDate, cantidad: Int = 5): List<LocalDate> {
        val resultado = mutableListOf<LocalDate>()
        var fecha = primerDiaHabil(desde)
        while (resultado.size < cantidad) {
            if (esHabil(fecha)) resultado.add(fecha)
            fecha = fecha.plusDays(1)
        }
        return resultado
    }

    fun diaSemanaCorto(fecha: LocalDate): String = DIAS_CORTOS[fecha.dayOfWeek.value - 1]

    fun nombreMes(fecha: LocalDate): String = MESES[fecha.monthValue - 1]

    /** "Octubre 2026". Si la semana cruza de mes: "Octubre – Noviembre 2026". */
    fun mesYAnio(dias: List<LocalDate>): String {
        val primero = dias.first()
        val ultimo = dias.last()
        return when {
            primero.year == ultimo.year && primero.month == ultimo.month ->
                "${nombreMes(primero)} ${primero.year}"
            primero.year == ultimo.year ->
                "${nombreMes(primero)} – ${nombreMes(ultimo)} ${primero.year}"
            else ->
                "${nombreMes(primero)} ${primero.year} – ${nombreMes(ultimo)} ${ultimo.year}"
        }
    }

    /** "Martes 16 de setiembre 2026" */
    fun fechaLarga(fecha: LocalDate): String =
        "${DIAS[fecha.dayOfWeek.value - 1]} ${fecha.dayOfMonth} de " +
                "${nombreMes(fecha).lowercase()} ${fecha.year}"

    fun fechaLarga(iso: String): String =
        try { fechaLarga(LocalDate.parse(iso)) } catch (e: Exception) { iso }

    /** "Mié 7 oct 2026" (para listas) */
    fun fechaCorta(iso: String): String =
        try {
            val f = LocalDate.parse(iso)
            "${diaSemanaCorto(f)} ${f.dayOfMonth} ${nombreMes(f).take(3).lowercase()} ${f.year}"
        } catch (e: Exception) { iso }

    /** "08:30" -> "08:30 a 09:00" (citas de 30 minutos) */
    fun rangoHora(hora: String): String =
        try {
            val inicio = LocalTime.parse(hora)
            val fin = inicio.plusMinutes(30)
            "%02d:%02d a %02d:%02d".format(inicio.hour, inicio.minute, fin.hour, fin.minute)
        } catch (e: Exception) { hora }
}
