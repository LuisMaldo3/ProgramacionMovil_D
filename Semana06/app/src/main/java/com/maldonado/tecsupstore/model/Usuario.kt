package com.maldonado.tecsupstore.model

data class Usuario(
    val nombre: String,
    val correo: String
) {
    // "Maria Rojas" -> "MR"
    val iniciales: String
        get() = nombre
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
}
