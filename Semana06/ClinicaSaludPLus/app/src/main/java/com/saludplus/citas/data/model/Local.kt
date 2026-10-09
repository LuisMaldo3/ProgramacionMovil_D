package com.saludplus.citas.data.model

data class Local(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val telefono: String = "",
    val horarioAtencion: String = "Lun - Sáb: 08:00 - 20:00"
)
