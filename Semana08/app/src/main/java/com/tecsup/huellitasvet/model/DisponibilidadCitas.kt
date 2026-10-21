package com.tecsup.huellitasvet.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Reglas de horario sin dependencias de Android, compartidas por ViewModel y Repository. */
object DisponibilidadCitas {
    private val apertura = LocalTime.of(9, 0)
    private val cierre = LocalTime.of(18, 0)

    fun permitido(fecha: LocalDate, hora: String, duracionMin: Int, ahora: LocalDateTime = LocalDateTime.now()): Boolean {
        val inicio = runCatching { LocalTime.parse(hora) }.getOrNull() ?: return false
        if (duracionMin <= 0 || hora !in Horarios.todos) return false
        if (fecha.isBefore(ahora.toLocalDate()) || fecha.isAfter(ahora.toLocalDate().plusDays(7))) return false
        if (inicio.isBefore(apertura) || inicio.plusMinutes(duracionMin.toLong()).isAfter(cierre)) return false
        return LocalDateTime.of(fecha, inicio).isAfter(ahora)
    }

    fun seCruzan(hora1: String, duracion1: Int, hora2: String, duracion2: Int): Boolean {
        val a = runCatching { LocalTime.parse(hora1).toSecondOfDay() / 60 }.getOrNull() ?: return true
        val b = runCatching { LocalTime.parse(hora2).toSecondOfDay() / 60 }.getOrNull() ?: return true
        return a < b + duracion2.coerceAtLeast(1) && b < a + duracion1.coerceAtLeast(1)
    }

    fun libre(hora: String, duracionMin: Int, ocupadas: List<HorarioOcupado>, ignorarCitaId: Int? = null): Boolean =
        ocupadas.none { existente ->
            existente.citaId != ignorarCitaId && seCruzan(hora, duracionMin, existente.hora, existente.duracionMin)
        }
}
