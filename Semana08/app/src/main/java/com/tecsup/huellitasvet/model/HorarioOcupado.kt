package com.tecsup.huellitasvet.model

/** Proyección de Room con la duración real de cada cita activa. */
data class HorarioOcupado(
    val citaId: Int,
    val hora: String,
    val duracionMin: Int
)
